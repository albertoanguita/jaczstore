package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;

import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class ProducedCreationItem extends CreationItem {

    private List<Company> ProductionCompanies;

    private List<GenreCode> genres;

    private ImageFile imageFile;

    public ProducedCreationItem(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public ProducedCreationItem(
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
            ImageFile imageFile) {
        super(databaseMediator, id, timestamp, title, originalTitle, year, creatorsDirectors, actors, countries, externalURLs);
        ProductionCompanies = productionCompanies;
        this.genres = genres;
        this.imageFile = imageFile;
    }
}
