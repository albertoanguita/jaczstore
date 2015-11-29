package jacz.store;

import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Person extends NamedLibraryItem {

    public Person() {
        super();
    }

    Person(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Person();
    }

    static List<Person> buildList(List<? extends Model> models) {
        List<Person> persons = new ArrayList<>();
        for (Model model : models) {
            if (model != null) {
                persons.add(new Person(model));
            }
        }
        return persons;
    }

    static void appendList(List<Person> persons, List<? extends Model> models) {
        for (Model model : models) {
            if (model != null && !contains(persons, model)) {
                persons.add(new Person(model));
            }
        }
    }

    public static List<Person> getPeople() {
        return buildList(getModels(jacz.store.database.models.Person.class));
    }

    public static List<Person> getActors() {
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
        List<Person> actors = buildList(moviesActors);
        appendList(actors, tvSeriesActors);
        appendList(actors, chaptersActors);
        return actors;
    }

    public static List<Person> getDirectors() {
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
        List<Person> actors = buildList(movieDirectors);
        appendList(actors, chaptersDirectors);
        return actors;
    }

    public static List<Person> getCreators() {
        List<Model> creators = jacz.store.database.models.Person.findBySQL(
                "SELECT * FROM people " +
                        "INNER JOIN tv_series_people " +
                        "ON people.id = tv_series_people.person_id " +
                        "WHERE tv_series_people.type = ?",
                jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return buildList(creators);
    }

    public List<Movie> getMoviesAsActor() {
        List<jacz.store.database.models.Movie> modelMovies = getAssociation(jacz.store.database.models.Movie.class, jacz.store.database.models.MoviesPeople.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return Movie.buildList(modelMovies);
    }

    public List<Movie> getMoviesAsDirector() {
        List<jacz.store.database.models.Movie> modelMovies = getAssociation(jacz.store.database.models.Movie.class, jacz.store.database.models.MoviesPeople.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return Movie.buildList(modelMovies);
    }

    public List<TVSeries> getTVSeriesAsActor() {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getAssociation(jacz.store.database.models.TVSeries.class, jacz.store.database.models.TVSeriesPeople.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return TVSeries.buildList(modelTVSeries);
    }

    public List<TVSeries> getTVSeriesAsCreator() {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getAssociation(jacz.store.database.models.TVSeries.class, jacz.store.database.models.TVSeries.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return TVSeries.buildList(modelTVSeries);
    }

    public List<Chapter> getChaptersAsActor() {
        List<jacz.store.database.models.Chapter> modelChapters = getAssociation(jacz.store.database.models.Chapter.class, jacz.store.database.models.ChaptersPeople.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return Chapter.buildList(modelChapters);
    }

    public List<Chapter> getChaptersAsDirector() {
        List<jacz.store.database.models.Chapter> modelChapters = getAssociation(jacz.store.database.models.Chapter.class, jacz.store.database.models.ChaptersPeople.class, "type = ? ", jacz.store.database.DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return Chapter.buildList(modelChapters);
    }
}
