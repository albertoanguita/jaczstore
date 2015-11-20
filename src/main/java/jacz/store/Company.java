package jacz.store;

import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Company extends NamedLibraryItem {

    public Company() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Company();
    }

}
