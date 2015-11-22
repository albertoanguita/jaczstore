package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Base;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alberto on 20/11/2015.
 */
public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseMediator.dropAndCreate();
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:store.db", "", "");

        Person arnold = new Person();
        arnold.setName("Arnold");
        Person silvester = new Person();
        silvester.setName("Silvester");
        Person christian = new Person();
        christian.setName("Christian");
        Person george = new Person();
        george.setName("George");

        Movie movie = new Movie();
        movie.setTitle("Predator");

        movie.addDirector(arnold);
        movie.addDirector(silvester);
        movie.addActor(christian);
        movie.addActor(george);

        List<Person> directors = movie.getCreatorsDirectors();
        for (Person person : directors) {
            System.out.println(person.getName());
        }
        List<Person> actors = movie.getActors();
        for (Person person : actors) {
            System.out.println(person.getName());
        }

        movie.removeDirector(arnold);
        movie.removeActor(christian);

        movie.removeCreatorsDirectors();
        movie.removeActors();
    }
}
