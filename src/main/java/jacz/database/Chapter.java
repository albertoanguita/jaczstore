package jacz.database;

import jacz.database.util.ItemIntegrator;
import org.aanguita.jacuzzi.AI.inference.Mycin;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Each chapter must belong to one and only one tv series
 * <p/>
 * Once we attach a chapter to a tv series, it will belong to that tv series for ever. It cannot be moved to another
 * tv series (it would not make much sense, and it is complex in terms of maintaining coherence)
 */
public final class Chapter extends CreationItem {

//    private String season;
//
//    private int minutes;

//    private List<VideoFile> videoFiles;

    Chapter(String dbPath) {
        super(dbPath);
    }

    public Chapter(String dbPath, String title) {
        super(dbPath, title);
    }

    public Chapter(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public Chapter(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    public DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.CHAPTER;
    }

    @Override
    public boolean isOrphan() {
        return getTVSeries().isEmpty();
    }

    public static List<Chapter> getChapters(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.CHAPTER));
    }

    public static Chapter getChapterById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.CHAPTER, id);
        return model != null ? new Chapter(model, dbPath) : null;
    }

    public static List<Chapter> getChaptersFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
    }

    static List<Chapter> buildList(String dbPath, List<? extends Model> models) {
        return buildList(dbPath, models, null);
    }

    static List<Chapter> buildList(String dbPath, List<? extends Model> models, Integer season) {
        DatabaseMediator.connect(dbPath);
        try {
            List<Chapter> chapters = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    Chapter chapter = new Chapter(model, dbPath);
                    if (season == null || season.equals(chapter.getSeason())) {
                        chapters.add(new Chapter(model, dbPath));
                    }
                }
            }
            return chapters;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public List<TVSeries> getTVSeries() {
        try {
            connect();
            List<jacz.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.CHAPTER_LIST);
            if (modelTVSeries != null) {
                return TVSeries.buildList(dbPath, modelTVSeries);
            } else {
                return new ArrayList<>();
            }
        } finally {
            disconnect();
        }
    }

    public Integer getSeason() {
        return getInteger(DatabaseMediator.Field.SEASON);
    }

    public void setSeason(Integer season) {
        set(DatabaseMediator.Field.SEASON, season, true);
    }

    public void setSeasonPostponed(Integer season) {
        set(DatabaseMediator.Field.SEASON, season, false);
    }

    public Integer getNumber() {
        return getInteger(DatabaseMediator.Field.NUMBER);
    }

    public void setNumber(Integer number) {
        set(DatabaseMediator.Field.NUMBER, number, true);
    }

    public void setNumberPostponed(Integer number) {
        set(DatabaseMediator.Field.NUMBER, number, false);
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

    public List<VideoFile> getVideoFiles() {
        List<jacz.database.models.VideoFile> models = getReferencedElements(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.VIDEO_FILE_LIST);
        if (models != null) {
            return VideoFile.buildList(dbPath, models);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Integer> getVideoFilesIds() {
        return getReferencedElementsIds(DatabaseMediator.Field.VIDEO_FILE_LIST);
    }

    public <C extends Model> void removeVideoFiles() {
        removeReferencedElements(DatabaseMediator.Field.VIDEO_FILE_LIST, true);
    }

    public <C extends Model> void removeVideoFilesPostponed() {
        removeReferencedElements(DatabaseMediator.Field.VIDEO_FILE_LIST, false);
    }

    public <C extends Model> void removeVideoFile(VideoFile videoFile) {
        removeReferencedElement(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFile, true);
    }

    public <C extends Model> void removeVideoFilePostponed(VideoFile videoFile) {
        removeReferencedElement(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFile, false);
    }

    public void setVideoFiles(List<VideoFile> videoFiles) {
        setReferencedElements(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFiles, true);
    }

    public void setVideoFilesPostponed(List<VideoFile> videoFiles) {
        setReferencedElements(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFiles, false);
    }

    public void setVideoFilesIds(List<Integer> videoFilesIds) {
        setReferencedElementsIds(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFilesIds, true);
    }

    public void setVideoFilesIdsPostponed(List<Integer> videoFilesIds) {
        setReferencedElementsIds(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFilesIds, false);
    }

    public void setVideoFiles(VideoFile... videoFiles) {
        setReferencedElements(DatabaseMediator.Field.VIDEO_FILE_LIST, true, videoFiles);
    }

    public void setVideoFilesPostponed(VideoFile... videoFiles) {
        setReferencedElements(DatabaseMediator.Field.VIDEO_FILE_LIST, false, videoFiles);
    }

    public <C extends Model> void addVideoFile(VideoFile videoFile) {
        addReferencedElement(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFile, true);
    }

    public <C extends Model> void addVideoFilePostponed(VideoFile videoFile) {
        addReferencedElement(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFile, false);
    }

    @Override
    public float match(DatabaseItem anotherItem) {
        // todo add comparison of tv series, and season/chapter number
        float similarity = super.match(anotherItem);
        Chapter anotherChapterItem = (Chapter) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.durationSimilarity(getMinutes(), anotherChapterItem.getMinutes()));
        similarity = Mycin.combine(similarity, ItemIntegrator.chapterSeasonAndNumber(getSeason(), getNumber(), anotherChapterItem.getSeason(), anotherChapterItem.getNumber()));
        return similarity;
    }

    @Override
    public void mergeBasicPostponed(DatabaseItem anotherItem) {
        super.mergeBasicPostponed(anotherItem);
        Chapter anotherChapterItem = (Chapter) anotherItem;
        if (getMinutes() == null && anotherChapterItem.getMinutes() != null) {
            setMinutesPostponed(anotherChapterItem.getMinutes());
        }
        if (getSeason() == null && anotherChapterItem.getSeason() != null) {
            setSeasonPostponed(anotherChapterItem.getSeason());
        }
    }

    @Override
    public DatabaseMediator.ReferencedElements getReferencedElements() {
        DatabaseMediator.ReferencedElements referencedElements = super.getReferencedElements();
        referencedElements.add(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.ReferencedList.VIDEO_FILES, getVideoFilesIds());
        return referencedElements;
    }

    @Override
    public void mergeReferencedElementsPostponed(DatabaseMediator.ReferencedElements referencedElements) {
        super.mergeReferencedElementsPostponed(referencedElements);
        for (Integer videoFileId : referencedElements.get(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.ReferencedList.VIDEO_FILES)) {
            addReferencedElementId(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFileId, false);
        }
    }

    @Override
    public void delete() {
        // check any tv series pointing to me, delete their reference to me
        for (TVSeries tvSeries : getTVSeries()) {
            tvSeries.removeChapter(this);
        }
        super.delete();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", season=" + getSeason() +
                ", number=" + getNumber() +
                ", minutes=" + getMinutes() +
                ", video file ids='" + getVideoFilesIds();
    }
}
