package jacz.store.database.models;

import jacz.store.*;
import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Company model (table companies)
 */
public class Company extends Model {

    @Override
    public void beforeDelete() {
        jacz.store.Company company = new jacz.store.Company(this, DatabaseMediator.getDBPath());
        List<jacz.store.Movie> movies = company.getMovies(DatabaseMediator.getDBPath());
        for (jacz.store.Movie movie : movies) {
            movie.removeProductionCompany(company);
        }
        List<jacz.store.TVSeries> tvSeries = company.getTVSeries(DatabaseMediator.getDBPath());
        for (jacz.store.TVSeries aTVSeries : tvSeries) {
            aTVSeries.removeProductionCompany(company);
        }
        if (DatabaseMediator.mustAutoComplete()) {
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
