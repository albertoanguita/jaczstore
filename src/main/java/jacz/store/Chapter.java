package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Chapter extends CreationItem {

    private String season;

    private int minutes;

    private List<VideoFile> videoFiles;

    public Chapter(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public Chapter(
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
            String season,
            int minutes,
            List<VideoFile> videoFiles) {
        super(databaseMediator, id, timestamp, title, originalTitle, year, creatorsDirectors, actors, countries, externalURLs);
        this.season = season;
        this.minutes = minutes;
        this.videoFiles = videoFiles;
    }

    @Override
    public void save() {
        databaseMediator.saveChapter(this);
    }

    @Override
    public void inflate() {

    }
}
