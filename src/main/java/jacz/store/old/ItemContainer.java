package jacz.store.old;

import jacz.store.Database;
import jacz.store.Filter;
import jacz.store.common.LibraryItem;
import jacz.store.db_mediator.CorruptDataException;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * A generic library of items that can be queried
 */
public abstract class ItemContainer<T extends LibraryItem> implements Serializable {

    /**
     * Name of this item container. A container is associated to a database entity which can receive a query with filters
     */
    private final String containerName;

    /**
     * Items loaded in memory, indexed by their identifier. The user can query these items providing an identifier
     */
    private final Map<String, T> items;

    /**
     * Object for accessing the database
     */
    private final DBMediator dbMediator;

    /**
     * Database parameters
     */
    protected final Database database;

    /**
     * Class constructor
     *
     * @param containerName    name for this container
     * @param database Database
     * @param dbMediator database mediator
     */
    ItemContainer(String containerName, Database database, DBMediator dbMediator) {
        this.containerName = containerName;
        this.items = new HashMap<>();
        this.database = database;
        this.dbMediator = dbMediator;
//        if (database.isLoadInMemory()) {
//            loadContainer();
//        }
    }

    public String getContainerName() {
        return containerName;
    }

    void addItem(LibraryItem item) {
        //noinspection unchecked
        items.put(item.getIdentifier(), (T) item);
    }

    /**
     * Defines the list of fields of the items of this container
     *
     * @return a list of String with the names of the fields of the items
     */
    protected abstract List<String> defineFields();

    /**
     * Builds a new item for this container with a set of values extracted from the database
     *
     * @param record fields and values
     * @return a new item
     * @throws ParseException
     */
    protected abstract T loadItem(Map<String, String> record) throws ParseException;

    /**
     * Builds a new item for this container
     *
     * @return a new item with random identifier and empty fields
     * @throws ParseException
     */
    public abstract T buildNewItem() throws ParseException, DBException, IOException;

//    private void loadContainer() {
//        try {
//            List<Map<String, String>> records = dbMediator.select(containerName, defineFields(), new ArrayList<Filter>());
//            for (Map<String, String> record : records) {
//                items.put(record.get(LibraryItem.IDENTIFIER), loadItem(record));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("error loading database: " + containerName);
//        }
//    }

    public List<String> getItems(List<String> fields, List<Filter> filters) throws DBException, CorruptDataException {
//        if (database.isLoadInMemory()) {
//            // we query the database to take advantage of its filter system
//            // select the identifiers only, since the objects are already in memory
//            List<String> onlyIdField = new ArrayList<>();
//            onlyIdField.add(LibraryItem.IDENTIFIER);
//            List<Map<String, String>> ids = dbMediator.select(containerName, onlyIdField, filters);
//            List<String> result = new ArrayList<>(ids.size());
//            for (Map<String, String> id : ids) {
//                result.add(id.get(LibraryItem.IDENTIFIER));
//            }
//            return result;
//        } else {
            // clear previous items (data is not stored in memory, so we erase the elements from memory when we can -> now)
            // we put the new items in memory for the user to retrieve them with the getItem() method later
//            items.clear();
//            if (!fields.contains(LibraryItem.IDENTIFIER)) {
//                fields.add(0, LibraryItem.IDENTIFIER);
//            }
//            List<Map<String, String>> values = dbMediator.select(containerName, fields, filters);
//            List<String> result = new ArrayList<>();
//            for (Map<String, String> record : values) {
//                try {
//                    items.put(record.get(LibraryItem.IDENTIFIER), loadItem(record));
//                    result.add(record.get(LibraryItem.IDENTIFIER));
//                } catch (ParseException e) {
//                    throw new CorruptDataException(e.getMessage());
//                }
//            }
//            return result;
//        }
        return null;
    }

    /**
     * Returns an iterator over all items in memory
     *
     * @return an iterator over all items in memory
     */
    public List<String> getAllItems() {
        return new ArrayList<>(items.keySet());
    }

    public T getItem(String id) {
        return items.get(id);
    }

    public void removeItem(String id) throws DBException, IOException {
        items.remove(id);
        database.removeItem(containerName, id);
    }
}
