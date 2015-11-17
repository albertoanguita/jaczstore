package jacz.store.JalaLiteTest;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Alberto on 17/11/2015.
 */
public class TestJavaLite {

    public static void main(String[] args) {

//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        final Logger logger = LoggerFactory.getLogger(TestJavaLite.class);


        Base.open("org.sqlite.JDBC", "jdbc:sqlite:test2.db", "root", "p@ssw0rd");

//        List<Person> people = Person.findAll();
//        for (Person person : people) {
//            System.out.println("Person " + person.get("id") + ": " + person.get("name"));
//        }
        List<Movie> movies = Movie.findAll();
        for (Movie movie : movies) {
            System.out.println("Movie " + movie.getString("id") + ": " + movie.get("title") + ": " + movie.getInteger("year"));
        }



        System.out.println("-------------");
        Movie movie = Movie.findById(7);
        Integer year = movie.getInteger("year");


//        Person newPerson = new Person("Alice");
//        if (!newPerson.exists()) {
//            newPerson.saveIt(); // will generate ID
//        }
//        MoviesPeople mp = new MoviesPeople();
//        mp.set("type", Person.TYPE.actor.name());
//        mp.set("person_id", newPerson.getId());
//        mp.set("movie_id", movie.getId());
//        mp.saveIt();


        logger.info("retrieving actors..");
        List<Person> persons = movie.get(Person.class, "type = ? ", Person.TYPE.actor.name());

        //get all patients of this doctor
//        List<Person> actors = movie.getAll(Person.class);
        for (Person person : persons) {
            System.out.println("Person " + person.get("id") + ": " + person.get("name"));
        }
    }
}
