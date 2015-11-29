package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Movies to Companies associative relation
 */
@Table("movies_companies")
public class MoviesCompanies extends Model {

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }

    static void deleteRecords(String field, Object id) {
        List<MoviesCompanies> moviesCompaniesModels = where(field + " = ?", id);
        for (MoviesCompanies moviesCompanies : moviesCompaniesModels) {
            moviesCompanies.delete();
        }
    }
}
