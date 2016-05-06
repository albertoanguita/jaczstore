package jacz.database;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;
import jacz.database.util.GenreCode;
import jacz.database.util.LocalizedLanguage;
import jacz.database.util.QualityCode;
import jacz.storage.ActiveJDBCController;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Generic database item
 * <p/>
 * <p/>
 */
public abstract class DatabaseItem {

    private static final String LIST_SEPARATOR = "\n";

    private Model model;

    protected final String dbPath;

    private final Map<DatabaseMediator.Field, Object> pendingChanges;

    public DatabaseItem(String dbPath) {
        this(dbPath, null, new HashMap<>());
    }

    public DatabaseItem(String dbPath, Map<DatabaseMediator.Field, Object> initialValues) {
        this(dbPath, null, initialValues);
    }

    public DatabaseItem(String dbPath, Integer id) {
        this(dbPath, id, null);
    }

    public DatabaseItem(String dbPath, Integer id, Map<DatabaseMediator.Field, Object> initialValues) {
        this.dbPath = dbPath;
        pendingChanges = new HashMap<>();
        try {
            connect();
            openTransaction();
            this.model = getItemType().modelClass.newInstance();
            if (id != null) {
                model.setId(id);
                model.insert();
            }
            set(DatabaseMediator.Field.CREATION_DATE, System.currentTimeMillis(), false);
            if (initialValues != null) {
                for (Map.Entry<DatabaseMediator.Field, Object> initialValue : initialValues.entrySet()) {
                    set(initialValue.getKey(), initialValue.getValue(), false);
                }
            }
            flushChanges();
            commitTransaction();
        } catch (IllegalAccessException | InstantiationException e) {
            DatabaseMediator.reportError("Error retrieving item with id", dbPath, id, e);
            rollbackTransaction();
        } finally {
            disconnect();
        }
    }

    DatabaseItem(Model model, String dbPath) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
        this.model = model;
        this.dbPath = dbPath;
        pendingChanges = new HashMap<>();
    }

    public abstract DatabaseMediator.ItemType getItemType();

    public abstract boolean isOrphan();

    static boolean contains(Collection<? extends DatabaseItem> items, Model model) {
        for (DatabaseItem item : items) {
            if (item.getId().equals(model.getInteger(DatabaseMediator.Field.ID.value))) {
                return true;
            }
        }
        return false;
    }

    public Integer getId() {
        return (Integer) model.getId();
    }

    public Date getCreationDate() {
        return new Date(getLong(DatabaseMediator.Field.CREATION_DATE));
    }

    String getString(DatabaseMediator.Field field) {
        if (pendingChanges.containsKey(field)) {
            return (String) pendingChanges.get(field);
        }
        return model.getString(field.value);
    }

    Integer getInteger(DatabaseMediator.Field field) {
        if (pendingChanges.containsKey(field)) {
            return (Integer) pendingChanges.get(field);
        }
        return model.getInteger(field.value);
    }

    Long getLong(DatabaseMediator.Field field) {
        if (pendingChanges.containsKey(field)) {
            return (Long) pendingChanges.get(field);
        }
        return model.getLong(field.value);
    }

    Date getDate(DatabaseMediator.Field field) {
        return new Date(getLong(field));
    }

    <E> E getEnum(DatabaseMediator.Field field, Class<E> enum_) {
        try {
            Method valueOf = enum_.getMethod("valueOf", String.class);
            String fieldValue = getString(field);
            return fieldValue != null ? (E) valueOf.invoke(null, fieldValue) : null;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            DatabaseMediator.reportError("Error retrieving enum from the database with valueOf", dbPath, field, enum_, e);
            return null;
        }
    }

    private void updateTimestamp() {
        updateTimestamp(model);
    }

    private void updateTimestamp(Model model) {
        connect();
        model.set(DatabaseMediator.Field.TIMESTAMP.value, DatabaseMediator.getNewTimestamp(dbPath));
        disconnect();
    }

    public long getTimestamp() {
        return getLong(DatabaseMediator.Field.TIMESTAMP);
    }

    /**
     * Manually set the timestamp of this item
     *
     * @param timestamp new timestamp of this item
     */
    public void setTimestamp(long timestamp, boolean flush) {
        set(DatabaseMediator.Field.TIMESTAMP, timestamp, flush);
    }

    public List<String> getTags() {
        return Tag.getItemTags(dbPath, this, getItemType());
    }

    public boolean addTag(String tag) {
        return Tag.addTag(dbPath, this, tag, getItemType());
    }

    public boolean removeTag(String tag) {
        return Tag.removeTag(dbPath, this, tag, getItemType());
    }

    protected void set(DatabaseMediator.Field field, Object value, boolean flush) {
        pendingChanges.put(field, value);
        if (flush) {
            flushChanges();
        }
    }

    protected <E> void setEnum(DatabaseMediator.Field field, Class<E> enum_, Object value, String getNameMethod, boolean flush) {
        try {
            Method getName = enum_.getMethod(getNameMethod);
            String str = value != null ? (String) getName.invoke(value) : null;
            set(field, str, flush);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            DatabaseMediator.reportError("Error retrieving enums from the database with user provided getNameMethod", dbPath, field, enum_, value, getNameMethod, flush, e);
        }
    }

    public void reset() {
        reset(true);
    }

    public void resetPostponed() {
        reset(false);
    }

    private void reset(boolean flush) {
        for (DatabaseMediator.Field field : getItemType().fields) {
            if (field.canBeReset()) {
                set(field, null, flush);
            }
        }
    }

    public void flushChanges() {
        connect();
        openTransaction();
        updateTimestamp();
        for (Map.Entry<DatabaseMediator.Field, Object> change : pendingChanges.entrySet()) {
            model.set(change.getKey().value, change.getValue());
            if (change.getKey() == DatabaseMediator.Field.TIMESTAMP) {
                DatabaseMediator.updateHighestManualTimestamp(dbPath, (Long) change.getValue());
            }
        }
        DatabaseMediator.updateLastUpdateTime(dbPath);
        save();
        commitTransaction();
        disconnect();
        pendingChanges.clear();
    }

    public float match(DatabaseItem anotherItem) {
        // no fields at this level indicate equality
        return 0f;
    }

    public void merge(DatabaseItem anotherItem) {
        merge(anotherItem, getReferencedElements());
    }

    public void merge(DatabaseItem anotherItem, DatabaseMediator.ReferencedElements referencedElements) {
        mergeBasicPostponed(anotherItem);
        mergeReferencedElementsPostponed(referencedElements);
        flushChanges();
    }

    public final void mergePostponed(DatabaseItem anotherItem) {
        mergeBasicPostponed(anotherItem);
        mergeReferencedElementsPostponed(getReferencedElements());
    }

    public abstract void mergeBasicPostponed(DatabaseItem anotherItem);

    public DatabaseMediator.ReferencedElements getReferencedElements() {
        return new DatabaseMediator.ReferencedElements();
    }

    public void mergeReferencedElementsPostponed(DatabaseMediator.ReferencedElements referencedElements) {
    }

//    static float evaluateListSimilarity(ListSimilarity listSimilarity, float confidence) {
//        int min = Math.min(listSimilarity.firstListSize, listSimilarity.secondListSize);
//        if (min != 0) {
//            return confidence * (listSimilarity.commonItems / Math.min(listSimilarity.firstListSize, listSimilarity.secondListSize));
//        } else {
//            return 0;
//        }
//    }

    protected void save() {
        connect();
        model.saveIt();
        disconnect();
    }

    protected static <C extends Model> List<C> getModels(String dbPath, DatabaseMediator.ItemType type) {
        DatabaseMediator.connect(dbPath);
        try {
            Method findAll = type.modelClass.getMethod("findAll");
            return ((LazyList<C>) findAll.invoke(null)).load();
        } catch (Exception e) {
            DatabaseMediator.reportError("Error retrieving models from the database with findAll", dbPath, type, e);
            return new ArrayList<>();
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected static <C extends Model> List<C> getModels(String dbPath, DatabaseMediator.ItemType type, String query, Object... params) {
        DatabaseMediator.connect(dbPath);
        try {
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            params = params != null ? params : new Object[0];
            return ((LazyList<C>) where.invoke(null, query, params)).load();
        } catch (Exception e) {
            DatabaseMediator.reportError("Error retrieving models from the database with where", dbPath, type, query, params, e);
            return new ArrayList<>();
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected static <C extends Model> C getModelById(String dbPath, DatabaseMediator.ItemType type, int id) {
        DatabaseMediator.connect(dbPath);
        try {
            Object[] params = {id};
            List<C> models = getModels(dbPath, type, "id = ?", params);
            return models.isEmpty() ? null : models.get(0);
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected <C extends Model> List<C> getReferencedElements(DatabaseMediator.ItemType type, DatabaseMediator.Field field) {
        Object[] ids = getStringList(field).toArray();
        connect();
        try {
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            StringBuilder query = new StringBuilder("id = -1");
            for (int i = 0; i < ids.length; i++) {
                query.append(" OR id = ?");
            }
            return ((LazyList<C>) where.invoke(null, query.toString(), ids)).load();
        } catch (Exception e) {
            DatabaseMediator.reportError("Error retrieving models from the database with where", dbPath, type, field, ids, e);
            return null;
        } finally {
            disconnect();
        }
    }

//    protected List<String> getReferencedElementsIds(DatabaseMediator.ItemType type, DatabaseMediator.Field field) {
//        return getStringList(field);
//    }

    protected List<Integer> getReferencedElementsIds(DatabaseMediator.Field field) {
        List<Integer> idList = new ArrayList<>();
        for (String id : getStringList(field)) {
            idList.add((Integer.parseInt(id)));
        }
        return idList;
    }

    protected void removeReferencedElements(DatabaseMediator.Field field, boolean flush) {
        removeList(field, flush);
    }

    protected <C extends Model> boolean removeReferencedElement(DatabaseMediator.Field field, DatabaseItem item, boolean flush) {
        return removeReferencedElementById(field, item.getId().toString(), flush);
    }

    protected <C extends Model> boolean removeReferencedElementById(DatabaseMediator.Field field, String id, boolean flush) {
        List<String> stringList = getStringList(field);
        boolean removed = stringList.remove(id);
        setStringList(field, stringList, flush);
        return removed;
    }

    protected void setReferencedElements(DatabaseMediator.Field field, List<? extends DatabaseItem> models, boolean flush) {
        List<String> idList = new ArrayList<>();
        for (DatabaseItem item : models) {
            idList.add(item.getId().toString());
        }
        setStringList(field, idList, flush);
    }

    protected void setReferencedElementsIds(DatabaseMediator.Field field, List<Integer> idList, boolean flush) {
        setStringList(field, idList, flush);
    }

    protected void setReferencedElements(DatabaseMediator.Field field, boolean flush, DatabaseItem... models) {
        List<String> idList = new ArrayList<>();
        for (DatabaseItem item : models) {
            idList.add(item.getId().toString());
        }
        setStringList(field, idList, flush);
    }

    protected <C extends Model> boolean addReferencedElement(DatabaseMediator.Field field, DatabaseItem item, boolean flush) {
//        return addStringValue(field, item.getId().toString(), flush);
        return addReferencedElementId(field, item.getId(), flush);
    }

    protected <C extends Model> boolean addReferencedElementId(DatabaseMediator.Field field, Integer itemId, boolean flush) {
        return addStringValue(field, itemId.toString(), flush);
    }

    /**
     * Database must be previously connected
     *
     * @param type
     * @param field
     * @param <C>
     * @return
     */
    protected <C extends Model> List<C> getElementsContainingMe(DatabaseMediator.ItemType type, DatabaseMediator.Field field) {
        Object[] myId = new String[]{"%\n" + getId().toString() + "\n%"};
        try {
            connect();
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            String query = field.value + " LIKE ?";
            return ((LazyList<C>) where.invoke(null, query, myId)).load();
        } catch (Exception e) {
            DatabaseMediator.reportError("Error retrieving models from the database with where", dbPath, type, field, myId, e);
            return null;
        } finally {
            disconnect();
        }
    }

    protected List<String> getStringList(DatabaseMediator.Field field) {
        return deserializeList(getString(field));
    }

    protected void removeStringList(DatabaseMediator.Field field, boolean flush) {
        removeList(field, flush);
    }

    protected boolean removeStringValue(DatabaseMediator.Field field, String value, boolean flush) {
        List<String> stringList = getStringList(field);
        boolean removed = stringList.remove(value);
        setStringList(field, stringList, flush);
        return removed;
    }

    protected void setStringList(DatabaseMediator.Field field, List<?> stringList, boolean flush) {
        set(field, serializeList(stringList), flush);
    }

    protected boolean addStringValue(DatabaseMediator.Field field, String value, boolean flush) {
        List<String> stringList = getStringList(field);
        boolean notContains = !stringList.contains(value);
        if (notContains) {
            stringList.add(value);
        }
        setStringList(field, stringList, flush);
        return notContains;
    }

    protected <E> List<E> getEnums(DatabaseMediator.Field field, Class<E> enum_) {
        try {
            Method valueOf = enum_.getMethod("valueOf", String.class);
            List<E> enumValues = new ArrayList<>();
            for (String str : getStringList(field)) {
                enumValues.add((E) valueOf.invoke(null, str));
            }
            return enumValues;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            DatabaseMediator.reportError("Error retrieving enums from the database with valueOf", dbPath, field, enum_, e);
            return new ArrayList<>();
        }
    }

    protected <E> boolean removeEnum(DatabaseMediator.Field field, Class<E> enum_, E value, String getNameMethod, boolean flush) {
        List<E> enums = getEnums(field, enum_);
        boolean removed = enums.remove(value);
        setEnums(field, enum_, enums, getNameMethod, flush);
        return removed;
    }

    protected <E> void setEnums(DatabaseMediator.Field field, Class<E> enum_, List<E> values, String getNameMethod, boolean flush) {
        try {
            Method getName = enum_.getMethod(getNameMethod);
            List<String> strList = new ArrayList<>();
            for (E value : values) {
                strList.add((String) getName.invoke(value));
            }
            set(field, serializeList(strList), flush);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            DatabaseMediator.reportError("Error retrieving enums from the database with user provided getNameMethod", dbPath, field, enum_, values, getNameMethod, flush, e);
        }
    }

    protected <E> boolean addEnums(DatabaseMediator.Field field, Class<E> enum_, List<E> newValues, String getNameMethod, boolean flush) {
        List<E> values = getEnums(field, enum_);
        boolean modified = false;
        for (E newValue : newValues) {
            boolean notContains = !values.contains(newValue);
            if (notContains) {
                values.add(newValue);
                modified = true;
            }
        }
        setEnums(field, enum_, values, getNameMethod, flush);
        return modified;
    }

    protected <E> boolean addEnum(DatabaseMediator.Field field, Class<E> enum_, E value, String getNameMethod, boolean flush) {
        List<E> values = getEnums(field, enum_);
        boolean notContains = !values.contains(value);
        if (notContains) {
            values.add(value);
        }
        setEnums(field, enum_, values, getNameMethod, flush);
        return notContains;
    }

    protected void removeList(DatabaseMediator.Field field, boolean flush) {
        set(field, null, flush);
    }

    protected String serializeList(List<?> list) {
        if (list.isEmpty()) {
            return "";
        } else {
            StringBuilder serList = new StringBuilder(LIST_SEPARATOR);
            for (Object item : list) {
                serList.append(item.toString()).append(LIST_SEPARATOR);
            }
            return serList.toString();
        }
    }

    protected List<String> deserializeList(String value) {
        value = value == null ? "" : value;
        StringTokenizer tokenizer = new StringTokenizer(value, LIST_SEPARATOR);
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //noinspection SimplifiableIfStatement
        if (!(o instanceof DatabaseItem)) return false;

        return equals(o, getReferencedElements());
    }

    public boolean equals(Object o, DatabaseMediator.ReferencedElements referencedElements) {
        if (this == o) return true;
        if (!(o instanceof DatabaseItem)) return false;

        DatabaseItem that = (DatabaseItem) o;

        for (DatabaseMediator.Field field : getItemType().fields) {
            if (field.canBeCompared()) {
                switch (field.dbType) {

                    case ID:
                        // cannot happen
                        return false;
                    case TEXT:
                        String str1 = getString(field);
                        String str2 = that.getString(field);
                        if (onlyOneDefined(str1, str2)) {
                            return false;
                        }
                        if (bothDefined(str1, str2) && !str1.equals(str2)) {
                            return false;
                        }
                        break;
                    case INTEGER:
                        Integer int1 = getInteger(field);
                        Integer int2 = that.getInteger(field);
                        if (onlyOneDefined(int1, int2)) {
                            return false;
                        }
                        if (bothDefined(int1, int2) && !int1.equals(int2)) {
                            return false;
                        }
                        break;
                    case LONG:
                        Long long1 = getLong(field);
                        Long long2 = that.getLong(field);
                        if (onlyOneDefined(long1, long2)) {
                            return false;
                        }
                        if (bothDefined(long1, long2) && !long1.equals(long2)) {
                            return false;
                        }
                        break;
                    case DATE:
                        Date date1 = getDate(field);
                        Date date2 = that.getDate(field);
                        if (onlyOneDefined(date1, date2)) {
                            return false;
                        }
                        if (bothDefined(date1, date2) && !date1.equals(date2)) {
                            return false;
                        }
                        break;
                    case QUALITY:
                        QualityCode quality1 = getEnum(field, QualityCode.class);
                        QualityCode quality2 = that.getEnum(field, QualityCode.class);
                        if (onlyOneDefined(quality1, quality2)) {
                            return false;
                        }
                        if (bothDefined(quality1, quality2) && !quality1.equals(quality2)) {
                            return false;
                        }
                        break;
                    case ID_LIST:
                        // referenced elements are taken from the corresponding argument instead of from 'that'
                        List<Integer> idList1 = getReferencedElementsIds(field);
                        List<Integer> idList2 = referencedElements.get(field.getReferencedType(), field.getReferencedList());
                        if (!idList1.equals(idList2)) {
                            return false;
                        }
                        break;
                    case STRING_LIST:
                        List<String> strList1 = getStringList(field);
                        List<String> strList2 = that.getStringList(field);
                        if (!strList1.equals(strList2)) {
                            return false;
                        }
                        break;
                    case GENRE_LIST:
                        List<GenreCode> genreList1 = getEnums(DatabaseMediator.Field.GENRES, GenreCode.class);
                        List<GenreCode> genreList2 = that.getEnums(DatabaseMediator.Field.GENRES, GenreCode.class);
                        if (!genreList1.equals(genreList2)) {
                            return false;
                        }
                        break;
                    case LANGUAGE:
                        LanguageCode language1 = getEnum(DatabaseMediator.Field.LANGUAGE, LanguageCode.class);
                        LanguageCode language2 = that.getEnum(DatabaseMediator.Field.LANGUAGE, LanguageCode.class);
                        if (onlyOneDefined(language1, language2)) {
                            return false;
                        }
                        if (bothDefined(language1, language2) && !language1.equals(language2)) {
                            return false;
                        }
                        break;
                    case LOCALIZED_LANGUAGE:
                        LocalizedLanguage localizedLanguage1 = LocalizedLanguage.deserialize(getString(DatabaseMediator.Field.LOCALIZED_LANGUAGE));;
                        LocalizedLanguage localizedLanguage2 = LocalizedLanguage.deserialize(that.getString(DatabaseMediator.Field.LOCALIZED_LANGUAGE));;
                        if (onlyOneDefined(localizedLanguage1, localizedLanguage2)) {
                            return false;
                        }
                        if (bothDefined(localizedLanguage1, localizedLanguage2) && !localizedLanguage1.equals(localizedLanguage2)) {
                            return false;
                        }
                        break;
                    case LOCALIZED_LANGUAGE_LIST:
                        List<LocalizedLanguage> localizedLanguageList1 = LocalizedLanguage.deserialize(getStringList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST));
                        List<LocalizedLanguage> localizedLanguageList2 = LocalizedLanguage.deserialize(that.getStringList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST));
                        if (!localizedLanguageList1.equals(localizedLanguageList2)) {
                            return false;
                        }
                        break;
                    case LANGUAGE_LIST:
                        List<LanguageCode> languageList1 = getEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class);
                        List<LanguageCode> languageList2 = that.getEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class);
                        if (!languageList1.equals(languageList2)) {
                            return false;
                        }
                        break;
                    case COUNTRY_LIST:
                        List<CountryCode> countryList1 = getEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class);
                        List<CountryCode> countryList2 = that.getEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class);
                        if (!countryList1.equals(countryList2)) {
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    private boolean onlyOneDefined(Object o1, Object o2) {
        return (o1 != null && o2 == null) || (o1 == null && o2 != null);
    }

    private boolean bothDefined(Object o1, Object o2) {
        return (o1 != null && o2 != null);
    }

    @Override
    public int hashCode() {
        return pendingChanges.hashCode();
    }

    public void delete() {
        connect();
        Tag.removeItem(getItemType(), getId());
        model.delete();
        disconnect();
    }

    protected void connect() {
        DatabaseMediator.connect(dbPath);
    }

    protected void openTransaction() {
        ActiveJDBCController.getDB().openTransaction();
    }

    protected void commitTransaction() {
        ActiveJDBCController.getDB().commitTransaction();
    }

    protected void rollbackTransaction() {
        ActiveJDBCController.getDB().rollbackTransaction();
    }

    protected void disconnect() {
        DatabaseMediator.disconnect(dbPath);
    }
}
