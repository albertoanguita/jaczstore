package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database.DatabaseMediator;
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
        set(DatabaseMediator.Field.TITLE, title);
    }

    public String getOriginalTitle() {
        return getString(DatabaseMediator.Field.ORIGINAL_TITLE);
    }

    public void setOriginalTitle(String originalTitle) {
        set(DatabaseMediator.Field.ORIGINAL_TITLE, originalTitle);
    }

    public Integer getYear() {
        return getInteger(DatabaseMediator.Field.YEAR);
    }

    public void setYear(Integer year) {
        set(DatabaseMediator.Field.YEAR, year);
    }

    public List<CountryCode> getCountries() {
        return getEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class);
    }

    public void removeCountries() {
        removeList(DatabaseMediator.Field.COUNTRIES);
    }

    public boolean removeCountry(CountryCode country) {
        return removeEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, "getAlpha2");
    }

    public void setCountries(List<CountryCode> countries) {
        setEnums(DatabaseMediator.Field.COUNTRIES, CountryCode.class, countries, "getAlpha2");
    }

    public boolean addCountry(CountryCode country) {
        return addEnum(DatabaseMediator.Field.COUNTRIES, CountryCode.class, country, "getAlpha2");
    }

    public List<String> getExternalURLs() {
        return getStringList(DatabaseMediator.Field.EXTERNAL_URLS);
    }

    public void removeExternalURLs() {
        removeStringList(DatabaseMediator.Field.EXTERNAL_URLS);
    }

    public boolean removeExternalURL(String externalURL) {
        return removeStringValue(DatabaseMediator.Field.EXTERNAL_URLS, externalURL);
    }

    public void setExternalURLs(List<String> externalURLs) {
        setStringList(DatabaseMediator.Field.EXTERNAL_URLS, externalURLs);
    }

    public boolean addExternalURL(String externalURL) {
        return addStringValue(DatabaseMediator.Field.EXTERNAL_URLS, externalURL);
    }

    protected List<Person> getCreatorsDirectors() {
        LazyList<jacz.store.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.CREATOR_LIST);
        return Person.buildList(dbPath, models);
    }

    protected <C extends Model> void removeCreatorsDirectors() {
        removeReferencedElements(DatabaseMediator.Field.CREATOR_LIST);
    }

    protected <C extends Model> void removeCreatorDirector(Person person) {
        removeReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person);
    }

    protected void setCreatorsDirectors(List<Person> persons) {
        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, persons);
    }

    protected void setCreatorsDirectors(Person... persons) {
        setReferencedElements(DatabaseMediator.Field.CREATOR_LIST, persons);
    }

    protected <C extends Model> void addCreatorDirector(Person person) {
        addReferencedElement(DatabaseMediator.Field.CREATOR_LIST, person);
    }

    protected List<Person> getActors() {
        LazyList<jacz.store.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.PERSON, DatabaseMediator.Field.ACTOR_LIST);
        return Person.buildList(dbPath, models);
    }

    public <C extends Model> void removeActors() {
        removeReferencedElements(DatabaseMediator.Field.ACTOR_LIST);
    }

    public <C extends Model> void removeActor(Person person) {
        removeReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person);
    }

    public void setActors(List<Person> persons) {
        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, persons);
    }

    public void setActors(Person... persons) {
        setReferencedElements(DatabaseMediator.Field.ACTOR_LIST, persons);
    }

    public <C extends Model> void addActor(Person person) {
        addReferencedElement(DatabaseMediator.Field.ACTOR_LIST, person);
    }
}
