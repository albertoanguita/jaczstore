package jacz.database.models;

import jacz.database.*;
import jacz.database.Movie;
import jacz.database.TVSeries;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Company model (table companies)
 */
public class Company extends Model {

    @Override
    public void beforeDelete() {
        jacz.database.Company company = new jacz.database.Company(this, DatabaseMediator.getDBPath());
        List<jacz.database.Movie> movies = company.getMovies(DatabaseMediator.getDBPath());
        for (Movie movie : movies) {
            movie.removeProductionCompany(company);
        }
        List<TVSeries> tvSeries = company.getTVSeries(DatabaseMediator.getDBPath());
        for (TVSeries aTVSeries : tvSeries) {
            aTVSeries.removeProductionCompany(company);
        }
        if (DatabaseMediator.mustAutoComplete()) {
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
