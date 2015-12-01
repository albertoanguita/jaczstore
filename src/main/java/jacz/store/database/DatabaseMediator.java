package jacz.store.database;

import jacz.store.database.models.Metadata;
import org.javalite.activejdbc.Base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * General operations on the store database
 */
public class DatabaseMediator {

    public enum PERSON_TYPE {CREATOR, ACTOR}

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("Y/M/d-HH:mm:ss:SSS");



    private static void dropDatabase(String path) {
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:" + path, "", "");

        Base.exec("DROP TABLE IF EXISTS metadata");
        Base.exec("DROP TABLE IF EXISTS deleted_items");
        Base.exec("DROP TABLE IF EXISTS movies");
        Base.exec("DROP TABLE IF EXISTS tv_series");
        Base.exec("DROP TABLE IF EXISTS chapters");
        Base.exec("DROP TABLE IF EXISTS people");
        Base.exec("DROP TABLE IF EXISTS companies");
        Base.exec("DROP TABLE IF EXISTS video_files");
        Base.exec("DROP TABLE IF EXISTS subtitle_files");
        Base.exec("DROP TABLE IF EXISTS image_files");
        Base.exec("DROP TABLE IF EXISTS movies_people");
        Base.exec("DROP TABLE IF EXISTS tv_series_people");
        Base.exec("DROP TABLE IF EXISTS chapters_people");
        Base.exec("DROP TABLE IF EXISTS movies_companies");
        Base.exec("DROP TABLE IF EXISTS tv_series_companies");
        Base.exec("DROP TABLE IF EXISTS movies_video_files");
        Base.exec("DROP TABLE IF EXISTS chapters_video_files");

        Base.close();
    }

    private static void createDatabase(String path, String version, String identifier) {
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:" + path, "", "");

        Base.exec("CREATE TABLE metadata (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "version       TEXT, " +
                        "identifier    TEXT, " +
                        "creationDate  TEXT, " +
                        "lastAccess    TEXT, " +
                        "lastUpdate    TEXT, " +
                        "nextTimestamp INTEGER " +
                        ")"
        );
        Base.exec("CREATE TABLE deleted_items (" +
                        "id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "item_table TEXT, " +
                        "item_id    INTEGER, " +
                        "timestamp  INTEGER " +
                        ")"
        );
        Base.exec("CREATE TABLE movies (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "title         TEXT, " +
                        "originalTitle TEXT, " +
                        "year          INTEGER, " +
                        "countries     TEXT, " +
                        "externalURLs  TEXT, " +
                        "genres        TEXT, " +
                        "image_file_id INTEGER REFERENCES image_files(id), " +
                        "minutes       INTEGER " +
                        ")"
        );
        Base.exec("CREATE TABLE tv_series (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "title         TEXT, " +
                        "originalTitle TEXT, " +
                        "year          INTEGER, " +
                        "countries     TEXT, " +
                        "externalURLs  TEXT, " +
                        "genres        TEXT, " +
                        "image_file_id INTEGER REFERENCES image_files(id) " +
                        ")"
        );
        Base.exec("CREATE TABLE chapters (" +
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
        Base.exec("CREATE TABLE people (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "name      TEXT, " +
                        "aliases   TEXT " +
                        ")"
        );
        Base.exec("CREATE TABLE companies (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "name      TEXT, " +
                        "aliases   TEXT " +
                        ")"
        );
        Base.exec("CREATE TABLE video_files (" +
                        "id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp   INTEGER, " +
                        "hash        TEXT, " +
                        "length      INTEGER, " +
                        "name        TEXT, " +
                        "minutes     INTEGER, " +
                        "resolution  INTEGER, " +
                        "qualityCode TEXT, " +
                        "languages   TEXT " +
                        ")"
        );
        Base.exec("CREATE TABLE subtitle_files (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "hash          TEXT, " +
                        "length        INTEGER, " +
                        "name          TEXT, " +
                        "video_file_id INTEGER REFERENCES video_files(id), " +
                        "languages     TEXT " +
                        ")"
        );
        Base.exec("CREATE TABLE image_files (" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "hash      TEXT, " +
                        "length    INTEGER, " +
                        "name      TEXT " +
                        ")"
        );
        Base.exec("CREATE TABLE movies_people (\n" +
                        "id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp INTEGER, " +
                        "movie_id  INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id INTEGER NOT NULL REFERENCES people(id), " +
                        "type      TEXT" +
                        ")"
        );
        Base.exec("CREATE TABLE tv_series_people (\n" +
                        "id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp    INTEGER, " +
                        "tv_series_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id    INTEGER NOT NULL REFERENCES people(id), " +
                        "type         TEXT" +
                        ")"
        );
        Base.exec("CREATE TABLE chapters_people (\n" +
                        "id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp  INTEGER, " +
                        "chapter_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "person_id  INTEGER NOT NULL REFERENCES people(id), " +
                        "type       TEXT" +
                        ")"
        );
        Base.exec("CREATE TABLE movies_companies (\n" +
                        "id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp  INTEGER, " +
                        "movie_id   INTEGER NOT NULL REFERENCES movies(id), " +
                        "company_id INTEGER NOT NULL REFERENCES people(id)" +
                        ")"
        );
        Base.exec("CREATE TABLE tv_series_companies (\n" +
                        "id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp    INTEGER, " +
                        "tv_series_id INTEGER NOT NULL REFERENCES movies(id), " +
                        "company_id   INTEGER NOT NULL REFERENCES people(id)" +
                        ")"
        );
        Base.exec("CREATE TABLE movies_video_files (\n" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "movie_id      INTEGER NOT NULL REFERENCES movies(id), " +
                        "video_file_id INTEGER NOT NULL REFERENCES video_files(id)" +
                        ")"
        );
        Base.exec("CREATE TABLE chapters_video_files (\n" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "chapter_id    INTEGER NOT NULL REFERENCES chapters(id), " +
                        "video_file_id INTEGER NOT NULL REFERENCES video_files(id)" +
                        ")"
        );
        String nowString = dateFormat.format(new Date());
        new Metadata()
                .set("version", version)
                .set("identifier", identifier)
                .set("creationDate", nowString)
                .set("lastAccess", nowString)
                .set("lastUpdate", nowString)
                .set("nextTimestamp", 1L)
                .saveIt();

        Base.close();
    }

    public static void updateLastAccessTime() {
        Metadata metadata = getMetadata();
        metadata.set("lastAccess", dateFormat.format(new Date())).saveIt();
    }

    public static void updateLastUpdateTime() {
        Metadata metadata = getMetadata();
        metadata.set("lastUpdate", dateFormat.format(new Date())).saveIt();
    }

    public static synchronized long getNewTimestamp() {
        Metadata metadata = getMetadata();
        long newTimestamp = metadata.getLong("nextTimestamp");
        metadata.setLong("nextTimestamp", newTimestamp + 1).saveIt();
        return newTimestamp;
    }

    private static Metadata getMetadata() {
        return (Metadata) Metadata.findAll().get(0);
    }

    public static void main(String[] args) {
        dropAndCreate("store.db", "v1", "a");
    }

    public static void dropAndCreate(String path, String version, String identifier) {
        dropDatabase(path);
        createDatabase(path, version, identifier);
    }
}
