package jacz.store.old;

import jacz.store.Filter;
import jacz.store.common.LibraryItem;
import jacz.store.db_mediator.CorruptDataException;
import jacz.store.db_mediator.DBException;

import java.util.*;

/**
 * A set of several item containers, which share a common ancestor class. Queries to objects of this class will span all the containers
 * and retrieve items from the different containers jointly
 */
public class ItemContainerSet<T extends LibraryItem> {

    private final Set<ItemContainer<? extends T>> itemContainers;

    @SafeVarargs
    public ItemContainerSet(ItemContainer<? extends T>... itemContainers) {
        this.itemContainers = new HashSet<>();
        Collections.addAll(this.itemContainers, itemContainers);
    }

    public List<String> getItems(List<String> fields, List<Filter> filters) throws DBException, CorruptDataException {
        List<String> identifiers = new ArrayList<>();
        for (ItemContainer<?> itemContainer : itemContainers) {
            identifiers.addAll(itemContainer.getItems(fields, filters));
        }
        return identifiers;
    }

    public T getItem(String id) {
        T item = null;
        for (ItemContainer<? extends T> itemContainer : itemContainers) {
            item = itemContainer.getItem(id);
            if (item != null) {
                return item;
            }
        }
        return item;
    }
}
