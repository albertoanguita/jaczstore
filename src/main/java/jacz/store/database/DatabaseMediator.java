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

        connection.createStatement().executeUpdate("DROP TABLE metadata");
        connection.createStatement().executeUpdate("DROP TABLE movies");
        connection.createStatement().executeUpdate("DROP TABLE tv_series");
        connection.createStatement().executeUpdate("DROP TABLE chapters");
        connection.createStatement().executeUpdate("DROP TABLE people");
        connection.createStatement().executeUpdate("DROP TABLE companies");
        connection.createStatement().executeUpdate("DROP TABLE video_files");
        connection.createStatement().executeUpdate("DROP TABLE subtitle_files");
        connection.createStatement().executeUpdate("DROP TABLE image_files");
        connection.createStatement().executeUpdate("DROP TABLE movies_people");
        connection.createStatement().executeUpdate("DROP TABLE tv_series_people");
        connection.createStatement().executeUpdate("DROP TABLE chapters_people");
        connection.createStatement().executeUpdate("DROP TABLE movies_companies");
        connection.createStatement().executeUpdate("DROP TABLE tv_series_companies");
        connection.createStatement().executeUpdate("DROP TABLE movies_video_files");
        connection.createStatement().executeUpdate("DROP TABLE chapters_video_files");
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
                "CREATE TABLE movies (" +
                        "id             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp      INTEGER, " +
                        "title          TEXT, " +
                        "originalTitle  TEXT, " +
                        "year           INTEGER, " +
                        "countries      TEXT, " +
                        "externalURLs   TEXT, " +
                        "genres         TEXT, " +
                        "image_files_id INTEGER NOT NULL REFERENCES image_files(id), " +
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
                        "image_files_id INTEGER NOT NULL REFERENCES image_files(id) " +
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
                        "tv_series_id  INTEGER NOT NULL REFERENCES tv_series(id), " +
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
                        "video_files_id INTEGER NOT NULL REFERENCES video_files(id), " +
                        "language       TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE image_files (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "hash      TEXT, " +
                        "length    INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_people (\n" +
                        "movie_id  INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id INTEGER NOT NULL REFERENCES people(id), " +
                        "type      TEXT, " +
                        "PRIMARY KEY (movie_id, person_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE tv_series_people (\n" +
                        "tv_series_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id    INTEGER NOT NULL REFERENCES people(id), " +
                        "type         TEXT, " +
                        "PRIMARY KEY (tv_series_id, person_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE chapters_people (\n" +
                        "chapter_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id  INTEGER NOT NULL REFERENCES people(id), " +
                        "type       TEXT, " +
                        "PRIMARY KEY (chapter_id, person_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_companies (\n" +
                        "movie_id   INTEGER NOT NULL REFERENCES movies(id), " +
                        "company_id INTEGER NOT NULL REFERENCES people(id), " +
                        "PRIMARY KEY (movie_id, company_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE tv_series_companies (\n" +
                        "tv_series_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "company_id   INTEGER NOT NULL REFERENCES people(id), " +
                        "PRIMARY KEY (tv_series_id, company_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movies_video_files (\n" +
                        "movie_id      INTEGER NOT NULL REFERENCES movies(id), " +
                        "video_file_id INTEGER NOT NULL REFERENCES video_files(id), " +
                        "PRIMARY KEY (movie_id, video_file_id)\n" +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE chapters_video_files (\n" +
                        "chapter_id      INTEGER NOT NULL REFERENCES chapters(id), " +
                        "video_file_id INTEGER NOT NULL REFERENCES video_files(id), " +
                        "PRIMARY KEY (chapter_id, video_file_id)\n" +
                        ")"
        );

    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        dropDatabase("store2.db");
        createDatabase("store2.db");
    }
}
