package jacz.store;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class LibraryItem {

    private Model model;

    private int timestamp;

    public LibraryItem() {
        this.model = buildModel();
        updateTimestamp();
        save();
    }

    public LibraryItem(Model model) {
        this.model = model;
    }

    protected abstract Model buildModel();

    public Integer getId() {
        return (Integer) model.getId();
    }

    public String getString(String field) {
        return model.getString(field);
    }

    public Integer getInteger(String field) {
        return model.getInteger(field);
    }

    public <C extends Model> LazyList<C> get(Class<C> clazz, String query, Object... params) {
        return model.get(clazz, query, params);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        // todo
    }

    public void set(String field, Object value) {
        set(field, value, true);
    }

    public void set(String field, Object value, boolean save) {
        model.set(field, value);
        if (save) {
            save();
        }
    }

    protected void save() {
        model.saveIt();
    }

    public void delete() {
        model.deleteCascadeShallow();
    }




}
