package jacz.store;

import jacz.store.common.LibraryItem;

import java.util.List;

/**
 * The result of a search over a database
 */
public class ResultSet {

    private final List<? extends LibraryItem> items;

    private int cursor;

    public ResultSet(List<? extends LibraryItem> items) {
        this.items = items;
        cursor = -1;
    }

    public boolean hasNext() {
        return cursor + 1 < items.size();
    }

    public LibraryItem next() {
        cursor++;
        return items.get(cursor);
    }
}
