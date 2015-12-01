package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Alberto on 30/11/2015.
 */
public class ParallelTest {

    public static void main(String[] args) {

        DatabaseMediator.dropAndCreate("parallel1.db", "v1", "aaa");
        DatabaseMediator.dropAndCreate("parallel2.db", "v1", "aaa");

        final Logger logger = LoggerFactory.getLogger(ParallelTest.class);
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:parallel1.db", "", "");

        Movie movie1 = new Movie();
        movie1.setTitle("Predator");
        System.out.println(movie1.getTitle());
        Movie movie11 = Movie.getMovieById(1);

        Base.close();

        Base.open("org.sqlite.JDBC", "jdbc:sqlite:parallel2.db", "", "");

        Movie movie2 = new Movie();
        movie2.setTitle("Alien");
        System.out.println(movie2.getTitle());
//        List<jacz.store.database.models.Movie> movies = jacz.store.database.models.Movie.findAll();

        Base.close();

        Base.open("org.sqlite.JDBC", "jdbc:sqlite:parallel1.db", "", "");
        System.out.println(movie11.getTitle());
//        Base.close();
//        Base.open("org.sqlite.JDBC", "jdbc:sqlite:parallel2.db", "", "");
//        Movie movie21 = new Movie(movies.get(0));
        System.out.println(movie2.getTitle());
        movie2.setTitle("Alien 2");
        Base.close();

        Base.open("org.sqlite.JDBC", "jdbc:sqlite:parallel1.db", "", "");
        movie1 = Movie.getMovieById(1);
        System.out.println(movie1.getTitle());
        Base.close();
//        System.out.println(movie2.getTitle());
//        ParallelTaskExecutor.executeTask(new ParallelTask() {
//            @Override
//            public void performTask() {
//                Base.open("org.sqlite.JDBC", "jdbc:sqlite:test2.db", "", "");
//                Base.openTransaction();
//
//                ThreadUtil.safeSleep(5000);
//                Movie newMovie = new Movie();
//                newMovie.setTitle("Casino");
//                newMovie.setYear(2015);
//
//                Base.commitTransaction();
//                Base.close();
//            }
//        });
//        ThreadUtil.safeSleep(1000);
//        ParallelTaskExecutor.executeTask(new ParallelTask() {
//            @Override
//            public void performTask() {
//                Base.open("org.sqlite.JDBC", "jdbc:sqlite:test3.db", "", "");
//                Base.openTransaction();
//
//                ThreadUtil.safeSleep(5000);
//                Movie newMovie = new Movie();
//                newMovie.setTitle("Casino3");
//                newMovie.setYear(2013);
//
//                Base.commitTransaction();
//                Base.close();
//            }
//        });

//        Base.open("org.sqlite.JDBC", "jdbc:sqlite:test3.db", "", "");
//        DB db = new DB("db");
//        db.open("org.sqlite.JDBC", "jdbc:sqlite:test3.db", "", "");

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





//        Movie aMovie = Movie.findById(0);
//        System.out.println(aMovie.getTitle());
//        Person person = Person.findById(7);
//        Person person = new Person("Arnold");

//        db.openTransaction();
//        Movie newMovie = new Movie();
//        newMovie.setTitle("Casino");
//        newMovie.setYear(2015);
//        db.commitTransaction();

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
//        db.close();
    }
}
