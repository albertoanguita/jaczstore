package jacz.database;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

/**
 * Generic database item
 */
public abstract class DatabaseItem {

    private static final String LIST_SEPARATOR = "\n";

    private Model model;

    protected final String dbPath;

    private final Map<DatabaseMediator.Field, Object> pendingChanges;

    public DatabaseItem(String dbPath) {
        this.dbPath = dbPath;
        pendingChanges = new HashMap<>();
        try {
            connect();
            this.model = getItemType().modelClass.newInstance();
            set(DatabaseMediator.Field.CREATION_DATE, DatabaseMediator.dateFormat.format(new Date()), true);
            save();
            disconnect();
        } catch (Exception e) {
            // todo fatal error
            System.exit(1);
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

    protected abstract DatabaseMediator.ItemType getItemType();

    static boolean contains(Collection<? extends DatabaseItem> items, Model model) {
        for (DatabaseItem item : items) {
            if (item.getId().equals(model.getInteger(DatabaseMediator.Field.ID.value))) {
                return true;
            }
        }
        return false;
    }

    public Integer getId() {
        updateLastAccessTime();
        return (Integer) model.getId();
    }

    public Date getCreationDate() {
        try {
            return DatabaseMediator.dateFormat.parse(getString(DatabaseMediator.Field.CREATION_DATE));
        } catch (ParseException e) {
            // error reading the stored date
            // todo set new date and return it
            return null;
        }
    }

    public String getString(DatabaseMediator.Field field) {
        updateLastAccessTime();
        return model.getString(field.value);
    }

    public Integer getInteger(DatabaseMediator.Field field) {
        updateLastAccessTime();
        return model.getInteger(field.value);
    }

    public Long getLong(DatabaseMediator.Field field) {
        updateLastAccessTime();
        return model.getLong(field.value);
    }

    public void updateLastAccessTime() {
        DatabaseMediator.updateLastAccessTime(dbPath);
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

    protected void set(DatabaseMediator.Field field, Object value, boolean flush) {
        pendingChanges.put(field, value);
        if (flush) {
            flushChanges();
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
            set(field, null, flush);
        }
    }

    public void flushChanges() {
        // todo use a transaction so all changes or none are set
        connect();
        for (Map.Entry<DatabaseMediator.Field, Object> change : pendingChanges.entrySet()) {
            model.set(change.getKey().value, change.getValue());
            if (change.getKey() == DatabaseMediator.Field.TIMESTAMP) {
                DatabaseMediator.updateHighestTimestamp(dbPath, (Long) change.getValue());
            }
        }
        DatabaseMediator.updateLastUpdateTime(dbPath);
        updateTimestamp();
        save();
        disconnect();
        pendingChanges.clear();
    }

    public float match(DatabaseItem anotherItem, ListSimilarity... listSimilarities) {
        // no fields of this class indicate equality
        return 0f;
    }

    public abstract void merge(DatabaseItem anotherItem);

    public abstract void mergePostponed(DatabaseItem anotherItem);

    static float evaluateListSimilarity(ListSimilarity listSimilarity, float confidence) {
        int min = Math.min(listSimilarity.firstListSize, listSimilarity.secondListSize);
        if (min != 0) {
            return confidence * (listSimilarity.commonItems / Math.min(listSimilarity.firstListSize, listSimilarity.secondListSize));
        } else {
            return 0;
        }
    }

    protected void save() {
        connect();
        model.saveIt();
        disconnect();
    }

    protected static <C extends Model> List<C> getModels(String dbPath, DatabaseMediator.ItemType type) {
        DatabaseMediator.connect(dbPath);
        try {
            Method findAll = type.modelClass.getMethod("findAll");
            List<C> models;
            models = (List<C>) findAll.invoke(null);
            return models;
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected static <C extends Model> List<C> getModels(String dbPath, DatabaseMediator.ItemType type, String query, Object... params) {
        DatabaseMediator.connect(dbPath);
        try {
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            params = params != null ? params : new Object[0];
            List<C> models;
            models = (List<C>) where.invoke(null, query, params);
            return models;
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
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

//    protected <C extends Model> Model getDirectAssociationParent(String dbPath, Class<C> parentClass) {
//        connect();
//        try {
//            return model.parent(parentClass);
//        } finally {
//            disconnect();
//        }
//    }
//
//    protected void setDirectAssociationParent(DatabaseItem item) {
//        connect();
//        item.model.add(model);
//        save();
//        disconnect();
//    }
//
//    protected <C extends Model> void removeDirectAssociationParent(Class<C> parentClass) {
//        connect();
//        model.parent(parentClass).delete();
//        save();
//        disconnect();
//    }
//
//    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass) {
//        connect();
//        try {
//            return model.getAll(childClass);
//        } finally {
//            disconnect();
//        }
//    }
//
//    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass, String query, Object... params) {
//        connect();
//        try {
//            return model.get(childClass, query, params);
//        } finally {
//            disconnect();
//        }
//    }
//
//    protected <C extends Model> void removeDirectAssociationChildren(Class<C> childClass) {
//        // todo use transaction
//        connect();
//        try {
//            List<C> children = getDirectAssociationChildren(childClass);
//            for (C child : children) {
//                child.delete();
//                updateTimestamp(child);
//            }
//        } finally {
//            disconnect();
//        }
//    }
//
//    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, List<? extends DatabaseItem> items) {
//        connect();
//        try {
//            removeDirectAssociationChildren(childrenClass);
//            for (DatabaseItem item : items) {
//                addDirectAssociationChild(item);
//            }
//        } finally {
//            disconnect();
//        }
//    }
//
//    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, DatabaseItem... items) {
//        connect();
//        try {
//            removeDirectAssociationChildren(childrenClass);
//            for (DatabaseItem item : items) {
//                addDirectAssociationChild(item);
//            }
//        } finally {
//            disconnect();
//        }
//    }
//
//    protected void addDirectAssociationChild(DatabaseItem item) {
//        // todo use a transaction
//        connect();
//        model.add(item.model);
//        item.updateTimestamp();
//        disconnect();
//    }

    protected <C extends Model> LazyList<C> getReferencedElements(DatabaseMediator.ItemType type, DatabaseMediator.Field field) {
        Object[] ids = getStringList(field).toArray();
        DatabaseMediator.connect(dbPath);
        try {
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            String query = "id = -1";
            for (int i = 0; i < ids.length; i++) {
                query += " OR id = ?";
            }
            return (LazyList<C>) where.invoke(null, query, ids);
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected List<String> getReferencedElementsIds(DatabaseMediator.ItemType type, DatabaseMediator.Field field) {
        return getStringList(field);
    }

    protected void removeReferencedElements(DatabaseMediator.Field field, boolean flush) {
        removeList(field, flush);
    }

    protected void removeReferencedElementsAndDelete(String field, boolean flush) {
        // todo
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

    protected <C extends Model> boolean removeReferencedElementAndDelete(DatabaseMediator.Field field, C model, boolean flush) {
        List<String> stringList = getStringList(field);
        boolean removed = stringList.remove(model.getId().toString());
        setStringList(field, stringList, flush);
        return removed;
        // todo
    }

    protected void setReferencedElements(DatabaseMediator.Field field, List<? extends DatabaseItem> models, boolean flush) {
        List<String> idList = new ArrayList<>();
        for (DatabaseItem item : models) {
            idList.add(item.getId().toString());
        }
        setStringList(field, idList, flush);
    }

    protected void setReferencedElementsIds(DatabaseMediator.Field field, List<String> idList, boolean flush) {
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
        return addStringValue(field, item.getId().toString(), flush);
    }

    protected <C extends Model> LazyList<C> getElementsContainingMe(DatabaseMediator.ItemType type, DatabaseMediator.Field field) {
        Object[] myId = new String[]{"%\n" + getId().toString() + "\n%"};
        DatabaseMediator.connect(dbPath);
        try {
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            String query = field.value + " LIKE ?";
            return (LazyList<C>) where.invoke(null, query, myId);
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        } finally {
            DatabaseMediator.disconnect(dbPath);
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

    protected void setStringList(DatabaseMediator.Field field, List<String> stringList, boolean flush) {
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
            String strList = getString(field);
            List<E> enumValues = new ArrayList<>();
            for (String str : deserializeList(strList)) {
                enumValues.add((E) valueOf.invoke(null, str));
            }
            return enumValues;
        } catch (IllegalAccessException e) {
            // todo
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
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
        } catch (NoSuchMethodException e) {
            // todo
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

    protected String serializeList(List<String> list) {
        if (list.isEmpty()) {
            return "";
        } else {
            StringBuilder serList = new StringBuilder(LIST_SEPARATOR);
            for (String item : list) {
                serList.append(item).append(LIST_SEPARATOR);
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

    public void delete() {
        connect();
        model.delete();
        disconnect();
    }

    protected void connect() {
        DatabaseMediator.connect(dbPath);
    }

    protected void disconnect() {
        DatabaseMediator.disconnect(dbPath);
    }
}
