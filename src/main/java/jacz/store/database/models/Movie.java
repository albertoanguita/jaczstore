package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Movie model (table movies)
 */
public class Movie extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // delete people association records
            MoviesPeople.deleteRecords("movie_id", getId());
            // delete companies association records
            MoviesCompanies.deleteRecords("movie_id", getId());
            // delete video files
            MoviesVideoFiles.deleteRecords("movie_id", getId());
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
