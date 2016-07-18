package jacz.database;

import jacz.database.util.LocalizedLanguage;
import jacz.database.util.QualityCode;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class VideoFile extends File {

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

    @Override
    public boolean isOrphan() {
        return getMovies().isEmpty() && getChapters().isEmpty();
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

    public Integer getBitrate() {
        return getInteger(DatabaseMediator.Field.BITRATE);
    }

    public void setBitrate(Integer bitrate) {
        set(DatabaseMediator.Field.BITRATE, bitrate, true);
    }

    public void setBitratePostponed(Integer bitrate) {
        set(DatabaseMediator.Field.BITRATE, bitrate, false);
    }

    public QualityCode getQuality() {
        return getEnum(DatabaseMediator.Field.QUALITY_CODE, QualityCode.class);
    }

    public void setQuality(QualityCode quality) {
        setEnum(DatabaseMediator.Field.QUALITY_CODE, QualityCode.class, quality, DatabaseMediator.QUALITY_NAME_METHOD, true);
    }

    public void setQualityPostponed(QualityCode quality) {
        setEnum(DatabaseMediator.Field.QUALITY_CODE, QualityCode.class, quality, DatabaseMediator.QUALITY_NAME_METHOD, false);
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

    public List<LocalizedLanguage> getLocalizedLanguages() {
        return LocalizedLanguage.deserialize(getStringList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST));
    }

    public void removeLocalizedLanguages() {
        removeList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, true);
    }

    public void removeLocalizedLanguagesPostponed() {
        removeList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, false);
    }

    public boolean removeLocalizedLanguage(LocalizedLanguage localizedLanguage) {
        return removeStringValue(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, LocalizedLanguage.serialize(localizedLanguage), true);
    }

    public boolean removeLocalizedLanguagePostponed(LocalizedLanguage localizedLanguage) {
        return removeStringValue(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, LocalizedLanguage.serialize(localizedLanguage), false);
    }

    public void setLocalizedLanguages(List<LocalizedLanguage> languages) {
        setStringList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, languages, true);
    }

    public void setLocalizedLanguagesPostponed(List<LocalizedLanguage> languages) {
        setStringList(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, languages, false);
    }

    public boolean addLocalizedLanguage(LocalizedLanguage localizedLanguage) {
        return addStringValue(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, LocalizedLanguage.serialize(localizedLanguage), true);
    }

    public boolean addLocalizedLanguagePostponed(LocalizedLanguage localizedLanguage) {
        return addStringValue(DatabaseMediator.Field.LOCALIZED_LANGUAGE_LIST, LocalizedLanguage.serialize(localizedLanguage), false);
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
        if (getBitrate() == null && anotherVideoFile.getBitrate() != null) {
            setBitratePostponed(anotherVideoFile.getBitrate());
        }
        if (getQuality() == null && anotherVideoFile.getQuality() != null) {
            setQualityPostponed(anotherVideoFile.getQuality());
        }
        for (LocalizedLanguage localizedLanguage : anotherVideoFile.getLocalizedLanguages()) {
            addLocalizedLanguagePostponed(localizedLanguage);
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

    public List<Movie> getMovies() {
        try {
            connect();
            List<jacz.database.models.Movie> modelMovies = getElementsContainingMe(DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.VIDEO_FILE_LIST);
            if (modelMovies != null) {
                return Movie.buildList(dbPath, modelMovies);
            } else {
                return new ArrayList<>();
            }
        } finally {
            disconnect();
        }
    }

    public List<Chapter> getChapters() {
        try {
            connect();
            List<jacz.database.models.Chapter> modelChapters = getElementsContainingMe(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.VIDEO_FILE_LIST);
            if (modelChapters != null) {
                return Chapter.buildList(dbPath, modelChapters);
            } else {
                return new ArrayList<>();
            }
        } finally {
            disconnect();
        }
    }

    @Override
    public void delete() {
        // check any tv series pointing to me, delete their reference to me
        for (Movie movie : getMovies()) {
            movie.removeVideoFile(this);
        }
        for (Chapter chapter : getChapters()) {
            chapter.removeVideoFile(this);
        }
        super.delete();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", minutes=" + getMinutes() +
                ", res=" + getResolution() +
                ", bitrate=" + getBitrate() +
                ", quality=" + getQuality() +
                ", subtitles=" + getSubtitleFilesIds() +
                ", localized languages=" + getLocalizedLanguages();
    }
}
