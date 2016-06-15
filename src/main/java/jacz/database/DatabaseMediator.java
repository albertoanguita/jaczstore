package jacz.database;

import jacz.database.models.Chapter;
import jacz.database.models.DeletedItem;
import jacz.database.models.Metadata;
import jacz.database.models.Movie;
import jacz.database.models.SubtitleFile;
import jacz.database.models.TVSeries;
import jacz.database.models.Tag;
import jacz.database.models.VideoFile;
import jacz.database.util.ImageHash;
import jacz.util.io.serialization.activejdbcsupport.ActiveJDBCController;
import jacz.util.log.ErrorFactory;
import jacz.util.log.ErrorHandler;
import jacz.util.log.ErrorLog;
import org.javalite.activejdbc.Model;

import java.sql.SQLException;
import java.util.*;

/**
 * General operations on the store database
 */
public class DatabaseMediator {

    public enum ItemType {
        METADATA("metadata", Metadata.class, Field.ID, Field.VERSION, Field.IDENTIFIER, Field.CREATION_DATE,
                Field.LAST_UPDATE, Field.NEXT_TIMESTAMP, Field.HIGHEST_MANUAL_TIMESTAMP),
        DELETED_ITEM("deleted_items", DeletedItem.class, Field.ID, Field.ITEM_TYPE, Field.ITEM_ID, Field.TIMESTAMP),
        MOVIE("movies", Movie.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.LANGUAGE, Field.TITLE, Field.ORIGINAL_TITLE,
                Field.YEAR, Field.SYNOPSIS, Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COMPANY_LIST, Field.COUNTRIES,
                Field.URIS, Field.GENRES, Field.VIDEO_FILE_LIST, Field.IMAGE_HASH, Field.MINUTES),
        TV_SERIES("tv_series", TVSeries.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.LANGUAGE, Field.TITLE,
                Field.ORIGINAL_TITLE, Field.YEAR, Field.SYNOPSIS, Field.CREATOR_LIST, Field.ACTOR_LIST, Field.COMPANY_LIST,
                Field.COUNTRIES, Field.URIS, Field.GENRES, Field.IMAGE_HASH, Field.CHAPTER_LIST),
        CHAPTER("chapters", Chapter.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.LANGUAGE, Field.TITLE,
                Field.ORIGINAL_TITLE, Field.YEAR, Field.SYNOPSIS, Field.CREATOR_LIST, Field.ACTOR_LIST,
                Field.COUNTRIES, Field.URIS, Field.SEASON, Field.NUMBER, Field.VIDEO_FILE_LIST, Field.MINUTES),
        VIDEO_FILE("video_files", VideoFile.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP, Field.HASH,
                Field.LENGTH, Field.NAME, Field.ADDITIONAL_SOURCES, Field.MINUTES, Field.RESOLUTION,
                Field.QUALITY_CODE, Field.LOCALIZED_LANGUAGE_LIST, Field.SUBTITLE_FILE_LIST),
        SUBTITLE_FILE("subtitle_files", SubtitleFile.class, Field.ID, Field.CREATION_DATE, Field.TIMESTAMP,
                Field.HASH, Field.LENGTH, Field.NAME, Field.ADDITIONAL_SOURCES, Field.LOCALIZED_LANGUAGE),
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

        public boolean hasImageHash() {
            return this == MOVIE || this == TV_SERIES;
        }
    }

    public enum Field {
        ID("id", DBType.ID),
        VERSION("version", DBType.TEXT),
        IDENTIFIER("identifier", DBType.TEXT),
        CREATION_DATE("creation_date", DBType.DATE),
        LAST_UPDATE("last_update", DBType.DATE),
        NEXT_TIMESTAMP("next_timestamp", DBType.INTEGER),
        HIGHEST_MANUAL_TIMESTAMP("highest_manual_timestamp", DBType.INTEGER),
        ITEM_TYPE("item_type", DBType.TEXT),
        ITEM_ID("item_id", DBType.INTEGER),
        TIMESTAMP("timestamp", DBType.LONG),
        TITLE("title", DBType.TEXT),
        ORIGINAL_TITLE("original_title", DBType.TEXT),
        YEAR("year", DBType.INTEGER),
        SYNOPSIS("synopsis", DBType.TEXT),
        CREATOR_LIST("creator_list", DBType.STRING_LIST),
        ACTOR_LIST("actor_list", DBType.STRING_LIST),
        COMPANY_LIST("company_list", DBType.STRING_LIST),
        COUNTRIES("countries", DBType.COUNTRY_LIST),
        URIS("uris", DBType.STRING_LIST),
        GENRES("genres", DBType.GENRE_LIST),
        VIDEO_FILE_LIST("video_file_list", DBType.ID_LIST),
        IMAGE_HASH("image_hash", DBType.TEXT),
        MINUTES("minutes", DBType.INTEGER),
        CHAPTER_LIST("chapter_list", DBType.ID_LIST),
        SEASON("season", DBType.INTEGER),
        NUMBER("number", DBType.INTEGER),
        NAME("name", DBType.TEXT),
        HASH("hash", DBType.TEXT),
        LENGTH("length", DBType.LONG),
        ADDITIONAL_SOURCES("additional_sources", DBType.STRING_LIST),
        RESOLUTION("resolution", DBType.INTEGER),
        QUALITY_CODE("quality_code", DBType.QUALITY),
        LANGUAGE("language", DBType.LANGUAGE),
        LOCALIZED_LANGUAGE("localized_language", DBType.LOCALIZED_LANGUAGE),
        LOCALIZED_LANGUAGE_LIST("localized_language_list", DBType.LOCALIZED_LANGUAGE_LIST),
        // todo remove
        LANGUAGES("languages", DBType.LANGUAGE_LIST),
        SUBTITLE_FILE_LIST("subtitle_file_list", DBType.ID_LIST);

        public final String value;

        public final DBType dbType;

        Field(String value, DBType dbType) {
            this.value = value;
            this.dbType = dbType;
        }

        boolean canBeReset() {
            return this != DatabaseMediator.Field.ID &&
                    this != DatabaseMediator.Field.CREATION_DATE &&
                    this != DatabaseMediator.Field.TIMESTAMP;
        }

        boolean canBeCompared() {
            return this != DatabaseMediator.Field.ID &&
                    this != DatabaseMediator.Field.VERSION &&
                    this != DatabaseMediator.Field.IDENTIFIER &&
                    this != DatabaseMediator.Field.CREATION_DATE &&
                    this != DatabaseMediator.Field.LAST_UPDATE &&
                    this != DatabaseMediator.Field.NEXT_TIMESTAMP &&
                    this != DatabaseMediator.Field.HIGHEST_MANUAL_TIMESTAMP;
        }

        ItemType getReferencedType() {
            switch (this) {

                case VIDEO_FILE_LIST:
                    return ItemType.VIDEO_FILE;
                case CHAPTER_LIST:
                    return ItemType.CHAPTER;
                case SUBTITLE_FILE_LIST:
                    return ItemType.SUBTITLE_FILE;
                default:
                    // todo fatal error
                    return null;
            }
        }

        ReferencedList getReferencedList() {
            switch (this) {

                case CREATOR_LIST:
                    return ReferencedList.CREATORS;
                case ACTOR_LIST:
                    return ReferencedList.ACTORS;
                case COMPANY_LIST:
                    return ReferencedList.COMPANIES;
                case VIDEO_FILE_LIST:
                    return ReferencedList.CHAPTERS;
                case CHAPTER_LIST:
                    return ReferencedList.VIDEO_FILES;
                case SUBTITLE_FILE_LIST:
                    return ReferencedList.SUBTITLE_FILES;
                default:
                    // todo fatal error
                    return null;
            }
        }
    }

    enum DBType {
        ID("INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"),
        TEXT("TEXT"),
        INTEGER("INTEGER"),
        LONG("INTEGER"),
        DATE("INTEGER"),
        QUALITY("TEXT"),
        ID_LIST("TEXT"),
        STRING_LIST("TEXT"),
        GENRE_LIST("TEXT"),
        LANGUAGE("TEXT"),
        LOCALIZED_LANGUAGE("TEXT"),
        LOCALIZED_LANGUAGE_LIST("TEXT"),
        LANGUAGE_LIST("TEXT"),
        COUNTRY_LIST("TEXT");

        private final String value;

        DBType(String value) {
            this.value = value;
        }
    }

    public enum ReferencedList {
        CREATORS,
        ACTORS,
        COMPANIES,
        CHAPTERS,
        VIDEO_FILES,
        SUBTITLE_FILES
    }

    public static class ReferencedElements {

        private final Map<DatabaseMediator.ItemType, Map<DatabaseMediator.ReferencedList, List<Integer>>> referencedElements;

        public ReferencedElements() {
            referencedElements = new HashMap<>();
        }

        public void add(DatabaseMediator.ItemType type, DatabaseMediator.ReferencedList referencedList, List<Integer> ids) {
            if (!referencedElements.containsKey(type)) {
                referencedElements.put(type, new HashMap<>());
            }
            referencedElements.get(type).put(referencedList, ids);
        }

        public List<Integer> get(DatabaseMediator.ItemType type, DatabaseMediator.ReferencedList referencedList) {
            return referencedElements.get(type).get(referencedList);
        }

        public void mapIds(Map<DatabaseMediator.ItemType, Map<Integer, Integer>> typeMappings) {
            for (Map.Entry<DatabaseMediator.ItemType, Map<DatabaseMediator.ReferencedList, List<Integer>>> referencedElementsEntry : referencedElements.entrySet()) {
                Map<Integer, Integer> mapping = typeMappings.get(referencedElementsEntry.getKey());
                for (List<Integer> idList : referencedElementsEntry.getValue().values()) {
                    if (mapping != null) {
                        // we have a mapping for this item type. only the elements contained in the mapping are mapped. The rest are removed
                        int i = 0;
                        while (i < idList.size()) {
                            if (mapping.containsKey(idList.get(i))) {
                                idList.set(i, mapping.get(idList.get(i)));
                                i++;
                            } else {
                                idList.remove(i);
                            }
                        }
                    } else {
                        // there is no mapping at all for this item type -> clear all lists
                        idList.clear();
                    }
                }
            }
        }
    }

    public static final String DATABASE_NAME = "jczMediaDatabase";

    static final String LANGUAGE_NAME_METHOD = "name";

    static final String COUNTRY_NAME_METHOD = "getAlpha2";

    static final String QUALITY_NAME_METHOD = "name";



    private static final String VERSION_0_1 = "0.1";

    public static final String CURRENT_VERSION = VERSION_0_1;

    private static final Map<String, ItemType> tableNameToItemType = new HashMap<>();

    private static ErrorHandler errorHandler = null;

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
            ActiveJDBCController.getDB().exec("DROP TABLE IF EXISTS " + itemType.table);
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
        createTable(ItemType.VIDEO_FILE);
        createTable(ItemType.SUBTITLE_FILE);
        createTable(ItemType.TAG);

        long now = System.currentTimeMillis();
        new Metadata()
                .set(Field.VERSION.value, version)
                .set(Field.IDENTIFIER.value, identifier)
                .set(Field.CREATION_DATE.value, now)
                .set(Field.LAST_UPDATE.value, now)
                .set(Field.NEXT_TIMESTAMP.value, 1L)
                .saveIt();

        disconnect(path);
    }

    private static void createTable(ItemType itemType) {
        StringBuilder create = new StringBuilder("CREATE TABLE ").append(itemType.table).append("(");
        for (Field field : itemType.fields) {
            create.append(field.value).append(" ").append(field.dbType.value).append(",");
        }
        // replace last comma with a ')'
        create.replace(create.length() - 1, create.length(), ")");
        ActiveJDBCController.getDB().exec(create.toString());
    }

    public static DatabaseItem createNewItem(String dbPath, ItemType type) {
        switch (type) {

            case MOVIE:
                return new jacz.database.Movie(dbPath);

            case TV_SERIES:
                return new jacz.database.TVSeries(dbPath);

            case CHAPTER:
                return new jacz.database.Chapter(dbPath);

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

            case VIDEO_FILE:
                return jacz.database.VideoFile.getVideoFileById(dbPath, id);

            case SUBTITLE_FILE:
                return jacz.database.SubtitleFile.getSubtitleFileById(dbPath, id);

            default:
                throw new IllegalArgumentException("Cannot build items of type " + type.name());
        }
    }

    public static Set<ImageHash> getAllImageHashes(String dbPath) {
        connect(dbPath);
        Set<ImageHash> imageHashes = new HashSet<>();
        List<ProducedCreationItem> producedCreationItems = new ArrayList<>();
        producedCreationItems.addAll(jacz.database.Movie.getMovies(dbPath));
        producedCreationItems.addAll(jacz.database.TVSeries.getTVSeries(dbPath));
        for (ProducedCreationItem producedCreationItem : producedCreationItems) {
            if (producedCreationItem.getImageHash() != null) {
                imageHashes.add(producedCreationItem.getImageHash());
            }
        }
        disconnect(dbPath);
        return imageHashes;
    }

    public static String getDatabaseIdentifier(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        disconnect(dbPath);
        return metadata.getString(Field.IDENTIFIER.value);
    }

    public static void updateLastUpdateTime(String dbPath) {
        connect(dbPath);
        Metadata metadata = getMetadata();
        metadata.set(Field.LAST_UPDATE.value, System.currentTimeMillis()).saveIt();
        disconnect(dbPath);
    }

    public static long getNewTimestamp(String dbPath) {
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

    public static Long getHighestManualTimestamp(String dbPath) {
        connect(dbPath);
        Long highestTimestamp;
        // synchronized blocks must be inside connect blocks to avoid cases of interlocks
        synchronized (DatabaseMediator.class) {
            Metadata metadata = getMetadata();
            highestTimestamp = metadata.getLong(Field.HIGHEST_MANUAL_TIMESTAMP.value);
        }
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
        ActiveJDBCController.connect(DATABASE_NAME, dbPath);
    }

    public static void disconnect(String dbPath) {
        ActiveJDBCController.disconnect();
    }

    public static String getDBPath() {
        try {
            String url = ActiveJDBCController.getDB().connection().getMetaData().getURL();
            return url.substring(url.indexOf(':') + 1);
        } catch (SQLException e) {
            reportError("Error obtaining the database path");
            return null;
        }
    }

    public static void checkConsistency(String dbPath) {
        deleteOrphans(jacz.database.Chapter.getChapters(dbPath));
        deleteOrphans(jacz.database.VideoFile.getVideoFiles(dbPath));
        deleteOrphans(jacz.database.SubtitleFile.getSubtitleFiles(dbPath));
    }

    private static void deleteOrphans(List<? extends DatabaseItem> items) {
        for (DatabaseItem item : items) {
            if (item.isOrphan()) {
                item.delete();
            }
        }
    }

    public static void registerErrorHandler(ErrorHandler errorHandler) {
        DatabaseMediator.errorHandler = errorHandler;
    }

    public static void reportError(String message, Object... data) {
        if (errorHandler != null) {
            ErrorFactory.reportError(errorHandler, message, data);
        } else {
            ErrorLog.reportError("DATABASE_API", message, data);
        }
    }
}