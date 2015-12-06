package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Person extends NamedLibraryItem {

    public Person(String dbPath) {
        super(dbPath);
    }

    Person(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Person();
    }

    static List<Person> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<Person> persons = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    persons.add(new Person(model, dbPath));
                }
            }
            return persons;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    static void appendList(String dbPath, List<Person> persons, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            for (Model model : models) {
                if (model != null && !contains(persons, model)) {
                    persons.add(new Person(model, dbPath));
                }
            }
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public static List<Person> getPeople(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.PERSON));
    }

    public List<Movie> getMoviesAsActor(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getElementsContainingMe(DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.ACTOR_LIST);
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<Movie> getMoviesAsDirector(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getElementsContainingMe(DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.CREATOR_LIST);
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<TVSeries> getTVSeriesAsActor(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.ACTOR_LIST);
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public List<TVSeries> getTVSeriesAsCreator(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.CREATOR_LIST);
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public List<Chapter> getChaptersAsActor(String dbPath) {
        List<jacz.store.database.models.Chapter> modelChapters = getElementsContainingMe(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.ACTOR_LIST);
        return Chapter.buildList(dbPath, modelChapters);
    }

    public List<Chapter> getChaptersAsDirector(String dbPath) {
        List<jacz.store.database.models.Chapter> modelChapters = getElementsContainingMe(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.CREATOR_LIST);
        return Chapter.buildList(dbPath, modelChapters);
    }
}
