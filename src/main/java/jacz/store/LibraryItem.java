package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Generic library item
 */
public abstract class LibraryItem {

    private static final String LIST_SEPARATOR = "\n";

    private Model model;

    protected final String dbPath;

    public LibraryItem(String dbPath) {
        this.dbPath = dbPath;
        connect();
        this.model = buildModel();
        updateTimestamp();
        save();
        disconnect();
    }

    LibraryItem(Model model, String dbPath) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
        this.model = model;
        this.dbPath = dbPath;
    }

    protected abstract Model buildModel();

    static boolean contains(Collection<? extends LibraryItem> items, Model model) {
        for (LibraryItem item : items) {
            if (item.getId().equals(model.getInteger("id"))) {
                return true;
            }
        }
        return false;
    }

    public Integer getId() {
        updateLastAccessTime();
        return (Integer) model.getId();
    }

    public String getString(String field) {
        updateLastAccessTime();
        return model.getString(field);
    }

    public Integer getInteger(String field) {
        updateLastAccessTime();
        return model.getInteger(field);
    }

    public Long getLong(String field) {
        updateLastAccessTime();
        return model.getLong(field);
    }

    public void updateLastAccessTime() {
        DatabaseMediator.updateLastAccessTime(dbPath);
    }


    public long getTimestamp() {
        return getLong("timestamp");
    }

    public void updateTimestamp() {
        connect();
        model.set("timestamp", DatabaseMediator.getNewTimestamp(dbPath));
        disconnect();
    }

    protected void set(String field, Object value) {
        connect();
        model.set(field, value);
        DatabaseMediator.updateLastUpdateTime(dbPath);
        updateTimestamp();
        save();
        disconnect();
    }

    public float match(LibraryItem anotherItem) {
        // no fields of this class indicate equality
        return 0f;
    }

    // todo make abstract
    public void merge(LibraryItem anotherItem) {
    }

    protected void save() {
        connect();
        model.saveIt();
        disconnect();
    }

    protected static <C extends Model> List<C> getModels(String dbPath, Class<? extends Model> modelClass) {
        DatabaseMediator.connect(dbPath);
        try {
            Method findAll = modelClass.getMethod("findAll");
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

    protected static <C extends Model> List<C> getModels(String dbPath, Class<? extends Model> modelClass, String query, Object[] params) {
        DatabaseMediator.connect(dbPath);
        try {
            Method where = modelClass.getMethod("where", String.class, Object[].class);
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

    protected static <C extends Model> C getModelById(String dbPath, Class<? extends Model> modelClass, int id) {
        DatabaseMediator.connect(dbPath);
        try {
            Object[] params = {id};
            List<C> models = getModels(dbPath, modelClass, "id = ?", params);
            return models.isEmpty() ? null : models.get(0);
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected <C extends Model> Model getDirectAssociationParent(String dbPath, Class<C> parentClass) {
        connect();
        try {
            return model.parent(parentClass);
        } finally {
            disconnect();
        }
    }

    protected void setDirectAssociationParent(LibraryItem item) {
        connect();
        item.model.add(model);
        save();
        disconnect();
    }

    protected <C extends Model> void removeDirectAssociationParent(Class<C> parentClass) {
        connect();
        model.parent(parentClass).delete();
        save();
        disconnect();
    }

    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass) {
        connect();
        try {
            return model.getAll(childClass);
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass, String query, Object... params) {
        connect();
        try {
            return model.get(childClass, query, params);
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void removeDirectAssociationChildren(Class<C> childClass) {
        connect();
        try {
            List<C> children = getDirectAssociationChildren(childClass);
            for (C child : children) {
                child.delete();
            }
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, List<? extends LibraryItem> items) {
        connect();
        try {
            removeDirectAssociationChildren(childrenClass);
            for (LibraryItem item : items) {
                addDirectAssociationChild(item);
            }
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, LibraryItem... items) {
        connect();
        try {
            removeDirectAssociationChildren(childrenClass);
            for (LibraryItem item : items) {
                addDirectAssociationChild(item);
            }
        } finally {
            disconnect();
        }
    }

    protected void addDirectAssociationChild(LibraryItem item) {
        connect();
        model.add(item.model);
        disconnect();
    }

    protected <C extends Model> LazyList<C> getAssociation(Class<C> clazz) {
        connect();
        try {
            return getAssociation(clazz, null);
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> LazyList<C> getAssociation(Class<C> clazz, String query, Object... params) {
        connect();
        try {
            if (query != null) {
                return model.get(clazz, query, params);
            } else {
                return model.getAll(clazz);
            }
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void removeAssociations(Class<? extends Model> modelClass, String idField, String type) {
        connect();
        try {
            Method where = modelClass.getMethod("where", String.class, Object[].class);
            List<C> associations;
            if (type != null) {
                associations = (List<C>) where.invoke(null, idField + " = '" + getId() + "' AND type = '" + type + "'", new Object[0]);
            } else {
                associations = (List<C>) where.invoke(null, idField + " = '" + getId() + "'", new Object[0]);
            }
            for (C association : associations) {
                association.delete();
            }
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void removeAssociation(Class<? extends Model> modelClass, String idField, LibraryItem otherItem, String otherIdField, String type) {
        connect();
        try {
            Method findFirst = modelClass.getMethod("findFirst", String.class, Object[].class);
            C association;
            if (type != null) {
                association = (C) findFirst.invoke(null, idField + " = '" + getId() + "' AND " + otherIdField + " = '" + otherItem.getId() + "' AND type = '" + type + "'", new Object[0]);
            } else {
                association = (C) findFirst.invoke(null, idField + " = '" + getId() + "' AND " + otherIdField + " = '" + otherItem.getId() + "'", new Object[0]);
            }
            association.delete();
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String childIdField, String type, List<? extends LibraryItem> items) {
        connect();
        try {
            removeAssociations(modelClass, idField, type);
            for (LibraryItem item : items) {
                addAssociation(modelClass, idField, childIdField, type, item);
            }
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String childIdField, String type, LibraryItem... items) {
        connect();
        try {
            removeAssociations(modelClass, idField, type);
            for (LibraryItem item : items) {
                addAssociation(modelClass, idField, childIdField, type, item);
            }
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> void addAssociation(Class<? extends Model> modelClass, String idField, String childIdField, String type, LibraryItem item) {
        connect();
        try {
            Model associativeModel = modelClass.newInstance();
            associativeModel.set(idField, getId());
            associativeModel.set(childIdField, item.getId());
            if (type != null) {
                associativeModel.set("type", type);
            }
            associativeModel.saveIt();
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    protected <C extends Model> LazyList<C> getReferencedElements(Class<? extends Model> modelClass, String field) {
        Object[] ids = getStringList(field).toArray();
        DatabaseMediator.connect(dbPath);
        try {
            Method where = modelClass.getMethod("where", String.class, Object[].class);
            String query = "id = -1";
            for (int i = 0; i < ids.length; i++) {
                query += " OR id = ?";
            }
            return  (LazyList<C>) where.invoke(null, query, ids);
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    protected void removeReferencedElements(String field) {
        removeList(field);
    }

    protected void removeReferencedElementsAndDelete(String field) {
        // todo
    }

    protected <C extends Model> boolean removeReferencedElement(String field, LibraryItem item) {
        List<String> stringList = getStringList(field);
        boolean removed = stringList.remove(item.getId().toString());
        setStringList(field, stringList);
        return removed;
    }

    protected <C extends Model> boolean removeReferencedElementAndDelete(String field, C model) {
        List<String> stringList = getStringList(field);
        boolean removed = stringList.remove(model.getId());
        setStringList(field, stringList);
        return removed;
        // todo
    }

    protected void setReferencedElements(String field, List<? extends LibraryItem> models) {
        List<String> idList = new ArrayList<>();
        for (LibraryItem item : models) {
            idList.add(item.getId().toString());
        }
        setStringList(field, idList);
    }

    protected void setReferencedElements(String field, LibraryItem... models) {
        List<String> idList = new ArrayList<>();
        for (LibraryItem item : models) {
            idList.add(item.getId().toString());
        }
        setStringList(field, idList);
    }

    protected <C extends Model> boolean addReferencedElement(String field, LibraryItem item) {
        return addStringValue(field, item.getId().toString());
    }

    protected <C extends Model> LazyList<C> getElementsContainingMe(Class<? extends Model> modelClass, String field) {
        Object[] myId = new String[]{"%\n" + getId().toString() + "\n%"};
        DatabaseMediator.connect(dbPath);
        try {
            Method where = modelClass.getMethod("where", String.class, Object[].class);
            String query = field + " LIKE ?";
            return  (LazyList<C>) where.invoke(null, query, myId);
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }



    protected List<String> getStringList(String field) {
        return deserializeList(getString(field));
    }

    protected void removeStringList(String field) {
        removeList(field);
    }

    protected boolean removeStringValue(String field, String value) {
        List<String> stringList = getStringList(field);
        boolean removed = stringList.remove(value);
        setStringList(field, stringList);
        return removed;
    }

    protected void setStringList(String field, List<String> stringList) {
        set(field, serializeList(stringList));
    }

    protected boolean addStringValue(String field, String value) {
        List<String> stringList = getStringList(field);
        boolean notContains = !stringList.contains(value);
        if (notContains) {
            stringList.add(value);
        }
        setStringList(field, stringList);
        return notContains;
    }

    protected <E> List<E> getEnums(String field, Class<E> enum_) {
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

    protected <E> boolean removeEnum(String field, Class<E> enum_, E value, String getNameMethod) {
        List<E> enums = getEnums(field, enum_);
        boolean removed = enums.remove(value);
        setEnums(field, enum_, enums, getNameMethod);
        return removed;
    }

    protected <E> void setEnums(String field, Class<E> enum_, List<E> values, String getNameMethod) {
        try {
            Method getName = enum_.getMethod(getNameMethod);
            List<String> strList = new ArrayList<>();
            for (E value : values) {
                strList.add((String) getName.invoke(value));
            }
            set(field, serializeList(strList));
        } catch (NoSuchMethodException e) {
            // todo
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected <E> boolean addEnums(String field, Class<E> enum_, List<E> newValues, String getNameMethod) {
        List<E> values = getEnums(field, enum_);
        boolean modified = false;
        for (E newValue : newValues) {
            boolean notContains = !values.contains(newValue);
            if (notContains) {
                values.add(newValue);
                modified = true;
            }
        }
        setEnums(field, enum_, values, getNameMethod);
        return modified;
    }

    protected <E> boolean addEnum(String field, Class<E> enum_, E value, String getNameMethod) {
        List<E> values = getEnums(field, enum_);
        boolean notContains = !values.contains(value);
        if (notContains) {
            values.add(value);
        }
        setEnums(field, enum_, values, getNameMethod);
        return notContains;
    }

    protected void removeList(String field) {
        set(field, null);
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
