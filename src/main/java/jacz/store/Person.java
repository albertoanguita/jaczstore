package jacz.store;

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
        connect(dbPath);
        try {
            List<Person> persons = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    persons.add(new Person(model, dbPath));
                }
            }
            return persons;
        } finally {
            disconnect(dbPath);
        }
    }

    static void appendList(String dbPath, List<Person> persons, List<? extends Model> models) {
        connect(dbPath);
        try {
            for (Model model : models) {
                if (model != null && !contains(persons, model)) {
                    persons.add(new Person(model, dbPath));
                }
            }
        } finally {
            disconnect(dbPath);
        }
    }

    public static List<Person> getPeople(String dbPath) {
        return buildList(dbPath, getModels(dbPath, jacz.store.database.models.Person.class));
    }

    public static List<Person> getActors(String dbPath) {
        List<Model> moviesActors = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN movies_people " +
                        "ON people.id = movies_people.person_id " +
                        "WHERE movies_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        List<Model> tvSeriesActors = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN tv_series_people " +
                        "ON people.id = tv_series_people.person_id " +
                        "WHERE tv_series_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        List<Model> chaptersActors = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN chapters_people " +
                        "ON people.id = chapters_people.person_id " +
                        "WHERE chapters_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        List<Person> actors = buildList(dbPath, moviesActors);
        appendList(dbPath, actors, tvSeriesActors);
        appendList(dbPath, actors, chaptersActors);
        return actors;
    }

    public static List<Person> getDirectors(String dbPath) {
        List<Model> movieDirectors = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN movies_people " +
                        "ON people.id = movies_people.person_id " +
                        "WHERE movies_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        List<Model> chaptersDirectors = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN chapters_people " +
                        "ON people.id = chapters_people.person_id " +
                        "WHERE chapters_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        List<Person> actors = buildList(dbPath, movieDirectors);
        appendList(dbPath, actors, chaptersDirectors);
        return actors;
    }

    public static List<Person> getCreators(String dbPath) {
        List<Model> creators = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN tv_series_people " +
                        "ON people.id = tv_series_people.person_id " +
                        "WHERE tv_series_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return buildList(dbPath, creators);
    }

    public List<Movie> getMoviesAsActor(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getAssociation(jacz.store.database.models.Movie.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<Movie> getMoviesAsDirector(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getAssociation(jacz.store.database.models.Movie.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<TVSeries> getTVSeriesAsActor(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getAssociation(jacz.store.database.models.TVSeries.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public List<TVSeries> getTVSeriesAsCreator(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getAssociation(jacz.store.database.models.TVSeries.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public List<Chapter> getChaptersAsActor(String dbPath) {
        List<jacz.store.database.models.Chapter> modelChapters = getAssociation(jacz.store.database.models.Chapter.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return Chapter.buildList(dbPath, modelChapters);
    }

    public List<Chapter> getChaptersAsDirector(String dbPath) {
        List<jacz.store.database.models.Chapter> modelChapters = getAssociation(jacz.store.database.models.Chapter.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return Chapter.buildList(dbPath, modelChapters);
    }
}
