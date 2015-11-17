package jacz.store;

import jacz.store.database.DatabaseMediator;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class File extends LibraryItem {

    private String hash;

    private Long length;

    public File(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public File(DatabaseMediator databaseMediator, Integer id, int timestamp, String hash, Long length) {
        super(databaseMediator, id, timestamp);
        this.hash = hash;
        this.length = length;
    }
}
