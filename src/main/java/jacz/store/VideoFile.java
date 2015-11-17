package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database.DatabaseMediator;
import jacz.store.util.QualityCode;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class VideoFile extends File {

    private String name;

    private QualityCode quality;

    private List<LanguageCode> languages;

    private List<SubtitleFile> subtitleFiles;

    public VideoFile(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public VideoFile(
            DatabaseMediator databaseMediator,
            Integer id,
            int timestamp,
            String hash,
            Long length,
            String name,
            QualityCode quality,
            List<LanguageCode> languages,
            List<SubtitleFile> subtitleFiles) {
        super(databaseMediator, id, timestamp, hash, length);
        this.name = name;
        this.quality = quality;
        this.languages = languages;
        this.subtitleFiles = subtitleFiles;
    }

    @Override
    public void save() {
        databaseMediator.saveVideoFile(this);
    }

    @Override
    public void inflate() {

    }
}
