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
    public void setCountries(List<CountryCode> countries) {
        super.setCountries(countries);
    }

    @Override
    public List<String> getExternalURLs() {
        return super.getExternalURLs();
    }

    @Override
    public void setExternalURLs(List<String> externalURLs) {
        super.setExternalURLs(externalURLs);
    }

    protected List<Person> getCreatorsDirectors() {
        List<jacz.store.database.models.Person> modelPersons = getAssociation(jacz.store.database.models.Person.class, "type = ? ", DatabaseMediator.PERSON_TYPE.CREATOR.name());
        return Person.buildList(modelPersons);
    }

    protected <C extends Model> void removeCreatorsDirectors() {
        removeAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name());
    }

    protected void setCreatorsDirectors(List<Person> persons) {
        setAssociationList(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name(), persons);
    }

    protected void setCreatorsDirectors(Person... persons) {
        setAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name(), persons);
    }

    protected <C extends Model> void addCreatorDirectors(List<Person> persons) {
        addAssociationList(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name(), persons);
    }

    protected <C extends Model> void addCreatorDirectors(Person... persons) {
        addAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name(), persons);
    }

    protected <C extends Model> void addCreatorDirector(Person person) {
        addAssociation(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.CREATOR.name(), person);
    }

    public List<Person> getActors() {
        List<jacz.store.database.models.Person> modelPersons = getAssociation(jacz.store.database.models.Person.class, "type = ? ", DatabaseMediator.PERSON_TYPE.ACTOR.name());
        return Person.buildList(modelPersons);
    }

    public <C extends Model> void removeActors() {
        removeAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name());
    }

    public void setActors(List<Person> persons) {
        setAssociationList(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name(), persons);
    }

    public void setActors(Person... persons) {
        setAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name(), persons);
    }

    public <C extends Model> void addActors(List<Person> persons) {
        addAssociationList(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name(), persons);
    }

    public <C extends Model> void addActors(Person... persons) {
        addAssociations(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name(), persons);
    }

    public <C extends Model> void addActor(Person person) {
        addAssociation(getPeopleAssociationModel(), getAssociationIdField(), DatabaseMediator.PERSON_TYPE.ACTOR.name(), person);
    }
}
