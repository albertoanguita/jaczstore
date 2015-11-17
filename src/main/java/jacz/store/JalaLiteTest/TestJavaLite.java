package jacz.store.JalaLiteTest;

import org.javalite.activejdbc.Base;

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

        Base.open("org.sqlite.JDBC", "jdbc:sqlite:test.db", "root", "p@ssw0rd");

//        List<Person> people = Person.findAll();
//        for (Person person : people) {
//            System.out.println("Person " + person.get("id") + ": " + person.get("name"));
//        }
//        List<Movie> movies = Movie.findAll();
//        for (Movie movie : movies) {
//            System.out.println("Movie " + movie.get("id") + ": " + movie.get("title") + ": " + movie.get("year"));
//        }


        System.out.println("-------------");
        Movie movie = Movie.findById(1);
        //get all patients of this doctor
        List<Person> actors = movie.getAll(Person.class);
        for (Person person : actors) {
            System.out.println("Person " + person.get("id") + ": " + person.get("name"));
        }
    }
}
