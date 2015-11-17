package jacz.store;

import jacz.store.database.DatabaseMediator;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class LibraryItem {

    protected final DatabaseMediator databaseMediator;

    protected boolean isInflated;

    private Integer id;

    private int timestamp;

    /**
     * New item, not in the database
     */
    public LibraryItem(DatabaseMediator databaseMediator) {
        this.databaseMediator = databaseMediator;
        isInflated = false;
        id = null;
        updateTimestamp();
    }

    /**
     * Item recovered from the database
     *
     * @param id        item id
     * @param timestamp timestamp of last item update
     */
    public LibraryItem(DatabaseMediator databaseMediator, Integer id, int timestamp) {
        this.databaseMediator = databaseMediator;
        isInflated = false;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        // todo
    }

    /**
     * Save the item data into the database
     */
    public abstract void save();

    /**
     * Inflates objects related to this object with data from the database
     */
    public abstract void inflate();
}
