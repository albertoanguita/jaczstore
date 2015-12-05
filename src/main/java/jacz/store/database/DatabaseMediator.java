package jacz.store.database;

import jacz.store.ConcurrentDataAccessControl;
import jacz.store.database.models.*;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * General operations on the store database
 */
public class DatabaseMediator {

    public enum ItemType {
        METADATA("metadata", Metadata.class),
        DELETED_ITEMS("deleted_items", DeletedItem.class),
        MOVIE("movies", Movie.class),
        TV_SERIES("tv_series", TVSeries.class),
        CHAPTER("chapters", Chapter.class),
        PERSON("people", Person.class),
        COMPANY("companies", Company.class),
        VIDEO_FILE("video_files", VideoFile.class),
        SUBTITLE_FILE("subtitle_files", SubtitleFile.class),
        TAG("tags", Tag.class);

        public final String table;

        public final Class<? extends Model> modelClass;

        ItemType(String table, Class<? extends Model> modelClass) {
            this.table = table;
            this.modelClass = modelClass;
        }
    }

    public enum Field {
        ID("id", Type.INTEGER_PK_AUTO),
        VERSION("version", Type.TEXT),
        IDENTIFIER("identifier", Type.TEXT),
        CREATION_DATE("creationDate", Type.TEXT),
        LAST_ACCESS("lastAccess", Type.TEXT),
        LAST_UPDATE("lastUpdate", Type.TEXT),
        NEXT_TIMESTAMP("nextTimestamp", Type.INTEGER),
        ITEM_TABLE("item_table", Type.TEXT),
        ITEM_ID("item_id", Type.INTEGER),
        TIMESTAMP("timestamp", Type.INTEGER),
        TITLE("title", Type.TEXT),
        ORIGINAL_TITLE("originalTitle", Type.TEXT),
        YEAR("year", Type.INTEGER),
        CREATOR_LIST("creator_list", Type.TEXT),
        ACTOR_LIST("actor_list", Type.TEXT),
        COMPANY_LIST("company_list", Type.TEXT),
        COUNTRIES("countries", Type.TEXT),
        EXTERNAL_URLS("externalURLs", Type.TEXT),
        GENRES("genres", Type.TEXT),
        VIDEO_FILE_LIST("video_file_list", Type.TEXT),
        IMAGE_HASH("image_hash", Type.TEXT),
        MINUTES("minutes", Type.INTEGER),
        TV_SERIES_ID("tv_series_id", Type.INTEGER_REF_TV_SERIES),
        SEASON("season", Type.TEXT),
        NAME("name", Type.TEXT),
        ALIASES("aliases", Type.TEXT),
        HASH("hash", Type.TEXT),
        LENGTH("length", Type.INTEGER),
        RESOLUTION("resolution", Type.INTEGER),
        QUALITY_CODE("qualityCode", Type.TEXT),
        LANGUAGES("languages", Type.TEXT),
        VIDEO_FILE_ID("video_file_id", Type.INTEGER_REF_VIDEO_FILES);

        public final String value;

        public final Type type;

        Field(String value, Type type) {
            this.value = value;
            this.type = type;
        }
    }

    private enum Type {
        INTEGER_PK_AUTO("INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"),
        TEXT("TEXT"),
        INTEGER("INTEGER"),
        INTEGER_REF_TV_SERIES("INTEGER REFERENCES tv_series(id)"),
        INTEGER_REF_VIDEO_FILES("INTEGER REFERENCES video_files(id)");

        private final String value;

        Type(String value) {
            this.value = value;
        }
    }
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("Y/M/d-HH:mm:ss:SSS");

    private static final Pattern AUTOCOMPLETE_DB = Pattern.compile("^(.)*store.db$");

    private static int connectionCount = 0;


    public static void dropAndCreate(String path, String version, String identifier) {
        dropDatabase(path);
        createDatabase(path, version, identifier);
    }


    private static void dropDatabase(String path) {
        connect(path);

        for (ItemType itemType : ItemType.values()) {
            Base.exec("DROP TABLE IF EXISTS " + itemType.table);
        }

        disconnect(path);
    }

    private static void createDatabase(String path, String version, String identifier) {
        connect(path);

        createTable(ItemType.METADATA, Field.ID, Field.VERSION, Field.IDENTIFIER, Field.CREATION_DATE,
                Field.LAST_ACCESS, Field.LAST_UPDATE, Field.NEXT_TIMESTAMP);
        createTable(ItemType.DELETED_ITEMS, Field.ID, Field.ITEM_TABLE, Field.ITEM_ID, Field.TIMESTAMP);
        createTable(ItemType.MOVIE, Field.ID, Field.TIMESTAMP, Field.TITLE, Field.ORIGINAL_TITLE, Field.YEAR,
                Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COMPANY_LIST, Field.COUNTRIES, Field.EXTERNAL_URLS,
                Field.GENRES, Field.VIDEO_FILE_LIST, Field.IMAGE_HASH, Field.MINUTES);
        createTable(ItemType.TV_SERIES, Field.ID, Field.TIMESTAMP, Field.TITLE, Field.ORIGINAL_TITLE, Field.YEAR,
                Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COMPANY_LIST, Field.COUNTRIES, Field.EXTERNAL_URLS,
                Field.GENRES, Field.IMAGE_HASH);
        createTable(ItemType.CHAPTER, Field.ID, Field.TIMESTAMP, Field.TITLE, Field.ORIGINAL_TITLE, Field.YEAR,
                Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COUNTRIES, Field.EXTERNAL_URLS,
                Field.TV_SERIES_ID, Field.SEASON, Field.VIDEO_FILE_LIST, Field.MINUTES);
        createTable(ItemType.PERSON, Field.ID, Field.TIMESTAMP, Field.NAME, Field.ALIASES);
        createTable(ItemType.COMPANY, Field.ID, Field.TIMESTAMP, Field.NAME, Field.ALIASES);
        createTable(ItemType.VIDEO_FILE, Field.ID, Field.TIMESTAMP, Field.HASH, Field.LENGTH, Field.NAME,
                Field.MINUTES, Field.RESOLUTION, Field.QUALITY_CODE, Field.LANGUAGES);
        createTable(ItemType.SUBTITLE_FILE, Field.ID, Field.TIMESTAMP, Field.HASH, Field.LENGTH, Field.NAME,
                Field.VIDEO_FILE_ID, Field.LANGUAGES);
        createTable(ItemType.TAG, Field.ID, Field.ITEM_TABLE, Field.ITEM_ID, Field.NAME);

        String nowString = dateFormat.format(new Date());
        new Metadata()
                .set(Field.VERSION.value, version)
                .set(Field.IDENTIFIER.value, identifier)
                .set(Field.CREATION_DATE.value, nowString)
                .set(Field.LAST_ACCESS.value, nowString)
                .set(Field.LAST_UPDATE.value, nowString)
                .set(Field.NEXT_TIMESTAMP.value, 1L)
                .saveIt();

        disconnect(path);
    }

    private static void createTable(ItemType itemType, Field... fields) {
        StringBuilder create = new StringBuilder("CREATE TABLE ").append(itemType.table).append("(");
        for (Field field : fields) {
            create.append(field.value).append(" ").append(field.type.value).append(",");
        }
        // replace last comma with a ')'
        create.replace(create.length() - 1, create.length(), ")");
        Base.exec(create.toString());
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