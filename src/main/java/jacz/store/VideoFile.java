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
    protected DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.VIDEO_FILE;
    }

    public static List<VideoFile> getVideoFiles(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.VIDEO_FILE));
    }

    public static VideoFile getVideoFileById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.VIDEO_FILE, id);
        return model != null ? new VideoFile(model, dbPath) : null;
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
        return getInteger(DatabaseMediator.Field.MINUTES);
    }

    public void setMinutes(Integer minutes) {
        set(DatabaseMediator.Field.MINUTES, minutes, true);
    }

    public void setMinutesPostponed(Integer minutes) {
        set(DatabaseMediator.Field.MINUTES, minutes, false);
    }

    public Integer getResolution() {
        return getInteger(DatabaseMediator.Field.RESOLUTION);
    }

    public void setResolution(Integer resolution) {
        set(DatabaseMediator.Field.RESOLUTION, resolution, true);
    }

    public void setResolutionPostponed(Integer resolution) {
        set(DatabaseMediator.Field.RESOLUTION, resolution, false);
    }

    public QualityCode getQuality() {
        return QualityCode.valueOf(getString(DatabaseMediator.Field.QUALITY_CODE));
    }

    public void setQuality(QualityCode quality) {
        set(DatabaseMediator.Field.QUALITY_CODE, quality.name(), true);
    }

    public void setQualityPostponed(QualityCode quality) {
        set(DatabaseMediator.Field.QUALITY_CODE, quality.name(), false);
    }

    public List<SubtitleFile> getSubtitleFiles() {
        return SubtitleFile.buildList(dbPath, getDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class));
    }

    public void removeSubtitleFiles() {
        // todo replace with enums
        removeDirectAssociationChildren(jacz.store.database.models.SubtitleFile.class);
    }

//    public <C extends Model> void removeSubtitleFile(SubtitleFile subtitleFile) {
        //removeReferencedElement(DatabaseMediator.Field.SUB, subtitleFile);
        // todo
//    }

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
        if (getMinutes() == null && anotherVideoFile.getMinutes() != null) {
            setMinutes(anotherVideoFile.getMinutes());
        }
        if (getResolution() == null && anotherVideoFile.getResolution() != null) {
            setResolution(anotherVideoFile.getResolution());
        }
        if (getQuality() == null && anotherVideoFile.getQuality() != null) {
            setQuality(anotherVideoFile.getQuality());
        }
    }

    @Override
    public void delete() {
        // video files cannot be deleted. This way we avoid inconsistencies of references to video files pointing
        // to an non-existent item. We simply delete references to these
        // todo in the future, add the process of removing non-referenced video files at startup
    }
}
