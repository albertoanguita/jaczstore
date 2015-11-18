package jacz.store;

import jacz.store.database_old.DatabaseMediator;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class NamedLibraryItem extends LibraryItem {

    private String name;

    private List<String> aliases;

    public NamedLibraryItem(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public NamedLibraryItem(
            DatabaseMediator databaseMediator,
            Integer id,
            int timestamp,
            String name,
            List<String> aliases) {
        super(databaseMediator, id, timestamp);
        this.name = name;
        this.aliases = aliases;
    }
}
