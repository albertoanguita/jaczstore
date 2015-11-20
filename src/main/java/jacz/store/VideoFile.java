package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.QualityCode;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class VideoFile extends File {

    private String name;

    private QualityCode quality;

    private List<LanguageCode> languages;

    private List<SubtitleFile> subtitleFiles;

    public VideoFile() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.VideoFile();
    }

}
