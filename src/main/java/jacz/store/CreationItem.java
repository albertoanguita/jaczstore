package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;

import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class CreationItem extends LibraryItem {

    private String title;

    private String originalTitle;

    private Integer year;

    private List<Person> creatorsDirectors;

    private List<Person> actors;

    private List<CountryCode> countries;

    private List<String> externalURLs;

    public CreationItem(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public CreationItem(
            DatabaseMediator databaseMediator,
            Integer id,
            int timestamp,
            String title,
            String originalTitle,
            Integer year,
            List<Person> creatorsDirectors,
            List<Person> actors,
            List<CountryCode> countries,
            List<String> externalURLs) {
        super(databaseMediator, id, timestamp);
        this.title = title;
        this.originalTitle = originalTitle;
        this.year = year;
        this.creatorsDirectors = creatorsDirectors;
        this.actors = actors;
        this.countries = countries;
        this.externalURLs = externalURLs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Person> getCreatorsDirectors() {
        return creatorsDirectors;
    }

    public void setCreatorsDirectors(List<Person> creatorsDirectors) {
        this.creatorsDirectors = creatorsDirectors;
    }

    public List<Person> getActors() {
        return actors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public List<CountryCode> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryCode> countries) {
        this.countries = countries;
    }

    public List<String> getExternalURLs() {
        return externalURLs;
    }

    public void setExternalURLs(List<String> externalURLs) {
        this.externalURLs = externalURLs;
    }
}
