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

    private Integer minutes;

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
            if (model != null) {
                videoFiles.add(new VideoFile(model));
            }
        }
        return videoFiles;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public Integer getMinutes() {
        return getInteger("minutes");
    }

    public void setMinutes(Integer minutes) {
        set("minutes", minutes);
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
    public void removeLanguages() {
        super.removeLanguages();
    }

    @Override
    public boolean removeLanguages(LanguageCode language) {
        return super.removeLanguages(language);
    }

    @Override
    public void setLanguages(List<LanguageCode> languages) {
        super.setLanguages(languages);
    }

    @Override
    public boolean addLanguage(LanguageCode language) {
        return super.addLanguage(language);
    }

    public List<SubtitleFile> getSubtitleFiles() {
        return SubtitleFile.buildList(getDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class));
    }

    public void removeSubtitleFiles() {
        removeDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class);
    }

    public void setSubtitleFiles(List<SubtitleFile> subtitleFiles) {
        setDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class, subtitleFiles);
    }

    public void setSubtitleFiles(SubtitleFile... subtitleFiles) {
        setDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class, subtitleFiles);
    }

    public void addSubtitleFile(SubtitleFile subtitleFile) {
        addDirectAssociationChild(subtitleFile);
    }

    @Override
    public void delete() {
        super.delete();
        removeSubtitleFiles();
        removeAssociations(jacz.store.database.models.MoviesVideoFiles.class, "video_file_id", null);
        removeAssociations(jacz.store.database.models.ChaptersVideoFiles.class, "video_file_id", null);
    }
}
