package jacz.store.JalaLiteTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 */
public class Test {

    private static void dropDatabase(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("DROP TABLE movies");
        connection.createStatement().executeUpdate("DROP TABLE people");
        connection.createStatement().executeUpdate("DROP TABLE movies_actors");
        connection.createStatement().executeUpdate("DROP TABLE movies_directors");
    }

    private static void createDatabase(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT, " +
                        "year INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE people (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_actors (\n" +
                        "movie_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id  INTEGER NOT NULL REFERENCES people(id), " +
                        "PRIMARY KEY (movie_id, person_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_directors (\n" +
                        "movie_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id  INTEGER NOT NULL REFERENCES people(id), " +
                        "PRIMARY KEY (movie_id, person_id)\n" +
                        ")"
        );
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        ContentValues initialValues = new ContentValues();
//        initialValues.put("date_created", dateFormat.format(date));
//        long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    private static void addMovie(Connection connection, int id, String title, int year) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO movies VALUES (?, ?, ?)"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, title);
        preparedStatement.setInt(3, year);
        preparedStatement.executeUpdate();
    }

    private static void addPerson(Connection connection, int id, String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO people VALUES (?, ?)"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();
    }

    private static void addActors(Connection connection, int movieID, int... personID) throws SQLException {
        for (int actorID : personID) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO movies_actors VALUES (?, ?)"
            );
            preparedStatement.setInt(1, movieID);
            preparedStatement.setInt(2, actorID);
            preparedStatement.executeUpdate();
        }
    }

    private static void addDirectors(Connection connection, int movieID, int... personID) throws SQLException {
        for (int directorID : personID) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO movies_directors VALUES (?, ?)"
            );
            preparedStatement.setInt(1, movieID);
            preparedStatement.setInt(2, directorID);
            preparedStatement.executeUpdate();
        }
    }


    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        // create a database connection
        Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");

        dropDatabase(connection);

        createDatabase(connection);
        addMovie(connection, 0, "Terminator", 1989);
        addMovie(connection, 1, "Aladdin", 1985);
        addMovie(connection, 2, "The hope", 2001);
        addMovie(connection, 3, "Basic instinct", 1995);
        addMovie(connection, 4, "Sister act", 2000);
        addMovie(connection, 5, "2001", 1975);
        addMovie(connection, 6, "Rocky", 1982);
        addMovie(connection, 7, "Abyss", 1988);

        addPerson(connection, 0, "Mark");
        addPerson(connection, 1, "Bradly");
        addPerson(connection, 2, "Anna");
        addPerson(connection, 3, "Harry");
        addPerson(connection, 4, "Silvester");
        addPerson(connection, 5, "Bob");
        addPerson(connection, 6, "Helen");
        addPerson(connection, 7, "Robert");
        addPerson(connection, 8, "Irina");
        addPerson(connection, 9, "Peter");
        addPerson(connection, 10, "Steven");
        addPerson(connection, 11, "George");
        addPerson(connection, 12, "Ridley");

        addActors(connection, 0, 0, 3, 4);
        addActors(connection, 1, 2, 8);
        addActors(connection, 2, 5, 6, 7);
        addActors(connection, 3, 4, 5);
        addActors(connection, 4, 3, 4);
        addActors(connection, 5, 2, 6, 7);
        addActors(connection, 6, 1, 5, 6);
        addActors(connection, 7, 0, 4, 5, 6, 7);

        addDirectors(connection, 0, 9);
        addDirectors(connection, 1, 10);
        addDirectors(connection, 2, 11);
        addDirectors(connection, 3, 12);
        addDirectors(connection, 4, 9);
        addDirectors(connection, 5, 10);
        addDirectors(connection, 6, 11);
        addDirectors(connection, 7, 11, 12);
    }
}