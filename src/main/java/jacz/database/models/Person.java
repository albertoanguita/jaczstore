package jacz.database.models;

import jacz.database.*;
import jacz.database.Chapter;
import jacz.database.Movie;
import jacz.database.TVSeries;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Person model (table people)
 */
public class Person extends Model {

    @Override
    public void beforeDelete() {
        String dbPath = DatabaseMediator.getDBPath();
        if (dbPath != null) {
            jacz.database.Person person = new jacz.database.Person(this, dbPath);
            List<Movie> movies = person.getMoviesAsActor(dbPath);
            for (Movie movie : movies) {
                movie.removeActor(person);
            }
            movies = person.getMoviesAsDirector(dbPath);
            for (Movie movie : movies) {
                movie.removeCreator(person);
            }
            List<TVSeries> tvSeries = person.getTVSeriesAsActor(dbPath);
            for (TVSeries aTVSeries : tvSeries) {
                aTVSeries.removeActor(person);
            }
            tvSeries = person.getTVSeriesAsCreator(dbPath);
            for (TVSeries aTVSeries : tvSeries) {
                aTVSeries.removeCreator(person);
            }
            List<jacz.database.Chapter> chapters = person.getChaptersAsActor(dbPath);
            for (Chapter chapter : chapters) {
                chapter.removeActor(person);
            }
            chapters = person.getChaptersAsDirector(dbPath);
            for (Chapter chapter : chapters) {
                chapter.removeCreator(person);
            }
        }
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
