package jacz.store.common;

import jacz.store.ItemInterface;
import jacz.store.db_mediator.DBException;
import jacz.util.hash.HashFunction;
import jacz.util.hash.SHA_256;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * The super class for any library item. Contains the basic fields: identifier, creationDate and modificationDate
 */
public abstract class LibraryItem implements Serializable {


    public static final String IDENTIFIER = "identifier";

    public static final String CREATION_DATE = "creationDate";

    public static final String MODIFICATION_DATE = "modificationDate";

    /**
     * Name of the container or library that stores this item
     */
    private final String container;

    /**
     * Interface for accessing the database
     */
    protected final ItemInterface itemInterface;

    /**
     * Item identifier. Must be unique among any other library item. Must be always set
     */
    private final String identifier;

    /**
     * Date of creation of this item
     */
    private final Date creationDate;

    /**
     * Date of last modification of this item
     */
    private Date modificationDate;

    public LibraryItem(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        this.container = container;
        this.itemInterface = itemInterface;
        identifier = values.get(IDENTIFIER);
        if (itemIsNew) {
            creationDate = new Date();
            modificationDate = creationDate;
            values.put(CREATION_DATE, itemInterface.dateToString(creationDate));
            values.put(MODIFICATION_DATE, itemInterface.dateToString(modificationDate));
            initValues();
        } else {
            creationDate = initializeValueDate(CREATION_DATE, values);
            modificationDate = initializeValueDate(MODIFICATION_DATE, values);
            loadValues(values);
        }
    }

    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = new ArrayList<>();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(IDENTIFIER);
        fields.add(CREATION_DATE);
        fields.add(MODIFICATION_DATE);
        return fields;
    }

    protected abstract void initValues();

    protected abstract void loadValues(Map<String, String> values) throws ParseException;

//    protected List<String> initializeValueListIdentifier(String field, Map<String, String> values) throws ParseException {
//        String s = initializeValue(field, values);
//        return s != null ? itemInterface.stringToList(s) : null;
//    }

    protected String initializeValueString(String field, Map<String, String> values) throws ParseException {
        return initializeValue(field, values);
    }

    protected Date initializeValueDate(String field, Map<String, String> values) throws ParseException {
        String s = initializeValue(field, values);
        return s != null ? itemInterface.stringToDate(s) : null;
    }

    protected Integer initializeValueInteger(String field, Map<String, String> values) throws ParseException {
        String s = initializeValue(field, values);
        return s != null ? Integer.parseInt(s) : null;
    }

    protected Long initializeValueLong(String field, Map<String, String> values) throws ParseException {
        String s = initializeValue(field, values);
        return s != null ? Long.parseLong(s) : null;
    }

    protected List<String> initializeValueStringList(String field, Map<String, String> values) throws ParseException {
        String s = initializeValue(field, values);
//        if (s != null) {
            return itemInterface.stringToList(s);
//        } else {
//            throw new ParseException("null value for list", 0);
//        }
    }

//    protected List<AlbumSong> initializeValueAlbumSongList(String field, Map<String, String> values) throws ParseException {
//        // todo move to specific class
//        List<AlbumSong> albumSongList = new ArrayList<>();
//        String s = initializeValue(field, values);
////        if (s != null) {
//            List<String> stringList = itemInterface.stringToList(s);
//            for (String str : stringList) {
//                List<String> albumSongElements = itemInterface.stringToList(str);
//                albumSongList.add(new AlbumSong(albumSongElements.get(0), albumSongElements.get(1), albumSongElements.get(2)));
//            }
////        } else {
////            throw new ParseException("null value for list", 0);
////        }
//        return albumSongList;
//    }

    protected <T> T initializeValueCustomElement(String field, Map<String, String> values, CustomElementLoader<T> customElementLoader) throws ParseException {
        String s = initializeValue(field, values);
        return customElementLoader.loadElement(s);
    }

    protected <T> List<T> initializeValueCustomElementList(String field, Map<String, String> values, CustomElementLoader<T> customElementLoader) throws ParseException {
        List<T> elementList = new ArrayList<>();
        String s = initializeValue(field, values);
        for (String str : itemInterface.stringToList(s)) {
            elementList.add(customElementLoader.loadElement(str));
        }
        return elementList;
    }

    private String initializeValue(String field, Map<String, String> values) throws ParseException {
        if (values.containsKey(field)) {
            return values.get(field);
        } else {
            return null;
        }
    }

    public final String getIdentifier() {
        return identifier;
    }

    public final String getLibrary() {
        return container;
    }

    protected <T> T updateField(T oldValue, T newValue, String fieldName) throws DBException, IOException {
        if ((oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue))) {
            // value change
            updateField(fieldName, newValue.toString());
            return newValue;
        } else {
            // no change
            return oldValue;
        }
    }

    private void updateField(String field, String value) throws DBException, IOException {
        updateModificationDate();
        Map<String, String> fieldsAndValues = buildModificationDateMap();
        fieldsAndValues.put(field, value);
        itemInterface.updateFields(container, identifier, fieldsAndValues);
    }

    protected <T> List<T> updateList(List<T> oldList, List<T> newList, String fieldName) throws DBException, IOException {
        if (!oldList.equals(newList)) {
            oldList = newList;
            if (oldList == null) {
                oldList = new ArrayList<>();
            }
            updateList(fieldName, oldList);
        }
        return oldList;
    }

    protected <T> List<T> updateList(List<T> oldList, List<T> newList, String fieldName, CustomElementLoader<T> customElementLoader) throws DBException, IOException {
        if (!oldList.equals(newList)) {
            oldList = newList;
            if (oldList == null) {
                oldList = new ArrayList<>();
            }
            updateList(fieldName, oldList, customElementLoader);
        }
        return oldList;
    }

    protected <T> List<T> addElementWithRepetition(List<T> elementList, int index, T element, String fieldName) throws IndexOutOfBoundsException, DBException, IOException {
        if (elementList == null) {
            elementList = new ArrayList<>();
        }
        elementList.add(index, element);
        updateList(fieldName, elementList);
        return elementList;
    }

    protected <T> List<T> addElementWithRepetition(List<T> elementList, int index, T element, String fieldName, CustomElementLoader<T> customElementLoader) throws IndexOutOfBoundsException, DBException, IOException {
        if (elementList == null) {
            elementList = new ArrayList<>();
        }
        elementList.add(index, element);
        updateList(fieldName, elementList, customElementLoader);
        return elementList;
    }

    protected <T> List<T> addElementWithoutRepetition(List<T> elementList, int index, T element, String fieldName) throws IndexOutOfBoundsException, DBException, IOException {
        if (elementList == null) {
            elementList = new ArrayList<>();
        }
        if (!elementList.contains(element)) {
            elementList.add(index, element);
            updateList(fieldName, elementList);
        }
        return elementList;
    }

    protected <T> List<T> addElementWithoutRepetition(List<T> elementList, int index, T element, String fieldName, CustomElementLoader<T> customElementLoader) throws IndexOutOfBoundsException, DBException, IOException {
        if (elementList == null) {
            elementList = new ArrayList<>();
        }
        if (!elementList.contains(element)) {
            elementList.add(index, element);
            updateList(fieldName, elementList, customElementLoader);
        }
        return elementList;
    }

    protected <T> void removeElement(List<T> elementList, int index, String fieldName) throws IndexOutOfBoundsException, DBException, IOException {
        if (elementList == null) {
            throw new IndexOutOfBoundsException("List is null");
        }
        elementList.remove(index);
        updateList(fieldName, elementList);
    }

    protected <T> void removeElement(List<T> elementList, int index, String fieldName, CustomElementLoader<T> customElementLoader) throws IndexOutOfBoundsException, DBException, IOException {
        if (elementList == null) {
            throw new IndexOutOfBoundsException("List is null");
        }
        elementList.remove(index);
        updateList(fieldName, elementList, customElementLoader);
    }

    private void updateList(String field, List<?> values) throws DBException, IOException {
        updateModificationDate();
        Map<String, String> fieldsAndValues = buildModificationDateMap();
        fieldsAndValues.put(field, itemInterface.listToString(values));
        itemInterface.updateFields(container, identifier, fieldsAndValues);
    }

    private <T> void updateList(String field, List<T> values, CustomElementLoader<T> customElementLoader) throws DBException, IOException {
        List<String> stringList = new ArrayList<>();
        for (T element : values) {
            stringList.add(customElementLoader.saveElement(element));
        }
        updateList(field, stringList);
    }

//    protected List<AlbumSong> updateAlbumSongList(List<AlbumSong> oldList, List<AlbumSong> newList, String fieldName) throws DBException, IOException {
//        if (!oldList.equals(newList)) {
//            oldList = newList;
//            if (oldList == null) {
//                oldList = new ArrayList<>();
//            }
//            updateAlbumSongList(fieldName, oldList);
//        }
//        return oldList;
//    }

//    protected List<AlbumSong> addAlbumSongWithRepetition(List<AlbumSong> elementList, int index, AlbumSong element, String fieldName) throws IndexOutOfBoundsException, DBException, IOException {
//        if (elementList == null) {
//            elementList = new ArrayList<>();
//        }
//        elementList.add(index, element);
//        updateAlbumSongList(fieldName, elementList);
//        return elementList;
//    }
//
//    protected void removeAlbumSong(List<AlbumSong> elementList, int index, String fieldName) throws IndexOutOfBoundsException, DBException, IOException {
//        if (elementList == null) {
//            throw new IndexOutOfBoundsException("List is null");
//        }
//        elementList.remove(index);
//        updateAlbumSongList(fieldName, elementList);
//    }
//
//    private void updateAlbumSongList(String field, List<AlbumSong> values) throws DBException, IOException {
//        updateModificationDate();
//        Map<String, String> fieldsAndValues = buildModificationDateMap();
//        List<String> albumSongValues = new ArrayList<>();
//        for (AlbumSong albumSong : values) {
//            List<String> oneAlbumSongList = new ArrayList<>();
//            oneAlbumSongList.add(albumSong.songFile);
//            oneAlbumSongList.add(albumSong.songTitle);
//            oneAlbumSongList.add(albumSong.songItem);
//            albumSongValues.add(itemInterface.listToString(oneAlbumSongList));
//        }
//        fieldsAndValues.put(field, itemInterface.listToString(albumSongValues));
//        itemInterface.updateFields(container, identifier, fieldsAndValues);
//    }

    private Map<String, String> buildModificationDateMap() {
        Map<String, String> fieldsAndValues = new HashMap<>();
        fieldsAndValues.put("modificationDate", itemInterface.dateToString(modificationDate));
        return fieldsAndValues;
    }

    private void updateModificationDate() {
        modificationDate = new Date();
    }

    protected void updateModificationDateAndWrite() throws DBException, IOException {
        updateModificationDate();
        Map<String, String> fieldsAndValues = buildModificationDateMap();
        itemInterface.updateFields(container, identifier, fieldsAndValues);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public Object get(String field) {
        switch (field) {
            case IDENTIFIER:
                return getIdentifier();

            case CREATION_DATE:
                return getCreationDate();

            case MODIFICATION_DATE:
                return getModificationDate();

            default:
                throw new IllegalArgumentException("Invalid field name: " + field);
        }
    }

    /**
     * deletes all underlying information
     */
    public final void reset() {
        initValues();
    }

    /**
     * Calculates a state hash with all underlying information. Identifier, creation date and modification date are not included
     *
     * @return a state hash
     */
    public final String getStateHash() {
        List<String> fields = getFields();
        String[] fieldArray = new String[fields.size()];
        return getStateHash(fields.toArray(fieldArray));
    }

    public final String getStateHash(String... fields) {
        HashFunction hashFunction = new SHA_256();
        for (String field : fields) {
            hashFunction.update(get(field));
        }
        return hashFunction.digestAsHex();
    }

    public final String getStateHash(String field) {
        HashFunction hashFunction = new SHA_256();
        hashFunction.update(get(field));
        return hashFunction.digestAsHex();
    }

    public final Map<String, String> getStateHashPerField() {
        List<String> fields = getFields();
        String[] fieldArray = new String[fields.size()];
        return getStateHashPerField(fields.toArray(fieldArray));
    }

    public final Map<String, String> getStateHashPerField(String... fields) {
        HashFunction hashFunction = new SHA_256();
        Map<String, String> hashMap = new HashMap<>();
        for (String field : fields) {
            hashMap.put(field, getStateHash(field));
        }
        return hashMap;
    }

    public Float match(LibraryItem anotherItem) {
        // no fields of this class indicate equality
        return 0f;
    }

    public void merge(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        String oldHash = getStateHash();
        mergeData(anotherItem, identifierMap);
        if (!oldHash.equals(getStateHash())) {
            updateModificationDateAndWrite();
        }
    }

    protected abstract void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException;

    public void merge(LibraryItem anotherItem) {
        // todo remove
    }

    protected static <T> T mergeElement(T baseElement, T anotherElement) {
        return baseElement != null ? baseElement : anotherElement;
    }

    protected static <T> List<T> mergeList(List<T> baseList, List<T> anotherList) {
        List<T> mergedList = new ArrayList<>(baseList);
        for (T anotherElement : anotherList) {
            if (!baseList.contains(anotherElement)) {
                mergedList.add(anotherElement);
            }
        }
        return mergedList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibraryItem)) return false;

        LibraryItem that = (LibraryItem) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
