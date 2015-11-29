package jacz.store.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * General operations on the store database
 */
public class DatabaseMediator {

    public enum PERSON_TYPE {CREATOR, ACTOR}

    private static void dropDatabase(String path) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        // create a database connection
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS metadata");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS deleted_items");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS movies");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS tv_series");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS chapters");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS people");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS companies");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS video_files");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS subtitle_files");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS image_files");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS movies_people");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS tv_series_people");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS chapters_people");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS movies_companies");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS tv_series_companies");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS movies_video_files");
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS chapters_video_files");
        connection.close();
    }

    private static void createDatabase(String path) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        // create a database connection
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        connection.createStatement().executeUpdate(
                "CREATE TABLE metadata (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "version       TEXT, " +
                        "identifier    TEXT, " +
                        "creationDate  DATE, " +
                        "lastAccess    DATE, " +
                        "lastUpdate    DATE, " +
                        "lastTimestamp INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE deleted_items (" +
                        "id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "item_table TEXT, " +
                        "item_id    INTEGER, " +
                        "timestamp  INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies (" +
                        "id             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp      INTEGER, " +
                        "title          TEXT, " +
                        "originalTitle  TEXT, " +
                        "year           INTEGER, " +
                        "countries      TEXT, " +
                        "externalURLs   TEXT, " +
                        "genres         TEXT, " +
                        "image_file_id INTEGER REFERENCES image_files(id), " +
                        "minutes        INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE tv_series (" +
                        "id             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp      INTEGER, " +
                        "title          TEXT, " +
                        "originalTitle  TEXT, " +
                        "year           INTEGER, " +
                        "countries      TEXT, " +
                        "externalURLs   TEXT, " +
                        "genres         TEXT, " +
                        "image_file_id INTEGER REFERENCES image_files(id) " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE chapters (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "title         TEXT, " +
                        "originalTitle TEXT, " +
                        "year          INTEGER, " +
                        "countries     TEXT, " +
                        "externalURLs  TEXT, " +
                        "tv_series_id  INTEGER REFERENCES tv_series(id), " +
                        "season        TEXT, " +
                        "minutes       INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE people (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "name      TEXT, " +
                        "aliases   TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE companies (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "name      TEXT, " +
                        "aliases   TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE video_files (" +
                        "id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp   INTEGER, " +
                        "hash        TEXT, " +
                        "length      INTEGER, " +
                        "name        TEXT, " +
                        "minutes     INTEGER, " +
                        "qualityCode TEXT, " +
                        "languages   TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE subtitle_files (" +
                        "id             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp      INTEGER, " +
                        "hash           TEXT, " +
                        "length         INTEGER, " +
                        "name           TEXT, " +
                        "video_file_id  INTEGER REFERENCES video_files(id), " +
                        "languages      TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE image_files (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "hash      TEXT, " +
                        "length    INTEGER, " +
                        "name      TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_people (\n" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "movie_id  INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id INTEGER NOT NULL REFERENCES people(id), " +
                        "type      TEXT" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE tv_series_people (\n" +
                        "id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp    INTEGER, " +
                        "tv_series_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id    INTEGER NOT NULL REFERENCES people(id), " +
                        "type         TEXT" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE chapters_people (\n" +
                        "id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp  INTEGER, " +
                        "chapter_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id  INTEGER NOT NULL REFERENCES people(id), " +
                        "type       TEXT" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_companies (\n" +
                        "id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp  INTEGER, " +
                        "movie_id   INTEGER NOT NULL REFERENCES movies(id), " +
                        "company_id INTEGER NOT NULL REFERENCES people(id)" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE tv_series_companies (\n" +
                        "id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp    INTEGER, " +
                        "tv_series_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "company_id   INTEGER NOT NULL REFERENCES people(id)" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_video_files (\n" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "movie_id      INTEGER NOT NULL REFERENCES movies(id), " +
                        "video_file_id INTEGER NOT NULL REFERENCES video_files(id)" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE chapters_video_files (\n" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "chapter_id    INTEGER NOT NULL REFERENCES chapters(id), " +
                        "video_file_id INTEGER NOT NULL REFERENCES video_files(id)" +
                        ")"
        );
        connection.close();
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        dropAndCreate();
    }

    public static void dropAndCreate() throws SQLException, ClassNotFoundException {
        dropDatabase("store.db");
        createDatabase("store.db");
    }
}
