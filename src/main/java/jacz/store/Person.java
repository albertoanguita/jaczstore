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
        return buildList(dbPath, getModels(dbPath, jacz.store.database.models.Person.class));
    }

    public List<Movie> getMoviesAsActor(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getElementsContainingMe(jacz.store.database.models.Movie.class, "actor_list");
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<Movie> getMoviesAsDirector(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getElementsContainingMe(jacz.store.database.models.Movie.class, "creator_list");
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<TVSeries> getTVSeriesAsActor(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getElementsContainingMe(jacz.store.database.models.TVSeries.class, "actor_list");
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public List<TVSeries> getTVSeriesAsCreator(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getElementsContainingMe(jacz.store.database.models.TVSeries.class, "creator_list");
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public List<Chapter> getChaptersAsActor(String dbPath) {
        List<jacz.store.database.models.Chapter> modelChapters = getElementsContainingMe(jacz.store.database.models.Chapter.class, "actor_list");
        return Chapter.buildList(dbPath, modelChapters);
    }

    public List<Chapter> getChaptersAsDirector(String dbPath) {
        List<jacz.store.database.models.Chapter> modelChapters = getElementsContainingMe(jacz.store.database.models.Chapter.class, "creator_list");
        return Chapter.buildList(dbPath, modelChapters);
    }
}
