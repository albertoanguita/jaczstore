package jacz.database;

import jacz.database.util.QualityCode;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class VideoFile extends FileWithLanguages {

//    private Integer minutes;
//
//    private QualityCode quality;
//
//    private List<LanguageCode> languages;
//
//    private List<SubtitleFile> subtitleFiles;

    VideoFile(String dbPath) {
        super(dbPath);
    }

    public VideoFile(String dbPath, String hash) {
        super(dbPath, hash);
    }

    public VideoFile(String dbPath, Integer id) {
        super(dbPath, id);
    }

    VideoFile(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    public DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.VIDEO_FILE;
    }

    public static List<VideoFile> getVideoFiles(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.VIDEO_FILE));
    }

    public static VideoFile getVideoFileById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.VIDEO_FILE, id);
        return model != null ? new VideoFile(model, dbPath) : null;
    }

    public static List<VideoFile> getVideoFilesFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
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
        return getQuality(DatabaseMediator.Field.QUALITY_CODE);
    }

    public void setQuality(QualityCode quality) {
        String qualityValue = quality != null ? quality.name() : null;
        set(DatabaseMediator.Field.QUALITY_CODE, qualityValue, true);
    }

    public void setQualityPostponed(QualityCode quality) {
        String qualityValue = quality != null ? quality.name() : null;
        set(DatabaseMediator.Field.QUALITY_CODE, qualityValue, false);
    }

    public List<SubtitleFile> getSubtitleFiles() {
        List<jacz.database.models.SubtitleFile> models = getReferencedElements(DatabaseMediator.ItemType.SUBTITLE_FILE, DatabaseMediator.Field.SUBTITLE_FILE_LIST);
        if (models != null) {
            return SubtitleFile.buildList(dbPath, models);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Integer> getSubtitleFilesIds() {
        return getReferencedElementsIds(DatabaseMediator.Field.SUBTITLE_FILE_LIST);
    }

    public void removeSubtitleFiles() {
        removeReferencedElements(DatabaseMediator.Field.SUBTITLE_FILE_LIST, true);
    }

    public void removeSubtitleFilesPostponed() {
        removeReferencedElements(DatabaseMediator.Field.SUBTITLE_FILE_LIST, false);
    }

    public <C extends Model> void removeSubtitleFile(SubtitleFile subtitleFile) {
        removeReferencedElement(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFile, true);
    }

    public <C extends Model> void removeSubtitleFilePostponed(SubtitleFile subtitleFile) {
        removeReferencedElement(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFile, false);
    }

    public void setSubtitleFiles(List<SubtitleFile> subtitleFiles) {
        setReferencedElements(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFiles, true);
    }

    public void setSubtitleFilesPostponed(List<SubtitleFile> subtitleFiles) {
        setReferencedElements(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFiles, false);
    }

    public void setSubtitleFilesIds(List<Integer> subtitleFilesIds) {
        setReferencedElementsIds(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFilesIds, true);
    }

    public void setSubtitleFilesIdsPostponed(List<Integer> subtitleFilesIds) {
        setReferencedElementsIds(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFilesIds, false);
    }

    public void setSubtitleFiles(SubtitleFile... subtitleFiles) {
        setReferencedElements(DatabaseMediator.Field.SUBTITLE_FILE_LIST, true, subtitleFiles);
    }

    public void setSubtitleFilesPostponed(SubtitleFile... subtitleFiles) {
        setReferencedElements(DatabaseMediator.Field.SUBTITLE_FILE_LIST, false, subtitleFiles);
    }

    public void addSubtitleFile(SubtitleFile subtitleFile) {
        addReferencedElement(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFile, true);
    }

    public void addSubtitleFilePostponed(SubtitleFile subtitleFile) {
        addReferencedElement(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFile, false);
    }

    @Override
    public void mergeBasicPostponed(DatabaseItem anotherItem) {
        super.mergeBasicPostponed(anotherItem);
        VideoFile anotherVideoFile = (VideoFile) anotherItem;
        if (getMinutes() == null && anotherVideoFile.getMinutes() != null) {
            setMinutesPostponed(anotherVideoFile.getMinutes());
        }
        if (getResolution() == null && anotherVideoFile.getResolution() != null) {
            setResolutionPostponed(anotherVideoFile.getResolution());
        }
        if (getQuality() == null && anotherVideoFile.getQuality() != null) {
            setQualityPostponed(anotherVideoFile.getQuality());
        }
    }


    @Override
    public DatabaseMediator.ReferencedElements getReferencedElements() {
        DatabaseMediator.ReferencedElements referencedElements = super.getReferencedElements();
        referencedElements.add(DatabaseMediator.ItemType.SUBTITLE_FILE, DatabaseMediator.ReferencedList.SUBTITLE_FILES, getSubtitleFilesIds());
        return referencedElements;
    }

    @Override
    public void mergeReferencedElementsPostponed(DatabaseMediator.ReferencedElements referencedElements) {
        super.mergeReferencedElementsPostponed(referencedElements);
        for (Integer subtitleFileId : referencedElements.get(DatabaseMediator.ItemType.SUBTITLE_FILE, DatabaseMediator.ReferencedList.SUBTITLE_FILES)) {
            addReferencedElementId(DatabaseMediator.Field.SUBTITLE_FILE_LIST, subtitleFileId, false);
        }
    }

    @Override
    public void delete() {
        // video files cannot be deleted. This way we avoid inconsistencies of references to video files pointing
        // to an non-existent item. We simply delete references to these
        // todo in the future, add the process of removing non-referenced video files at startup
    }
}
