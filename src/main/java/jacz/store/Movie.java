package jacz.store;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Movie extends CreationItem {

    // todo genres
    // todo countries
    // todo image hash

    private List<Person> directors;

    private List<Person> actors;

    private List<Company> ProductionCompanies;

    private List<ExternalURL> externalURLs;

}
