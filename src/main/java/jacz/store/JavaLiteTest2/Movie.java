package jacz.store.JavaLiteTest2;

import jacz.store.JavaLiteTest2.models.MoviesPeople;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 17/11/2015.
 */
public class Movie extends CreationItem {

    public Movie() {
        super();
    }

    private Movie(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return null;
    }

    public Integer getYear() {
        return getInteger("year");
    }

    public void setYear(Integer year) {
        set("year", year);
    }

//    public static List<Movie> findAll() {
//        List<Movie> movies = new ArrayList<>();
//        for (Model model : jacz.store.JavaLiteTest2.models.Movie.findAll()) {
//            movies.add(new Movie(model));
//        }
//        return movies;
//    }
//
//    public static Movie findById(Object id) {
//        return new Movie(jacz.store.JavaLiteTest2.models.Movie.findById(id));
//    }
//
//    public List<Person> getActors() {
//        List<Person> persons = new ArrayList<>();
//        List<jacz.store.JavaLiteTest2.models.Person> modelPersons = get(jacz.store.JavaLiteTest2.models.Person.class, "type = ? ", jacz.store.JavaLiteTest2.models.Person.TYPE.actor.name());
//        return Person.buildList(modelPersons);
//    }
//
//    public void addActor(Person person) {
//        MoviesPeople mp = new MoviesPeople();
//        mp.set("type", jacz.store.JavaLiteTest2.models.Person.TYPE.actor.name());
//        mp.set("movie_id", getId());
//        mp.set("person_id", person.getId());
//        mp.saveIt();
//    }
}
