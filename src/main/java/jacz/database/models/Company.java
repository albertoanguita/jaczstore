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
        String dbPath = DatabaseMediator.getDBPath();
        if (dbPath != null) {
            jacz.database.Company company = new jacz.database.Company(this, dbPath);
            List<jacz.database.Movie> movies = company.getMovies(dbPath);
            for (Movie movie : movies) {
                movie.removeProductionCompany(company);
            }
            List<TVSeries> tvSeries = company.getTVSeries(dbPath);
            for (TVSeries aTVSeries : tvSeries) {
                aTVSeries.removeProductionCompany(company);
            }
        }
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
