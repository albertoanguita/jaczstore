package jacz.store.database.models;

import jacz.store.*;
import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Person model (table people)
 */
public class Person extends Model {

    @Override
    public void beforeDelete() {
        jacz.store.Person person = new jacz.store.Person(this, DatabaseMediator.getDBPath());
        List<jacz.store.Movie> movies = person.getMoviesAsActor(DatabaseMediator.getDBPath());
        for (jacz.store.Movie movie : movies) {
            movie.removeActor(person);
        }
        movies = person.getMoviesAsDirector(DatabaseMediator.getDBPath());
        for (jacz.store.Movie movie : movies) {
            movie.removeDirector(person);
        }
        List<jacz.store.TVSeries> tvSeries = person.getTVSeriesAsActor(DatabaseMediator.getDBPath());
        for (jacz.store.TVSeries aTVSeries : tvSeries) {
            aTVSeries.removeActor(person);
        }
        tvSeries = person.getTVSeriesAsCreator(DatabaseMediator.getDBPath());
        for (jacz.store.TVSeries aTVSeries : tvSeries) {
            aTVSeries.removeCreator(person);
        }
        List<jacz.store.Chapter> chapters = person.getChaptersAsActor(DatabaseMediator.getDBPath());
        for (jacz.store.Chapter chapter : chapters) {
            chapter.removeActor(person);
        }
        chapters = person.getChaptersAsDirector(DatabaseMediator.getDBPath());
        for (jacz.store.Chapter chapter : chapters) {
            chapter.removeDirector(person);
        }
        if (DatabaseMediator.mustAutoComplete()) {
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
