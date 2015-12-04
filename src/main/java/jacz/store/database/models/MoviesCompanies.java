package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Movies to Companies associative relation
 */
@Table("movies_companies")
public class MoviesCompanies extends Model {

    @Override
    protected void afterCreate() {
        if (DatabaseMediator.mustAutoComplete()) {
            super.afterCreate();
            set("timestamp", DatabaseMediator.getNewTimestamp()).saveIt();
        }
    }

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }

    static void deleteRecords(String field, Object id) {
        List<MoviesCompanies> moviesCompaniesModels = where(field + " = ?", id);
        for (MoviesCompanies moviesCompanies : moviesCompaniesModels) {
            moviesCompanies.delete();
        }
    }
}
