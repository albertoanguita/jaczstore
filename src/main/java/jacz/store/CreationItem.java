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

    @Override
    public List<CountryCode> getCountries() {
        return super.getCountries();
    }

    @Override
    public void removeCountries() {
        super.removeCountries();
    }

    @Override
    public boolean removeCountry(CountryCode country) {
        return super.removeCountry(country);
    }

    @Override
    public void setCountries(List<CountryCode> countries) {
        super.setCountries(countries);
    }

    @Override
    public boolean addCountry(CountryCode country) {
        return super.addCountry(country);
    }

    @Override
    public List<String> getExternalURLs() {
        return super.getExternalURLs();
    }

    @Override
    public void removeExternalURLs() {
        super.removeExternalURLs();
    }

    @Override
    public boolean removeExternalURL(String externalURL) {
        return super.removeExternalURL(externalURL);
    }

    @Override
    public void setExternalURLs(List<String> externalURLs) {
        super.setExternalURLs(externalURLs);
    }

    @Override
    public boolean addExternalURL(String externalURL) {
        return super.addExternalURL(externalURL);
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
