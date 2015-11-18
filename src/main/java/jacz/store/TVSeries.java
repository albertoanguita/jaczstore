package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class TVSeries extends ProducedCreationItem {

    private List<Chapter> chapters;

    public TVSeries(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public TVSeries(
            DatabaseMediator databaseMediator,
            Integer id,
            int timestamp,
            String title,
            String originalTitle,
            Integer year,
            List<Person> creatorsDirectors,
            List<Person> actors,
            List<CountryCode> countries,
            List<String> externalURLs,
            List<Company> productionCompanies,
            List<GenreCode> genres,
            ImageFile imageFile,
            List<Chapter> chapters) {
        super(databaseMediator, id, timestamp, title, originalTitle, year, creatorsDirectors, actors, countries, externalURLs, productionCompanies, genres, imageFile);
        this.chapters = chapters;
    }

    @Override
    public void save() {
        databaseMediator.saveTVSeries(this);
    }

    @Override
    public void inflate() {

    }
}
