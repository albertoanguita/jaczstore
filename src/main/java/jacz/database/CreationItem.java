package jacz.database;

import com.neovisionaries.i18n.CountryCode;
import jacz.database.util.ItemIntegrator;
import jacz.util.AI.inference.Mycin;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class CreationItem extends DatabaseItem {

//    private String title;
//
//    private String originalTitle;
//
//    private Integer year;
//
//    private List<Person> creatorsDirectors;

//    private List<Person> actors;

//    private List<CountryCode> countries;

//    private List<String> externalURLs;

    private static final String COUNTRIES_NAME_METHOD = "getAlpha2";

    private static final float COUNTRIES_SIMILARITY_CONFIDENCE = 0.1f;

    private static final float CREATORS_SIMILARITY_CONFIDENCE = 0.7f;

    private static final float ACTORS_SIMILARITY_CONFIDENCE = 0.9f;


    public CreationItem(String dbPath) {
        super(dbPath);
    }

    public CreationItem(String dbPath, String title) {
        super(dbPath, buildInitialValues(title));
    }

    private static Map<DatabaseMediator.Field, Object> buildInitialValues(String title) {
        Map<DatabaseMediator.Field, Object> initialValues = new HashMap<>();
        initialValues.put(DatabaseMediator.Field.TITLE, title);
        return  initialValues;
    }

    public CreationItem(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public CreationItem(Model model, String dbPath) {
        super(model, dbPath);
    }

    public String getTitle() {
        return getString(DatabaseMediator.Field.TITLE);
    }

    public void setTitle(String title) {
        set(DatabaseMediator.Field.TITLE, title, true);
    }

    public void setTitlePostponed(String title) {
        set(DatabaseMediator.Field.TITLE, title, false);
    }

    public String getOriginalTitle() {
        return getString(DatabaseMediator.Field.ORIGINAL_TITLE);
    }

    public void setOriginalTitle(String originalTitle) {
        set(DatabaseMediator.Field.ORIGINAL_TITLE, originalTitle, true);
    }

    public void setOriginalTitlePostponed(String originalTitle) {
        set(DatabaseMediator.Field.ORIGINAL_TITLE, originalTitle, false);
    }

    public Integer getYear() {
        return getInteger(DatabaseMediator.Field.YEAR);
    }

    public void setYear(Integer year) {
        set(DatabaseMediator.Field.YEAR, year, true);
    }

    public void setYearPostponed(Integer year) {
        set(DatabaseMediator.Field.YEAR, year, false);
    }

    public String getSynopsis() {
        return getString(DatabaseMediator.Field.SYNOPSIS);
    }

    public void setSynopsis(String synopsis) {
        set(DatabaseMediator.Field.SYNOPSIS, synopsis, true);
    }

    public void setSynopsisPostponed(String synopsis) {
        set(DatabaseMediator.Field.SYNOPSIS, synopsis, false);
    }

    public List<CountryCode> getCountries() {
        return getEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class);
    }

    public void removeCountries() {
        removeList(DatabaseMediator.Field.COUNTRIES, true);
    }

    public void removeCountriesPostponed() {
        removeList(DatabaseMediator.Field.COUNTRIES, false);
    }

    public boolean removeCountry(CountryCode country) {
        return removeEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, COUNTRIES_NAME_METHOD, true);
    }

    public boolean removeCountryPostponed(CountryCode country) {
        return removeEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, COUNTRIES_NAME_METHOD, false);
    }

    public void setCountries(List<CountryCode> countries) {
        setEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class, countries, COUNTRIES_NAME_METHOD, true);
    }

    public void setCountriesPostponed(List<CountryCode> countries) {
        setEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class, countries, COUNTRIES_NAME_METHOD, false);
    }

    public boolean addCountry(CountryCode country) {
        return addEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, COUNTRIES_NAME_METHOD, true);
    }

    public boolean addCountryPostponed(CountryCode country) {
        return addEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, COUNTRIES_NAME_METHOD, false);
    }

    public List<String> getExternalURLs() {
        return getStringList(DatabaseMediator.Field.EXTERNAL_URLS);
    }

    public void removeExternalURLs() {
        removeStringList(DatabaseMediator.Field.EXTERNAL_URLS, true);
    }

    public void removeExternalURLsPostponed() {
        removeStringList(DatabaseMediator.Field.EXTERNAL_URLS, false);
    }

    public boolean removeExternalURL(String externalURL) {
        return removeStringValue(DatabaseMediator.Field.EXTERNAL_URLS, externalURL, true);
    }

    public boolean removeExternalURLPostponed(String externalURL) {
        return removeStringValue(DatabaseMediator.Field.EXTERNAL_URLS, externalURL, false);
    }

    public void setExternalURLs(List<String> externalURLs) {
        setStringList(DatabaseMediator.Field.EXTERNAL_URLS, externalURLs, true);
    }

    public void setExternalURLsPostponed(List<String> externalURLs) {
        setStringList(DatabaseMediator.Field.EXTERNAL_URLS, externalURLs, false);
    }

    public boolean addExternalURL(String externalURL) {
        return addStringValue(DatabaseMediator.Field.EXTERNAL_URLS, externalURL, true);
    }

    public boolean addExternalURLPostponed(String externalURL) {
        return addStringValue(DatabaseMediator.Field.EXTERNAL_URLS, externalURL, false);
    }

    public List<String> getCreators() {
        return getStringList(DatabaseMediator.Field.CREATOR_LIST);
    }

    public void removeCreators() {
        removeStringList(DatabaseMediator.Field.CREATOR_LIST, true);
    }

    public void removeCreatorsPostponed() {
        removeStringList(DatabaseMediator.Field.CREATOR_LIST, false);
    }

    public boolean removeCreator(String creator) {
        return removeStringValue(DatabaseMediator.Field.CREATOR_LIST, creator, true);
    }

    public boolean removeCreatorPostponed(String creator) {
        return removeStringValue(DatabaseMediator.Field.CREATOR_LIST, creator, false);
    }

    public void setCreators(List<String> creators) {
        setStringList(DatabaseMediator.Field.CREATOR_LIST, creators, true);
    }

    public void setCreatorsPostponed(List<String> creators) {
        setStringList(DatabaseMediator.Field.CREATOR_LIST, creators, false);
    }

    public boolean addCreator(String creator) {
        return addStringValue(DatabaseMediator.Field.CREATOR_LIST, creator, true);
    }

    public boolean addCreatorPostponed(String creator) {
        return addStringValue(DatabaseMediator.Field.CREATOR_LIST, creator, false);
    }

//    public List<Person> getCreators() {
//        LazyList<jacz.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.CREATOR_LIST);
//        if (models != null) {
//            return Person.buildList(dbPath, models);
//        } else {
//            return new ArrayList<>();
//        }
//    }
//
//    public List<Integer> getCreatorsIds() {
//        return getReferencedElementsIds(DatabaseMediator.Field.CREATOR_LIST);
//    }
//
//    public <C extends Model> void removeCreators() {
//        removeReferencedElements(DatabaseMediator.Field.CREATOR_LIST, true);
//    }
//
//    public <C extends Model> void removeCreatorsPostponed() {
//        removeReferencedElements(DatabaseMediator.Field.CREATOR_LIST, false);
//    }
//
//    public <C extends Model> void removeCreator(Person person) {
//        removeReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, true);
//    }
//
//    public <C extends Model> void removeCreatorPostponed(Person person) {
//        removeReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, false);
//    }
//
//    public void setCreators(List<Person> persons) {
//        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, persons, true);
//    }
//
//    public void setCreatorsPostponed(List<Person> persons) {
//        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, persons, false);
//    }
//
//    public void setCreatorsIds(List<Integer> persons) {
//        setReferencedElementsIds(DatabaseMediator.Field.CREATOR_LIST, persons, true);
//    }
//
//    public void setCreatorsIdsPostponed(List<Integer> persons) {
//        setReferencedElementsIds(DatabaseMediator.Field.CREATOR_LIST, persons, false);
//    }
//
//    public void setCreators(Person... persons) {
//        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, true, persons);
//    }
//
//    public void setCreatorsPostponed(Person... persons) {
//        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, false, persons);
//    }
//
//    public <C extends Model> void addCreator(Person person) {
//        addReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, true);
//    }
//
//    public <C extends Model> void addCreatorPostponed(Person person) {
//        addReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, false);
//    }

//    public List<Person> getActors() {
//        LazyList<jacz.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.ACTOR_LIST);
//        if (models != null) {
//            return Person.buildList(dbPath, models);
//        } else {
//            return new ArrayList<>();
//        }
//    }
//
//    public List<Integer> getActorsIds() {
//        return getReferencedElementsIds(DatabaseMediator.Field.ACTOR_LIST);
//    }
//
//    public <C extends Model> void removeActors() {
//        removeReferencedElements(DatabaseMediator.Field.ACTOR_LIST, true);
//    }
//
//    public <C extends Model> void removeActorsPostponed() {
//        removeReferencedElements(DatabaseMediator.Field.ACTOR_LIST, false);
//    }
//
//    public <C extends Model> void removeActor(Person person) {
//        removeReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, true);
//    }
//
//    public <C extends Model> void removeActorPostponed(Person person) {
//        removeReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, false);
//    }
//
//    public void setActors(List<Person> persons) {
//        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, persons, true);
//    }
//
//    public void setActorsPostponed(List<Person> persons) {
//        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, persons, false);
//    }
//
//    public void setActorsIds(List<Integer> persons) {
//        setReferencedElementsIds(DatabaseMediator.Field.ACTOR_LIST, persons, true);
//    }
//
//    public void setActorsIdsPostponed(List<Integer> persons) {
//        setReferencedElementsIds(DatabaseMediator.Field.ACTOR_LIST, persons, false);
//    }
//
//    public void setActors(Person... persons) {
//        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, true, persons);
//    }
//
//    public void setActorsPostponed(Person... persons) {
//        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, false, persons);
//    }
//
//    public <C extends Model> void addActor(Person person) {
//        addReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, true);
//    }
//
//    public <C extends Model> void addActorPostponed(Person person) {
//        addReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, false);
//    }

    public List<String> getActors() {
        return getStringList(DatabaseMediator.Field.ACTOR_LIST);
    }

    public void removeActors() {
        removeStringList(DatabaseMediator.Field.ACTOR_LIST, true);
    }

    public void removeActorsPostponed() {
        removeStringList(DatabaseMediator.Field.ACTOR_LIST, false);
    }

    public boolean removeActor(String actor) {
        return removeStringValue(DatabaseMediator.Field.ACTOR_LIST, actor, true);
    }

    public boolean removeActorPostponed(String actor) {
        return removeStringValue(DatabaseMediator.Field.ACTOR_LIST, actor, false);
    }

    public void setActors(List<String> actors) {
        setStringList(DatabaseMediator.Field.ACTOR_LIST, actors, true);
    }

    public void setActorsPostponed(List<String> actors) {
        setStringList(DatabaseMediator.Field.ACTOR_LIST, actors, false);
    }

    public boolean addActor(String actor) {
        return addStringValue(DatabaseMediator.Field.ACTOR_LIST, actor, true);
    }

    public boolean addActorPostponed(String actor) {
        return addStringValue(DatabaseMediator.Field.ACTOR_LIST, actor, false);
    }



    @Override
    public float match(DatabaseItem anotherItem) {
        float similarity = super.match(anotherItem);
        CreationItem anotherCreationItem = (CreationItem) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.creationsTitleSimilarity(getTitle(), anotherCreationItem.getTitle(), getOriginalTitle(), anotherCreationItem.getOriginalTitle()));
        similarity = Mycin.combine(similarity, ItemIntegrator.eventsYearSimilarity(getYear(), anotherCreationItem.getYear()));
        similarity = ItemIntegrator.addListSimilarity(similarity, getCreators(), anotherCreationItem.getCreators(), CREATORS_SIMILARITY_CONFIDENCE);
        similarity = ItemIntegrator.addListSimilarity(similarity, getActors(), anotherCreationItem.getActors(), ACTORS_SIMILARITY_CONFIDENCE);
        similarity = ItemIntegrator.addListSimilarity(similarity, getCountries(), anotherCreationItem.getCountries(), COUNTRIES_SIMILARITY_CONFIDENCE);
//        similarity = ItemIntegrator.addListSimilarity(similarity, getCreatorsIds(), anotherCreationItem.getCreatorsIds(), CREATORS_SIMILARITY_CONFIDENCE);
//        similarity = ItemIntegrator.addListSimilarity(similarity, getActorsIds(), anotherCreationItem.getActorsIds(), ACTORS_SIMILARITY_CONFIDENCE);
        return similarity;
    }

    @Override
    public void mergeBasicPostponed(DatabaseItem anotherItem) {
        CreationItem anotherCreationItem = (CreationItem) anotherItem;
        if (getTitle() == null && anotherCreationItem.getTitle() != null) {
            setTitlePostponed(anotherCreationItem.getTitle());
        }
        if (getOriginalTitle() == null && anotherCreationItem.getOriginalTitle() != null) {
            setOriginalTitlePostponed(anotherCreationItem.getOriginalTitle());
        }
        if (getYear() == null && anotherCreationItem.getYear() != null) {
            setYearPostponed(anotherCreationItem.getYear());
        }
        if (getSynopsis() == null && anotherCreationItem.getSynopsis() != null) {
            setSynopsisPostponed(anotherCreationItem.getSynopsis());
        }
        for (String creator : anotherCreationItem.getCreators()) {
            addCreator(creator);
        }
        for (String actor : anotherCreationItem.getActors()) {
            addActor(actor);
        }
        for (CountryCode countryCode : anotherCreationItem.getCountries()) {
            addCountryPostponed(countryCode);
        }
        for (String externalURL : anotherCreationItem.getExternalURLs()) {
            addExternalURLPostponed(externalURL);
        }
    }

    @Override
    public DatabaseMediator.ReferencedElements getReferencedElements() {
        DatabaseMediator.ReferencedElements referencedElements = super.getReferencedElements();
        // todo
//        referencedElements.add(DatabaseMediator.ItemType.PERSON, DatabaseMediator.ReferencedList.CREATORS, getCreatorsIds());
//        referencedElements.add(DatabaseMediator.ItemType.PERSON, DatabaseMediator.ReferencedList.ACTORS, getActorsIds());
        return referencedElements;
    }

    @Override
    public void mergeReferencedElementsPostponed(DatabaseMediator.ReferencedElements referencedElements) {
//        for (Integer creatorId : referencedElements.get(DatabaseMediator.ItemType.PERSON, DatabaseMediator.ReferencedList.CREATORS)) {
//            addReferencedElementId(DatabaseMediator.Field.CREATOR_LIST, creatorId, false);
//        }
//        for (Integer actorId : referencedElements.get(DatabaseMediator.ItemType.PERSON, DatabaseMediator.ReferencedList.ACTORS)) {
//            addReferencedElementId(DatabaseMediator.Field.ACTOR_LIST, actorId, false);
//        }
    }
}
