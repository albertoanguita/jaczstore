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
        jacz.database.Person person = new jacz.database.Person(this, DatabaseMediator.getDBPath());
        List<Movie> movies = person.getMoviesAsActor(DatabaseMediator.getDBPath());
        for (Movie movie : movies) {
            movie.removeActor(person);
        }
        movies = person.getMoviesAsDirector(DatabaseMediator.getDBPath());
        for (Movie movie : movies) {
            movie.removeCreator(person);
        }
        List<TVSeries> tvSeries = person.getTVSeriesAsActor(DatabaseMediator.getDBPath());
        for (TVSeries aTVSeries : tvSeries) {
            aTVSeries.removeActor(person);
        }
        tvSeries = person.getTVSeriesAsCreator(DatabaseMediator.getDBPath());
        for (TVSeries aTVSeries : tvSeries) {
            aTVSeries.removeCreator(person);
        }
        List<jacz.database.Chapter> chapters = person.getChaptersAsActor(DatabaseMediator.getDBPath());
        for (Chapter chapter : chapters) {
            chapter.removeActor(person);
        }
        chapters = person.getChaptersAsDirector(DatabaseMediator.getDBPath());
        for (Chapter chapter : chapters) {
            chapter.removeCreator(person);
        }
        if (DatabaseMediator.mustAutoComplete()) {
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
