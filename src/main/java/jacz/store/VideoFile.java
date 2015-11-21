package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.QualityCode;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
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

    VideoFile(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.VideoFile();
    }

    static List<VideoFile> buildList(List<? extends Model> models) {
        List<VideoFile> videoFiles = new ArrayList<>();
        for (Model model : models) {
            videoFiles.add(new VideoFile(model));
        }
        return videoFiles;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public QualityCode getQuality() {
        return QualityCode.valueOf(getString("qualityCode"));
    }

    public void setQuality(QualityCode quality) {
        set("qualityCode", quality.name());
    }

    @Override
    public List<LanguageCode> getLanguages() {
        return super.getLanguages();
    }

    @Override
    public void setLanguages(List<LanguageCode> languages) {
        super.setLanguages(languages);
    }

    public List<SubtitleFile> getSubtitleFiles() {
        // todo
        return null;
    }

    public void setSubtitleFiles(List<SubtitleFile> subtitleFiles) {
        // todo
    }
}
