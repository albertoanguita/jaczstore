package jacz.store;

import jacz.store.database.DatabaseMediator;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Company extends NamedLibraryItem {

    public Company(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public Company(DatabaseMediator databaseMediator, Integer id, int timestamp, String name, List<String> aliases) {
        super(databaseMediator, id, timestamp, name, aliases);
    }

    @Override
    public void save() {
        databaseMediator.saveCompany(this);
    }

    @Override
    public void inflate() {

    }
}
