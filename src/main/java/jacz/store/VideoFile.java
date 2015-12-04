package jacz.store;

import jacz.store.database.DatabaseMediator;
import jacz.store.util.QualityCode;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class VideoFile extends FileWithLanguages {

//    private String name;
//
//    private Integer minutes;
//
//    private QualityCode quality;
//
//    private List<LanguageCode> languages;
//
//    private List<SubtitleFile> subtitleFiles;

    public VideoFile(String dbPath) {
        super(dbPath);
    }

    VideoFile(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.VideoFile();
    }

    static List<VideoFile> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<VideoFile> videoFiles = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    videoFiles.add(new VideoFile(model, dbPath));
                }
            }
            return videoFiles;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public Integer getMinutes() {
        return getInteger("minutes");
    }

    public void setMinutes(Integer minutes) {
        set("minutes", minutes);
    }

    public Integer getResolution() {
        return getInteger("resolution");
    }

    public void setResolution(Integer resolution) {
        set("resolution", resolution);
    }

    public QualityCode getQuality() {
        return QualityCode.valueOf(getString("qualityCode"));
    }

    public void setQuality(QualityCode quality) {
        set("qualityCode", quality.name());
    }

    public List<SubtitleFile> getSubtitleFiles() {
        return SubtitleFile.buildList(dbPath, getDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class));
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
    public void merge(LibraryItem anotherItem) {
        super.merge(anotherItem);
        VideoFile anotherVideoFile = (VideoFile) anotherItem;
        if (getLength() == null && anotherVideoFile.getMinutes() != null) {
            setMinutes(anotherVideoFile.getMinutes());
        }
        if (getResolution() == null && anotherVideoFile.getResolution() != null) {
            setResolution(anotherVideoFile.getResolution());
        }
        if (getQuality() == null && anotherVideoFile.getQuality() != null) {
            setQuality(anotherVideoFile.getQuality());
        }
        // todo subtitles?
    }
}
