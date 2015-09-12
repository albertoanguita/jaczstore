package jacz.store.old2.common;

import jacz.store.old2.ItemInterface;
import jacz.store.old2.Libraries;
import jacz.store.old2.db_mediator.CorruptDataException;
import jacz.store.old2.db_mediator.DBException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * An artistic creation (movie, song, book...) with a title, year of creation and a creator
 */
public abstract class Creation extends TaggedLibraryItemWithImages {

    public static final String TITLE = "title";

    public static final String ORIGINAL_TITLE = "originalTitle";

    public static final String YEAR = "year";

    public static final String CREATORS = "creators";

    public static final String OWNER_COMPANIES = "ownerCompanies";

    public static final String WRITERS = "writers";

    public static final String PRODUCERS = "producers";

    /**
     * Title of this creation
     */
    private String title;

    /**
     * Original title (in its original language)
     */
    private String originalTitle;

    /**
     * Year of creation
     */
    private Integer year;

    /**
     * The creators leading this creation (a movie director, a music group, a games company, a book writer or main person in charge).
     */
    private List<String> creators;

    /**
     * The company/label/editorial that has the rights of this creation (e.g. Elsevier, BMG, HBO, Columbia...)
     */
    private List<String> ownerCompanies;

    /**
     * The persons that actually wrote this creation (movie writer, song writer, book additional writers)
     */
    private List<String> writers;

    /**
     * Persons that produced/edited this creation (a movie producer, a book editor, a song producer)
     */
    private List<String> producers;

    protected Creation(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(container, itemInterface, values, itemIsNew);
    }

    public List<String> getFields() {
        List<String> fields = super.getFields();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = TaggedLibraryItemWithImages.getFieldsStatic();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(TITLE);
        fields.add(ORIGINAL_TITLE);
        fields.add(YEAR);
        fields.add(CREATORS);
        fields.add(OWNER_COMPANIES);
        fields.add(WRITERS);
        fields.add(PRODUCERS);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        title = null;
        originalTitle = null;
        year = null;
        creators = new ArrayList<>();
        ownerCompanies = new ArrayList<>();
        writers = new ArrayList<>();
        producers = new ArrayList<>();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        title = initializeValueString(TITLE, values);
        originalTitle = initializeValueString(ORIGINAL_TITLE, values);
        year = initializeValueInteger(YEAR, values);
        creators = initializeValueStringList(CREATORS, values);
        ownerCompanies = initializeValueStringList(OWNER_COMPANIES, values);
        writers = initializeValueStringList(WRITERS, values);
        producers = initializeValueStringList(PRODUCERS, values);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws DBException, IOException {
        this.title = updateField(this.title, title, TITLE);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) throws DBException, IOException {
        this.originalTitle = updateField(this.originalTitle, originalTitle, ORIGINAL_TITLE);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) throws DBException, IOException {
        this.year = updateField(this.year, year, YEAR);
    }

    public List<String> getCreators() {
        return creators;
    }

    public void addCreator(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        creators = addElementWithoutRepetition(creators, index, element, CREATORS);
    }

    public void removeCreator(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(creators, index, CREATORS);
    }

    public void setCreators(List<String> creators) throws DBException, IOException {
        this.creators = updateList(this.creators, creators, CREATORS);
    }

    public List<String> getOwnerCompanies() {
        return ownerCompanies;
    }

    public void addOwnerCompany(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        ownerCompanies = addElementWithoutRepetition(ownerCompanies, index, element, OWNER_COMPANIES);
    }

    public void removeOwnerCompany(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(ownerCompanies, index, OWNER_COMPANIES);
    }

    public void setOwnerCompanies(List<String> ownerCompanies) throws DBException, IOException {
        this.ownerCompanies = updateList(this.ownerCompanies, ownerCompanies, OWNER_COMPANIES);
    }

    public List<String> getWriters() {
        return writers;
    }

    public void addWriter(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        writers = addElementWithoutRepetition(writers, index, element, WRITERS);
    }

    public void removeWriter(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(writers, index, WRITERS);
    }

    public void setWriters(List<String> writers) throws DBException, IOException {
        this.writers = updateList(this.writers, writers, WRITERS);
    }

    public List<String> getProducers() {
        return producers;
    }

    public void addProducer(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        producers = addElementWithoutRepetition(producers, index, element, PRODUCERS);
    }

    public void removeProducer(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(producers, index, PRODUCERS);
    }

    public void setProducers(List<String> producers) throws DBException, IOException {
        this.producers = updateList(this.producers, producers, PRODUCERS);
    }

    public Map<String, Set<String>> getCompaniesGroupsPersons() throws DBException, CorruptDataException, ParseException, IOException {
        Map<String, Set<String>> result = new HashMap<>();
        result.put(Libraries.COMPANY_CREATOR_LIBRARY, new HashSet<String>());
        result.put(Libraries.GROUP_CREATOR_LIBRARY, new HashSet<String>());
        result.put(Libraries.PERSON_LIBRARY, new HashSet<String>());
        for (String creatorId : getCreators()) {
            if (itemInterface.getItem(Libraries.PERSON_LIBRARY, creatorId) != null) {
                result.get(Libraries.PERSON_LIBRARY).add(creatorId);
            } else if (itemInterface.getItem(Libraries.COMPANY_CREATOR_LIBRARY, creatorId) != null) {
                result.get(Libraries.GROUP_CREATOR_LIBRARY).add(creatorId);
            } else if (itemInterface.getItem(Libraries.COMPANY_CREATOR_LIBRARY, creatorId) != null) {
                result.get(Libraries.COMPANY_CREATOR_LIBRARY).add(creatorId);
            }
        }
        result.get(Libraries.GROUP_CREATOR_LIBRARY).addAll(getOwnerCompanies());
        result.get(Libraries.PERSON_LIBRARY).addAll(getWriters());
        result.get(Libraries.PERSON_LIBRARY).addAll(getProducers());
        return result;
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case TITLE:
                return getTitle();

            case ORIGINAL_TITLE:
                return getOriginalTitle();

            case YEAR:
                return getYear();

            case CREATORS:
                return getCreators();

            case OWNER_COMPANIES:
                return getOwnerCompanies();

            case WRITERS:
                return getWriters();

            case PRODUCERS:
                return getProducers();

            default:
                return super.get(field);
        }
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        if (!(anotherItem instanceof Creation)) {
            return -1f;
        }
        Creation anotherCreation = (Creation) anotherItem;

        return super.match(anotherCreation);
    }

    @Override
    public void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof Creation)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        Creation o = (Creation) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setTitle(mergeElement(title, o.title));
        setOriginalTitle(mergeElement(originalTitle, o.originalTitle));
        setYear(mergeElement(year, o.year));
        setCreators(mergeList(creators, o.creators));
        setOwnerCompanies(mergeList(ownerCompanies, o.ownerCompanies));
        setWriters(mergeList(writers, o.writers));
        setProducers(mergeList(producers, o.producers));
    }
}
