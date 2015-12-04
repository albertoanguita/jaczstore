package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Company model (table companies)
 */
public class Company extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // delete people association records
            MoviesCompanies.deleteRecords("company_id", getId());
            TVSeriesCompanies.deleteRecords("company_id", getId());
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
