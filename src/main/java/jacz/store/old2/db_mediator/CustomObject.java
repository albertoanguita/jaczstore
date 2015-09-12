package jacz.store.old2.db_mediator;

import java.util.List;

/**
 * A custom object that can be stored in the database (isolated or in lists)
 */
public interface CustomObject<T> {

    public T loadObject(String value);

    public String saveObject(T object);

    public List<T> loadObjectList(String value);

    public String saveObjectList(List<T> objects);
}
