package jacz.store.database;

import jacz.store.ConcurrentDataAccessControl;
import jacz.store.database.models.Metadata;
import org.javalite.activejdbc.Base;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * General operations on the store database
 */
public class DatabaseMediator {

    public enum ItemType {
        MOVIE,
        TV_SERIES,
        CHAPTER,
        PERSON,
        COMPANY,
        VIDEO_FILE,
        SUBTITLE_FILE,
        IMAGE_FILE
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("Y/M/d-HH:mm:ss:SSS");

    private static final Pattern AUTOCOMPLETE_DB = Pattern.compile("^(.)*store.db$");

    private static int connectionCount = 0;


    public static void dropAndCreate(String path, String version, String identifier) {
        dropDatabase(path);
        createDatabase(path, version, identifier);
    }


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
                        "id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp       INTEGER, " +
                        "title           TEXT, " +
                        "originalTitle   TEXT, " +
                        "year            INTEGER, " +
                        "creator_list    TEXT, " +
                        "actor_list      TEXT, " +
                        "company_list    TEXT, " +
                        "countries       TEXT, " +
                        "externalURLs    TEXT, " +
                        "genres          TEXT, " +
                        "video_file_list TEXT, " +
                        "image_hash      TEXT, " +
                        "minutes         INTEGER " +
                        ")"
        );
        Base.exec("CREATE TABLE tv_series (" +
                        "id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp     INTEGER, " +
                        "title         TEXT, " +
                        "originalTitle TEXT, " +
                        "year          INTEGER, " +
                        "creator_list  TEXT, " +
                        "actor_list    TEXT, " +
                        "company_list  TEXT, " +
                        "countries     TEXT, " +
                        "externalURLs  TEXT, " +
                        "genres        TEXT, " +
                        "image_hash    TEXT " +
                        ")"
        );
        Base.exec("CREATE TABLE chapters (" +
                        "id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "timestamp       INTEGER, " +
                        "title           TEXT, " +
                        "originalTitle   TEXT, " +
                        "year            INTEGER, " +
                        "creator_list    TEXT, " +
                        "actor_list      TEXT, " +
                        "countries       TEXT, " +
                        "externalURLs    TEXT, " +
                        "tv_series_id    INTEGER REFERENCES tv_series(id), " +
                        "season          TEXT, " +
                        "video_file_list TEXT, " +
                        "minutes         INTEGER " +
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

    public static String getDatabaseIdentifier(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        disconnect(dbPath);
        return metadata.getString("identifier");
    }

    public static void setDatabaseIdentifier(String dbPath, String identifier) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.setString("identifier", identifier).saveIt();
        disconnect(dbPath);
    }

    public static void updateLastAccessTime(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.set("lastAccess", dateFormat.format(new Date())).saveIt();
        disconnect(dbPath);
    }

    public static void updateLastUpdateTime(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.set("lastUpdate", dateFormat.format(new Date())).saveIt();
        disconnect(dbPath);
    }

    public static synchronized long getNewTimestamp(String dbPath) {
        connect(dbPath);
        long newTimestamp = getNewTimestamp();
        disconnect(dbPath);
        return newTimestamp;
    }

    public static synchronized long getLastTimestamp(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        long nextTimestamp = metadata.getLong("nextTimestamp");
        disconnect(dbPath);
        return nextTimestamp - 1;
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

    public static void connect(String dbPath) {
        ConcurrentDataAccessControl.getInstance().getConcurrencyController().beginActivity(dbPath);
        synchronized (DatabaseMediator.class) {
            if (connectionCount == 0) {
                Base.open("org.sqlite.JDBC", "jdbc:sqlite:" + dbPath, "", "");
            }
            connectionCount++;
        }
    }

    public static void disconnect(String dbPath) {
        ConcurrentDataAccessControl.getInstance().getConcurrencyController().endActivity(dbPath);
        synchronized (DatabaseMediator.class) {
            connectionCount--;
            if (connectionCount == 0) {
                Base.close();
            }
        }
    }

    public static boolean mustAutoComplete() {
        try {
            return DatabaseMediator.AUTOCOMPLETE_DB.matcher(Base.connection().getMetaData().getURL()).matches();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        dropAndCreate("store.db", "v1", "a");
    }
}