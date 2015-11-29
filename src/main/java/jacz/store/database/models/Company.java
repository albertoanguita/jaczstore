package jacz.store.database.models;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Company model (table companies)
 */
public class Company extends Model {

    @Override
    public void beforeDelete() {
        // delete people association records
        MoviesCompanies.deleteRecords("company_id", getId());
        TVSeriesCompanies.deleteRecords("company_id", getId());
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
