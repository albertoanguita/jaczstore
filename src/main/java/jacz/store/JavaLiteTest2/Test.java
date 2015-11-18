package jacz.store.JavaLiteTest2;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Alberto on 17/11/2015.
 */
public class Test {

    public static void main(String[] args) {

//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        final Logger logger = LoggerFactory.getLogger(Test.class);


        Base.open("org.sqlite.JDBC", "jdbc:sqlite:test3.db", "", "");

//        List<Person> people = Person.findAll();
//        for (Person person : people) {
//            System.out.println("Person " + person.get("id") + ": " + person.get("name"));
//        }
//        List<jacz.store.JavaLiteTest2.models.Movie> movies = jacz.store.JavaLiteTest2.models.Movie.findAll();
//        for (jacz.store.JavaLiteTest2.models.Movie movie : movies) {
//            System.out.println("Movie " + movie.getString("id") + ": " + movie.get("title") + ": " + movie.getInteger("year"));
//        }
//        List<Movie> movies = Movie.findAll();
//        for (Movie movie : movies) {
//            System.out.println("Movie " + movie.getId() + ": " + movie.getTitle() + ": " + movie.getYear());
//        }


        Movie aMovie = Movie.findById(0);
        System.out.println(aMovie.getTitle());
//        Person person = Person.findById(7);
//        Person person = new Person("Arnold");

        Create.openTransaction();
        Movie newMovie = new Movie();
        newMovie.setTitle("Casino");
        newMovie.setYear(2015);
        Create.commitTransaction();

//        person.delete();
//        aMovie.addActor(person);


//        List<Person> actors = aMovie.getActors();
//        for (Person actor : actors) {
//            System.out.println("Actor " + actor.getId() + ": " + actor.getName());
//        }

//        movies = Movie.findAll();
//        for (Movie movie : movies) {
//            System.out.println("Movie " + movie.getId() + ": " + movie.getTitle() + ": " + movie.getYear());
//        }


//        System.out.println("-------------");
//        jacz.store.JavaLiteTest2.models.Movie movie = jacz.store.JavaLiteTest2.models.Movie.findById(7);
//        Integer year = movie.getInteger("year");


//        Person newPerson = new Person("Alice");
//        if (!newPerson.exists()) {
//            newPerson.saveIt(); // will generate ID
//        }
//        MoviesPeople mp = new MoviesPeople();
//        mp.set("type", Person.TYPE.actor.name());
//        mp.set("person_id", newPerson.getId());
//        mp.set("movie_id", movie.getId());
//        mp.saveIt();


//        logger.info("retrieving actors..");
//        List<jacz.store.JavaLiteTest2.models.Person> persons = movie.get(jacz.store.JavaLiteTest2.models.Person.class, "type = ? ", jacz.store.JavaLiteTest2.models.Person.TYPE.actor.name());
//        for (jacz.store.JavaLiteTest2.models.Person person : persons) {
//            System.out.println("Person " + person.get("id") + ": " + person.get("name"));
//        }
//
//        try {
//            Movie newMovie = new Movie("Titanic", 2004);
//            newMovie.addActor();
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
        Create.close();
    }
}
