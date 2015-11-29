package jacz.store;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class LibraryItem {

    private static final String LIST_SEPARATOR = "\n";

    private Model model;

    private int timestamp;

    public LibraryItem() {
        this.model = buildModel();
        updateTimestamp();
        save();
    }

    LibraryItem(Model model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
        this.model = model;
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
        return (Integer) model.getId();
    }

    public String getString(String field) {
        return model.getString(field);
    }

    public Integer getInteger(String field) {
        return model.getInteger(field);
    }

    public Long getLong(String field) {
        return model.getLong(field);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        // todo
    }

    protected void set(String field, Object value) {
        model.set(field, value);
        updateTimestamp();
        save();
    }

//    protected void set(String field, Object value, boolean save) {
//        model.set(field, value);
//        if (save) {
//            save();
//        }
//    }

    protected void save() {
        model.saveIt();
    }

    protected static <C extends Model> List<C> getModels(Class<? extends Model> modelClass) {
        try {
            Method findAll = modelClass.getMethod("findAll");
            List<C> models;
            models = (List<C>) findAll.invoke(null);
            return models;
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        }
    }

    protected static <C extends Model> List<C> getModels(Class<? extends Model> modelClass, String query, Object[] params) {
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
        }
    }

    protected static <C extends Model> List<C> getModels(Class<? extends Model> modelClass, Class<? extends Model> includeModeClass, String query, Object[] params) {
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
        }
    }

    protected static <C extends Model> C getModelById(Class<? extends Model> modelClass, int id) {
        Object[] params = {id};
        List<C> models = getModels(modelClass, "id = ?", params);
        return models.isEmpty() ? null : models.get(0);
    }

    protected <C extends Model> Model getDirectAssociationParent(Class<C> parentClass) {
        return model.parent(parentClass);
    }

    protected void setDirectAssociationParent(LibraryItem item) {
        item.model.add(model);
    }

    protected <C extends Model> void removeDirectAssociationParent(Class<C> parentClass) {
        model.parent(parentClass).delete();
    }

    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass) {
        return model.getAll(childClass);
    }

    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass, String query, Object... params) {
        return model.get(childClass, query, params);
    }

    protected <C extends Model> void removeDirectAssociationChildren(Class<C> childClass) {
        List<C> children = getDirectAssociationChildren(childClass);
        for (C child : children) {
            child.delete();
        }
    }

    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, List<? extends LibraryItem> items) {
        removeDirectAssociationChildren(childrenClass);
        for (LibraryItem item : items) {
            addDirectAssociationChild(item);
        }
    }

    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, LibraryItem... items) {
        removeDirectAssociationChildren(childrenClass);
        for (LibraryItem item : items) {
            addDirectAssociationChild(item);
        }
    }

    protected void addDirectAssociationChild(LibraryItem item) {
        model.add(item.model);
    }

    protected <C extends Model, D extends Model> LazyList<C> getAssociation(Class<C> clazz, Class<D> associationClass) {
        return getAssociation(clazz, associationClass, null);
//        return model.get(clazz, "alive = 1");
    }

    protected <C extends Model, D extends Model> LazyList<C> getAssociation(Class<C> clazz, Class<D> associationClass, String query, Object... params) {
        try {
//            Method getTableName = associationClass.getMethod("getTableName");
//            String tableName = (String) getTableName.invoke(null);
//            query = query != null ? ".alive = 1 AND " + query : ".alive = 1";
//            query = query != null ? "alive = 1 AND " + query : "alive = 1";
            if (query != null) {
                return model.get(clazz, query, params);
            } else {
                return model.getAll(clazz);
            }
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
            return null;
        }
    }

    protected <C extends Model> void removeAssociations(Class<? extends Model> modelClass, String idField, String type) {
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
        }
    }

    protected <C extends Model> void removeAssociation(Class<? extends Model> modelClass, String idField, LibraryItem otherItem, String otherIdField, String type) {
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
        }
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String childIdField, String type, List<? extends LibraryItem> items) {
        removeAssociations(modelClass, idField, type);
        for (LibraryItem item : items) {
            addAssociation(modelClass, idField, childIdField, type, item);
        }
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String childIdField, String type, LibraryItem... items) {
        removeAssociations(modelClass, idField, type);
        for (LibraryItem item : items) {
            addAssociation(modelClass, idField, childIdField, type, item);
        }
    }

    protected <C extends Model> void addAssociation(Class<? extends Model> modelClass, String idField, String childIdField, String type, LibraryItem item) {
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

    protected void removeList(String field) {
        set(field, null);
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

    protected <E> boolean addEnum(String field, Class<E> enum_, E value, String getNameMethod) {
        List<E> values = getEnums(field, enum_);
        boolean notContains = !values.contains(value);
        if (notContains) {
            values.add(value);
        }
        setEnums(field, enum_, values, getNameMethod);
        return notContains;
    }

    protected String serializeList(List<String> list) {
        if (list.isEmpty()) {
            return "";
        } else {
            StringBuilder serList = new StringBuilder(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                serList.append(LIST_SEPARATOR).append(list.get(i));
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
        model.delete();
    }
}
