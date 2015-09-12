package jacz.store.old;

import jacz.store.old2.Filter;
import jacz.store.old2.common.LibraryItem;
import jacz.store.old2.db_mediator.CorruptDataException;
import jacz.store.old2.db_mediator.DBException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A searchable list of generic items, indexed by the items identifiers
 */
public interface SearchableList<T extends LibraryItem> {

    /**
     * Searches for items by giving the desired fields and a set of optional filters
     *
     * @param fields  fields to retrieve
     * @param filters filters to apply to the search
     * @return list of identifiers of the retrieved items. These identifiers can then be used in the getItem() method
     * @throws DBException          the database could not be accessed
     * @throws CorruptDataException the database contains corrupted data
     */
    public List<String> getItems(List<String> fields, List<Filter> filters) throws DBException, CorruptDataException;

    /**
     * Retrieves all items of this container
     *
     * @return a Map.Entry with all items
     */
    public Set<Map.Entry<String, T>> getAllItems();

    /**
     * Retrieves a single item, previously searched with the getItems() method
     *
     * @param id identifier of the item
     * @return the item indexed by the provided identifier, or null if there is not such item
     */
    public T getItem(String id);
}
