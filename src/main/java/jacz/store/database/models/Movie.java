package jacz.store.database.models;

import jacz.store.*;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Movie model (table movies)
 */
public class Movie extends Model {

    @Override
    public void beforeDelete() {
        // delete image
        ImageFile.deleteRecord(this);
        // delete people association records
        MoviesPeople.deleteRecords("movie_id", getId());
        // delete companies association records
        MoviesCompanies.deleteRecords("movie_id", getId());
        // delete video files
        MoviesVideoFiles.deleteRecords("movie_id", getId());
        DeletedItem.addDeletedItem(this, getTableName());
    }

    static void deleteImageLink(Model imageModel) {
        List<Movie> movieModels = imageModel.getAll(Movie.class);
        for (Movie movie : movieModels) {
            movie.set("image_file_id", null);
            movie.saveIt();
        }
    }
}
