package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database.DatabaseMediator;
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

    public CreationItem() {
        super();
    }

    public CreationItem(Model model) {
        super(model);
    }

    abstract Class<? extends Model> getPeopleAssociationModel();

    abstract String getAssociationIdField();

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }

    public String getOriginalTitle() {
        return getString("originalTitle");
    }

    public void setOriginalTitle(String originalTitle) {
        set("originalTitle", originalTitle);
    }

    public Integer getYear() {
        return getInteger("year");
    }

    public void setYear(Integer year) {
        set("year", year);
    }

    public List<CountryCode> getCountries() {
        return getEnums("countries", CountryCode.class);
    }

    public void removeCountries() {
        removeList("countries");
    }

    public boolean removeCountry(CountryCode country) {
        return removeEnum("countries", CountryCode.class, country, "getAlpha2");
    }

    public void setCountries(List<CountryCode> countries) {
        setEnums("countries", CountryCode.class, countries, "getAlpha2");
    }

    public boolean addCountry(CountryCode country) {
        return addEnum("countries", CountryCode.class, country, "getAlpha2");
    }

    public List<String> getExternalURLs() {
        return getStringList("externalURLs");
    }

    public void removeExternalURLs() {
        removeStringList("externalURLs");
    }

    public boolean removeExternalURL(String externalURL) {
        return removeStringValue("externalURLs", externalURL);
    }

    public void setExternalURLs(List<String> externalURLs) {
        setStringList("externalURLs", externalURLs);
    }

    public boolean addExternalURL(String externalURL) {
        return addStringValue("externalURLs", externalURL);
    }

    protected List<Person> getCreatorsDirectors() {
        List<jacz.store.database.models.Person> modelPersons = getAssociation(jacz.store.database.models.Person.class, getPeopleAssociationModel(), "type = ? ", DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return Person.buildList(modelPersons);
    }

    protected <C extends Model> void removeCreatorsDirectors() {
        removeAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name());
    }

    protected <C extends Model> void removeCreatorDirector(Person person) {
        removeAssociation(getPeopleAssociationModel(), getAssociationIdField(), person, "person_id", DatabaseMediator.PERSON_TYPE.CREATOR.name());
    }

    protected void setCreatorsDirectors(List<Person> persons) {
        setAssociations(getPeopleAssociationModel(), getAssociationIdField(), "person_id", DatabaseMediator.PERSON_TYPE.CREATOR.name(), persons);
    }

    protected void setCreatorsDirectors(Person... persons) {
        setAssociations(getPeopleAssociationModel(), getAssociationIdField(), "person_id", DatabaseMediator.PERSON_TYPE.CREATOR.name(), persons);
    }

    protected <C extends Model> void addCreatorDirector(Person person) {
        addAssociation(getPeopleAssociationModel(), getAssociationIdField(), "person_id", DatabaseMediator.PERSON_TYPE.CREATOR.name(), person);
    }

    protected List<Person> getActors() {
        List<jacz.store.database.models.Person> modelPersons = getAssociation(jacz.store.database.models.Person.class, getPeopleAssociationModel(), "type = ? ", DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return Person.buildList(modelPersons);
    }

    public <C extends Model> void removeActors() {
        removeAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name());
    }

    public <C extends Model> void removeActor(Person person) {
        removeAssociation(getPeopleAssociationModel(), getAssociationIdField(), person, "person_id", DatabaseMediator.PERSON_TYPE.ACTOR.name());
    }

    public void setActors(List<Person> persons) {
        setAssociations(getPeopleAssociationModel(), getAssociationIdField(), "person_id", DatabaseMediator.PERSON_TYPE.ACTOR.name(), persons);
    }

    public void setActors(Person... persons) {
        setAssociations(getPeopleAssociationModel(), getAssociationIdField(), "person_id", DatabaseMediator.PERSON_TYPE.ACTOR.name(), persons);
    }

    public <C extends Model> void addActor(Person person) {
        addAssociation(getPeopleAssociationModel(), getAssociationIdField(), "person_id", DatabaseMediator.PERSON_TYPE.ACTOR.name(), person);
    }
}
