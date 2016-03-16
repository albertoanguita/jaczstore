package jacz.database;

import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Person extends NamedItem {

    Person(String dbPath) {
        super(dbPath);
    }

    public Person(String dbPath, String name) {
        super(dbPath, name);
    }

    public Person(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public Person(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    public DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.PERSON;
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

    public static Person getPersonById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.PERSON, id);
        return model != null ? new Person(model, dbPath) : null;
    }

    public static List<Person> getPeopleFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
    }

    public List<Movie> getMoviesAsActor(String dbPath) {
        List<jacz.database.models.Movie> modelMovies = getElementsContainingMe(DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.ACTOR_LIST);
        if (modelMovies != null) {
            return Movie.buildList(dbPath, modelMovies);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Movie> getMoviesAsDirector(String dbPath) {
        List<jacz.database.models.Movie> modelMovies = getElementsContainingMe(DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.CREATOR_LIST);
        if (modelMovies != null) {
            return Movie.buildList(dbPath, modelMovies);
        } else {
            return new ArrayList<>();
        }
    }

    public List<TVSeries> getTVSeriesAsActor(String dbPath) {
        List<jacz.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.ACTOR_LIST);
        if (modelTVSeries != null) {
            return TVSeries.buildList(dbPath, modelTVSeries);
        } else {
            return new ArrayList<>();
        }
    }

    public List<TVSeries> getTVSeriesAsCreator(String dbPath) {
        List<jacz.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.CREATOR_LIST);
        if (modelTVSeries != null) {
            return TVSeries.buildList(dbPath, modelTVSeries);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Chapter> getChaptersAsActor(String dbPath) {
        List<jacz.database.models.Chapter> modelChapters = getElementsContainingMe(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.ACTOR_LIST);
        if (modelChapters != null) {
            return Chapter.buildList(dbPath, modelChapters);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Chapter> getChaptersAsDirector(String dbPath) {
        List<jacz.database.models.Chapter> modelChapters = getElementsContainingMe(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.CREATOR_LIST);
        if (modelChapters != null) {
            return Chapter.buildList(dbPath, modelChapters);
        } else {
            return new ArrayList<>();
        }
    }
}
