package jacz.store;

import jacz.store.common.*;
import jacz.store.db_mediator.CorruptDataException;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;
import jacz.store.music.AudioAlbum;
import jacz.store.music.Song;
import jacz.util.lists.Duple;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo meter creation date a database
 */
public class Database {

    private static final String METADATA_CONTAINER = "Metadata";

    private static final String VERSION_FIELD = "version";

    private static final String IDENTIFIER_FACTORY_FIELD = "identifierFactory";

    private static final String CURRENT_VERSION = "a0.1";

    public final DBMediator dbMediator;

    private final IdentifierFactory identifierFactory;

    private final String initialDatabaseVersion;

    private final ItemInterface itemInterface;


    public Database(DBMediator dbMediator) throws IOException, DBException, CorruptDataException {
        this.dbMediator = dbMediator;
        dbMediator.connect();
        initialDatabaseVersion = readMetadataContainer(VERSION_FIELD);
        checkVersion();
        identifierFactory = IdentifierFactory.load(readMetadataContainer(IDENTIFIER_FACTORY_FIELD));
        itemInterface = new ItemInterface(this, dbMediator);
    }

    private void checkVersion() throws DBException, CorruptDataException {
        if (!CURRENT_VERSION.equals(initialDatabaseVersion)) {
            // must update given database
            // load the database with the previous version of class Database
            // todo
            System.out.println("updating database...");
        }
    }

    /**
     * Build a new empty database
     *
     * @param dbMediator mediator for accessing the database implementation
     */
    public static void createEmpty(DBMediator dbMediator) throws IOException, DBException, CorruptDataException {
        // create the database structure
        dbMediator.create(buildEmptyDatabaseContainerMap());
        dbMediator.connect();
        // add the metadata to the database
        Map<String, String> fieldsAndValues = new HashMap<>();
        fieldsAndValues.put(VERSION_FIELD, CURRENT_VERSION);
        fieldsAndValues.put(IDENTIFIER_FACTORY_FIELD, new IdentifierFactory().save());
        dbMediator.add(METADATA_CONTAINER, fieldsAndValues);
        dbMediator.disconnect();
    }

    private static Map<String, List<String>> buildEmptyDatabaseContainerMap() {
        Map<String, List<String>> containerMap = new HashMap<>();
        List<String> metadataFields = new ArrayList<>();
        metadataFields.add(VERSION_FIELD);
        metadataFields.add(IDENTIFIER_FACTORY_FIELD);
        containerMap.put(METADATA_CONTAINER, metadataFields);
        containerMap.put(Libraries.AUDIO_ALBUM_LIBRARY, AudioAlbum.getFieldsStatic());
        containerMap.put(Libraries.COMPANY_CREATOR_LIBRARY, CompanyCreator.getFieldsStatic());
        containerMap.put(Libraries.GROUP_CREATOR_LIBRARY, GroupCreator.getFieldsStatic());
        containerMap.put(Libraries.PERSON_LIBRARY, Person.getFieldsStatic());
        containerMap.put(Libraries.SONG_LIBRARY, Song.getFieldsStatic());
        containerMap.put(Libraries.TAG_LIBRARY, Tag.getFieldsStatic());
        return containerMap;
    }

    private String readMetadataContainer(String field) throws DBException, CorruptDataException {
        Duple<List<String>, List<List<String>>> dbSelect = dbMediator.select(METADATA_CONTAINER, new ArrayList<Filter>());
        if (dbSelect.element2.size() != 1) {
            throw new CorruptDataException("Metadata database is corrupted");
        }
        try {
            return dbSelect.element2.get(0).get(dbMediator.getColumnIndex(METADATA_CONTAINER, field));
        } catch (NullPointerException e) {
            throw new CorruptDataException("Metadata database is corrupted");
        }
    }

    public void close() throws IOException {
        dbMediator.disconnect();
    }

    public String getVersion() {
        return CURRENT_VERSION;
    }

    public String getInitialDatabaseVersion() {
        return initialDatabaseVersion;
    }

    public String getNewIdentifier() throws DBException, IOException {
        String identifier = identifierFactory.getOneIdentifier();
        writeIdentifierFactory();
        return identifier;
    }

    private void writeIdentifierFactory() throws DBException, IOException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(VERSION_FIELD, Filter.Operator.EQUALS, CURRENT_VERSION));
        Map<String, String> fieldsAndValues = new HashMap<>();
        fieldsAndValues.put(IDENTIFIER_FACTORY_FIELD, identifierFactory.save());
        dbMediator.set(METADATA_CONTAINER, filters, fieldsAndValues, true);
    }

    public ResultSet select(String library, List<Filter> filters) throws DBException, CorruptDataException, ParseException, IOException {
        Duple<List<String>, List<List<String>>> dbSelect = dbMediator.select(library, filters);
        List<LibraryItem> items = new ArrayList<>();
        for (List<String> row : dbSelect.element2) {
            items.add(loadItem(library, dbSelect.element1, row));
        }
        return new ResultSet(items);
    }

    public LibraryItem getItem(String library, String id) throws DBException, CorruptDataException, ParseException, IOException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(LibraryItem.IDENTIFIER, Filter.Operator.EQUALS, id));
        ResultSet resultSet = select(library, filters);
        if (resultSet.hasNext()) {
            return resultSet.next();
        } else {
            return null;
        }
    }

    public ResultSet getAllItems(String library) throws DBException, CorruptDataException, ParseException, IOException {
        return select(library, new ArrayList<Filter>());
    }

    public LibraryItem createNewItem(String library) throws ParseException, IOException, DBException {
        // build a record with a new identifier
        return createNewItem(library, getNewIdentifier());
    }

    public LibraryItem createNewItem(String library, String id) throws ParseException, IOException, DBException {
        // build a record with a specific id
        Map<String, String> record = new HashMap<>();
        record.put(LibraryItem.IDENTIFIER, id);
        LibraryItem item = buildItem(library, record, true);
        // the library item has added the additional fields
        dbMediator.add(library, record);
        return item;
    }

    private LibraryItem loadItem(String library, List<String> columns, List<String> row) throws ParseException, IOException, DBException {
        // creates an item with values extracted from the database
        return buildItem(library, buildRecord(columns, row), false);
    }

    private static Map<String, String> buildRecord(List<String> columns, List<String> row) {
        Map<String, String> record = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            record.put(columns.get(i), row.get(i));
        }
        return record;
    }

    private LibraryItem buildItem(String library, Map<String, String> record, boolean itemIsNew) throws ParseException, DBException, IOException {
        switch (library) {

            case Libraries.AUDIO_ALBUM_LIBRARY:
                return new AudioAlbum(itemInterface, record, itemIsNew);

            case Libraries.COMPANY_CREATOR_LIBRARY:
                return new CompanyCreator(itemInterface, record, itemIsNew);

            case Libraries.GROUP_CREATOR_LIBRARY:
                return new GroupCreator(itemInterface, record, itemIsNew);

            case Libraries.PERSON_LIBRARY:
                return new Person(itemInterface, record, itemIsNew);

            case Libraries.SONG_LIBRARY:
                return new Song(itemInterface, record, itemIsNew);

            case Libraries.TAG_LIBRARY:
                return new Tag(itemInterface, record, itemIsNew);

            default:
                throw new ParseException("Illegal library", -1);
        }
    }

    public synchronized void removeItem(String container, String identifier) throws DBException, IOException {
        List<Filter> filters = new ArrayList<>(1);
        filters.add(new Filter(LibraryItem.IDENTIFIER, Filter.Operator.EQUALS, identifier));
        dbMediator.remove(container, filters, true);
    }

    public static List<String> libraryIntegrationOrder() {
        List<String> libraryList = new ArrayList<>();
        libraryList.add(Libraries.TAG_LIBRARY);
        libraryList.add(Libraries.PERSON_LIBRARY);
        libraryList.add(Libraries.COMPANY_CREATOR_LIBRARY);
        libraryList.add(Libraries.GROUP_CREATOR_LIBRARY);
        libraryList.add(Libraries.SONG_LIBRARY);
        libraryList.add(Libraries.AUDIO_ALBUM_LIBRARY);
        return libraryList;
    }
}
