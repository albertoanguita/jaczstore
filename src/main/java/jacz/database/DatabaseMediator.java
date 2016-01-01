package jacz.database;

import jacz.database.models.Chapter;
import jacz.database.models.Company;
import jacz.database.models.DeletedItem;
import jacz.database.models.Metadata;
import jacz.database.models.Movie;
import jacz.database.models.Person;
import jacz.database.models.SubtitleFile;
import jacz.database.models.TVSeries;
import jacz.database.models.Tag;
import jacz.database.models.VideoFile;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * General operations on the store database
 */
public class DatabaseMediator {

    public enum ItemType {
        METADATA("metadata", Metadata.class, Field.ID, Field.VERSION, Field.IDENTIFIER, Field.CREATION_DATE,
                Field.LAST_ACCESS, Field.LAST_UPDATE, Field.NEXT_TIMESTAMP, Field.HIGHEST_MANUAL_TIMESTAMP),
        DELETED_ITEM("deleted_items", DeletedItem.class, Field.ID, Field.ITEM_TYPE, Field.ITEM_ID, Field.TIMESTAMP),
        MOVIE("movies", Movie.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.TITLE, Field.ORIGINAL_TITLE,
                Field.YEAR, Field.SYNOPSIS, Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COMPANY_LIST, Field.COUNTRIES,
                Field.EXTERNAL_URLS, Field.GENRES, Field.VIDEO_FILE_LIST, Field.IMAGE_HASH, Field.MINUTES),
        TV_SERIES("tv_series", TVSeries.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.TITLE,
                Field.ORIGINAL_TITLE, Field.YEAR, Field.SYNOPSIS, Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COMPANY_LIST,
                Field.COUNTRIES, Field.EXTERNAL_URLS, Field.GENRES, Field.IMAGE_HASH, Field.CHAPTER_LIST),
        CHAPTER("chapters", Chapter.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.TITLE,
                Field.ORIGINAL_TITLE, Field.YEAR, Field.SYNOPSIS, Field.CREATOR_LIST, Field.ACTOR_LIST,
                Field.COUNTRIES, Field.EXTERNAL_URLS, Field.SEASON, Field.VIDEO_FILE_LIST, Field.MINUTES),
        PERSON("people", Person.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.NAME, Field.ALIASES),
        COMPANY("companies", Company.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.NAME, Field.ALIASES),
        VIDEO_FILE("video_files", VideoFile.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.HASH,
                Field.LENGTH, Field.NAME, Field.ADDITIONAL_SOURCES, Field.MINUTES, Field.RESOLUTION,
                Field.QUALITY_CODE, Field.LANGUAGES, Field.SUBTITLE_FILE_LIST),
        SUBTITLE_FILE("subtitle_files", SubtitleFile.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP,
                Field.HASH, Field.LENGTH, Field.NAME, Field.ADDITIONAL_SOURCES, Field.LANGUAGES),
        TAG("tags", Tag.class, Field.ID, Field.ITEM_TYPE, Field.ITEM_ID, Field.NAME);

        public final String table;

        public final Class<? extends Model> modelClass;

        public final Field[] fields;

        ItemType(String table, Class<? extends Model> modelClass, Field... fields) {
            this.table = table;
            DatabaseMediator.addTableNameToItemMap(table, this);
            this.modelClass = modelClass;
            this.fields = fields;
        }
    }

    public enum Field {
        ID("id", Type.INTEGER_PK_AUTO),
        VERSION("version", Type.TEXT),
        IDENTIFIER("identifier", Type.TEXT),
        CREATION_DATE("creation_date", Type.TEXT),
        LAST_ACCESS("last_access", Type.TEXT),
        LAST_UPDATE("last_update", Type.TEXT),
        NEXT_TIMESTAMP("next_timestamp", Type.INTEGER),
        HIGHEST_MANUAL_TIMESTAMP("highest_manual_timestamp", Type.INTEGER),
        ITEM_TYPE("item_type", Type.TEXT),
        ITEM_ID("item_id", Type.INTEGER),
        TIMESTAMP("timestamp", Type.INTEGER),
        TITLE("title", Type.TEXT),
        ORIGINAL_TITLE("original_title", Type.TEXT),
        YEAR("year", Type.INTEGER),
        SYNOPSIS("synopsis", Type.TEXT),
        CREATOR_LIST("creator_list", Type.TEXT),
        ACTOR_LIST("actor_list", Type.TEXT),
        COMPANY_LIST("company_list", Type.TEXT),
        COUNTRIES("countries", Type.TEXT),
        EXTERNAL_URLS("external_urls", Type.TEXT),
        GENRES("genres", Type.TEXT),
        VIDEO_FILE_LIST("video_file_list", Type.TEXT),
        IMAGE_HASH("image_hash", Type.TEXT),
        MINUTES("minutes", Type.INTEGER),
        CHAPTER_LIST("chapter_list", Type.TEXT),
        SEASON("season", Type.TEXT),
        NAME("name", Type.TEXT),
        ALIASES("aliases", Type.TEXT),
        HASH("hash", Type.TEXT),
        LENGTH("length", Type.INTEGER),
        ADDITIONAL_SOURCES("additional_sources", Type.TEXT),
        RESOLUTION("resolution", Type.INTEGER),
        QUALITY_CODE("quality_code", Type.TEXT),
        LANGUAGES("languages", Type.TEXT),
        SUBTITLE_FILE_LIST("subtitle_file_list", Type.TEXT);

        public final String value;

        public final Type type;

        Field(String value, Type type) {
            this.value = value;
            this.type = type;
        }

        boolean canBeReset() {
            return this != DatabaseMediator.Field.ID &&
                    this != DatabaseMediator.Field.CREATION_DATE &&
                    this != DatabaseMediator.Field.TIMESTAMP;
        }
    }

    private enum Type {
        INTEGER_PK_AUTO("INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"),
        TEXT("TEXT"),
        INTEGER("INTEGER");
//        INTEGER_REF_TV_SERIES("INTEGER REFERENCES tv_series(id)"),
//        INTEGER_REF_VIDEO_FILES("INTEGER REFERENCES video_files(id)");

        private final String value;

        Type(String value) {
            this.value = value;
        }
    }

    public enum ReferencedList {
        CREATORS,
        ACTORS,
        COMPANIES,
        CHAPTERS
    }

    private static final String VERSION_0_1 = "0.1";

    public static final String CURRENT_VERSION = VERSION_0_1;

    private static final Map<String, ItemType> tableNameToItemType = new HashMap<>();

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("Y/M/d-HH:mm:ss:SSS");

//    private static final Pattern AUTOCOMPLETE_DB = Pattern.compile("^(.)*store.db$");

    private static int connectionCount = 0;

    private static void addTableNameToItemMap(String tableName, ItemType type) {
        tableNameToItemType.put(tableName, type);
    }

    public static ItemType getItemType(String tableName) {
        return tableNameToItemType.get(tableName);
    }

    public static void dropAndCreate(String path, String identifier) {
        dropDatabase(path);
        createDatabase(path, CURRENT_VERSION, identifier);
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

        createTable(ItemType.METADATA);
        createTable(ItemType.DELETED_ITEM);
        createTable(ItemType.MOVIE);
        createTable(ItemType.TV_SERIES);
        createTable(ItemType.CHAPTER);
        createTable(ItemType.PERSON);
        createTable(ItemType.COMPANY);
        createTable(ItemType.VIDEO_FILE);
        createTable(ItemType.SUBTITLE_FILE);
        createTable(ItemType.TAG);

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

    private static void createTable(ItemType itemType) {
        StringBuilder create = new StringBuilder("CREATE TABLE ").append(itemType.table).append("(");
        for (Field field : itemType.fields) {
            create.append(field.value).append(" ").append(field.type.value).append(",");
        }
        // replace last comma with a ')'
        create.replace(create.length() - 1, create.length(), ")");
        Base.exec(create.toString());
    }

    public static DatabaseItem createNewItem(String dbPath, ItemType type) {
        switch (type) {

            case MOVIE:
                return new jacz.database.Movie(dbPath);

            case TV_SERIES:
                return new jacz.database.TVSeries(dbPath);

            case CHAPTER:
                return new jacz.database.Chapter(dbPath);

            case PERSON:
                return new jacz.database.Person(dbPath);

            case COMPANY:
                return new jacz.database.Company(dbPath);

            case VIDEO_FILE:
                return new jacz.database.VideoFile(dbPath);

            case SUBTITLE_FILE:
                return new jacz.database.SubtitleFile(dbPath);

            default:
                throw new IllegalArgumentException("Cannot build items of type " + type.name());
        }
    }

    public static List<? extends DatabaseItem> getItems(String dbPath, ItemType type) {
        switch (type) {

            case MOVIE:
                return jacz.database.Movie.getMovies(dbPath);

            case TV_SERIES:
                return jacz.database.TVSeries.getTVSeries(dbPath);

            case CHAPTER:
                return jacz.database.Chapter.getChapters(dbPath);

            case PERSON:
                return jacz.database.Person.getPeople(dbPath);

            case COMPANY:
                return jacz.database.Company.getCompanies(dbPath);

            case VIDEO_FILE:
                return jacz.database.VideoFile.getVideoFiles(dbPath);

            case SUBTITLE_FILE:
                return jacz.database.SubtitleFile.getSubtitleFiles(dbPath);

            default:
                throw new IllegalArgumentException("Cannot build items of type " + type.name());
        }
    }

    public static DatabaseItem getItem(String dbPath, ItemType type, Integer id) {
        switch (type) {

            case MOVIE:
                return jacz.database.Movie.getMovieById(dbPath, id);

            case TV_SERIES:
                return jacz.database.TVSeries.getTVSeriesById(dbPath, id);

            case CHAPTER:
                return jacz.database.Chapter.getChapterById(dbPath, id);

            case PERSON:
                return jacz.database.Person.getPersonById(dbPath, id);

            case COMPANY:
                return jacz.database.Company.getCompanyById(dbPath, id);

            case VIDEO_FILE:
                return jacz.database.VideoFile.getVideoFileById(dbPath, id);

            case SUBTITLE_FILE:
                return jacz.database.SubtitleFile.getSubtitleFileById(dbPath, id);

            default:
                throw new IllegalArgumentException("Cannot build items of type " + type.name());
        }
    }

    public static String getDatabaseIdentifier(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        disconnect(dbPath);
        return metadata.getString(Field.IDENTIFIER.value);
    }

    public static void setDatabaseIdentifier(String dbPath, String identifier) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.setString(Field.IDENTIFIER.value, identifier).saveIt();
        disconnect(dbPath);
    }

    public static void updateLastAccessTime(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.set(Field.LAST_ACCESS.value, dateFormat.format(new Date())).saveIt();
        disconnect(dbPath);
    }

    public static void updateLastUpdateTime(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.set(Field.LAST_UPDATE.value, dateFormat.format(new Date())).saveIt();
        disconnect(dbPath);
    }

    public static synchronized long getNewTimestamp(String dbPath) {
        connect(dbPath);
        long newTimestamp = getNewTimestamp();
        disconnect(dbPath);
        return newTimestamp;
    }

    public static synchronized long getNewTimestamp() {
        Metadata metadata = getMetadata();
        long newTimestamp = metadata.getLong(Field.NEXT_TIMESTAMP.value);
        metadata.setLong(Field.NEXT_TIMESTAMP.value, newTimestamp + 1).saveIt();
        return newTimestamp;
    }

    public static synchronized Long getHighestManualTimestamp(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        Long highestTimestamp = metadata.getLong(Field.HIGHEST_MANUAL_TIMESTAMP.value);
        disconnect(dbPath);
        return highestTimestamp;
    }

    public static void updateHighestManualTimestamp(String dbPath, long timestamp) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.setLong(Field.HIGHEST_MANUAL_TIMESTAMP.value, timestamp).saveIt();
        disconnect(dbPath);
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

    public static String getDBPath() {
        try {
            String url = Base.connection().getMetaData().getURL();
            return url.substring(url.indexOf(':') + 1);
        } catch (SQLException e) {
            // todo
            return null;
        }
    }

//    public static boolean mustAutoComplete() {
//        // todo remove
//        try {
//            return DatabaseMediator.AUTOCOMPLETE_DB.matcher(Base.connection().getMetaData().getURL()).matches();
//        } catch (SQLException e) {
//            return false;
//        }
//    }

    public static void main(String[] args) {
        dropAndCreate("store.db", "a");
    }
}