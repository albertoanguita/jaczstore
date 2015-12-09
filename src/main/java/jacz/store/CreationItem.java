package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database.DatabaseMediator;
import jacz.util.AI.inference.Mycin;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class CreationItem extends LibraryItem {

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

    public CreationItem(String dbPath) {
        super(dbPath);
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
        return removeEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, "getAlpha2", true);
    }

    public boolean removeCountryPostponed(CountryCode country) {
        return removeEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, "getAlpha2", false);
    }

    public void setCountries(List<CountryCode> countries) {
        setEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class, countries, "getAlpha2", true);
    }

    public void setCountriesPostponed(List<CountryCode> countries) {
        setEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class, countries, "getAlpha2", false);
    }

    public boolean addCountry(CountryCode country) {
        return addEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, "getAlpha2", true);
    }

    public boolean addCountryPostponed(CountryCode country) {
        return addEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, "getAlpha2", false);
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

    protected List<Person> getCreatorsDirectors() {
        LazyList<jacz.store.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.CREATOR_LIST);
        return Person.buildList(dbPath, models);
    }

    protected <C extends Model> void removeCreatorsDirectors() {
        removeReferencedElements(DatabaseMediator.Field.CREATOR_LIST, true);
    }

    protected <C extends Model> void removeCreatorsDirectorsPostponed() {
        removeReferencedElements(DatabaseMediator.Field.CREATOR_LIST, false);
    }

    protected <C extends Model> void removeCreatorDirector(Person person) {
        removeReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, true);
    }

    protected <C extends Model> void removeCreatorDirectorPostponed(Person person) {
        removeReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, false);
    }

    protected void setCreatorsDirectors(List<Person> persons) {
        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, persons, true);
    }

    protected void setCreatorsDirectorsPostponed(List<Person> persons) {
        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, persons, false);
    }

    protected void setCreatorsDirectors(Person... persons) {
        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, true, persons);
    }

    protected void setCreatorsDirectorsPostponed(Person... persons) {
        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, false, persons);
    }

    protected <C extends Model> void addCreatorDirector(Person person) {
        addReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, true);
    }

    protected <C extends Model> void addCreatorDirectorPostponed(Person person) {
        addReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person, false);
    }

    protected List<Person> getActors() {
        LazyList<jacz.store.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.ACTOR_LIST);
        return Person.buildList(dbPath, models);
    }

    public <C extends Model> void removeActors() {
        removeReferencedElements(DatabaseMediator.Field.ACTOR_LIST, true);
    }

    public <C extends Model> void removeActorsPostponed() {
        removeReferencedElements(DatabaseMediator.Field.ACTOR_LIST, false);
    }

    public <C extends Model> void removeActor(Person person) {
        removeReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, true);
    }

    public <C extends Model> void removeActorPostponed(Person person) {
        removeReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, false);
    }

    public void setActors(List<Person> persons) {
        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, persons, true);
    }

    public void setActorsPostponed(List<Person> persons) {
        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, persons, false);
    }

    public void setActors(Person... persons) {
        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, true, persons);
    }

    public void setActorsPostponed(Person... persons) {
        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, false, persons);
    }

    public <C extends Model> void addActor(Person person) {
        addReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, true);
    }

    public <C extends Model> void addActorPostponed(Person person) {
        addReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person, false);
    }

    @Override
    public float match(LibraryItem anotherItem, ListSimilarity... listSimilarities) {
        float similarity = super.match(anotherItem, listSimilarities);
        CreationItem anotherCreationItem = (CreationItem) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.creationsTitleSimilarity(getTitle(), anotherCreationItem.getTitle(), getOriginalTitle(), anotherCreationItem.getOriginalTitle()));
        similarity = Mycin.combine(similarity, ItemIntegrator.eventsYearSimilarity(getYear(), anotherCreationItem.getYear()));
        List<CountryCode> countries1 = getCountries();
        List<CountryCode> countries2 = anotherCreationItem.getCountries();
        int countryMatches = 0;
        for (CountryCode countryCode : countries1) {
            if (countries2.contains(countryCode)) {
                countryMatches++;
            }
        }
        similarity = Mycin.combine(similarity, evaluateListSimilarity(new ListSimilarity(countries1.size(), countries2.size(), countryMatches), 0.1f));
        for (ListSimilarity listSimilarity : listSimilarities) {
            switch (listSimilarity.referencedList) {
                case CREATORS:
                    similarity = Mycin.combine(similarity, evaluateListSimilarity(listSimilarity, 0.7f));
                case ACTORS:
                    similarity = Mycin.combine(similarity, evaluateListSimilarity(listSimilarity, 0.9f));
            }
        }
        return similarity;
    }

    @Override
    public void merge(LibraryItem anotherItem) {

    }

    @Override
    public void mergePostponed(LibraryItem anotherItem) {

    }
}
