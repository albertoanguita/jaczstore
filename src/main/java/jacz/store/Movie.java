package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database.DatabaseMediator;
import jacz.store.util.GenreCode;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Movie extends ProducedCreationItem {

    private int minutes;

    private List<VideoFile> videoFiles;

    public Movie(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public Movie(
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
            int minutes,
            List<VideoFile> videoFiles) {
        super(databaseMediator, id, timestamp, title, originalTitle, year, creatorsDirectors, actors, countries, externalURLs, productionCompanies, genres, imageFile);
        this.minutes = minutes;
        this.videoFiles = videoFiles;
    }

    @Override
    public void save() {
        databaseMediator.saveMovie(this);
    }

    @Override
    public void inflate() {

    }

}
