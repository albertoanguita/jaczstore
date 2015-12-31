package jacz.database;

import jacz.database.util.ItemIntegrator;
import jacz.util.AI.inference.Mycin;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Chapter extends CreationItem {

//    private String season;
//
//    private int minutes;

//    private List<VideoFile> videoFiles;

    public Chapter(String dbPath) {
        super(dbPath);
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

    static List<Chapter> buildList(String dbPath, List<? extends Model> models, String season) {
        DatabaseMediator.connect(dbPath);
        try {
            List<Chapter> chapters = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    Chapter chapter  = new Chapter(model, dbPath);
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
        List<jacz.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.CHAPTER_LIST);
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    public void setTVSeries(TVSeries tvSeries) {
        tvSeries.addChapter(this);
    }

    public void setTVSeriesPostponed(TVSeries tvSeries) {

    }

    public void setTVSeriesId(Integer tvSeriesId) {
        setTVSeries(TVSeries.getTVSeriesById(dbPath, tvSeriesId));
    }

    public void setTVSeriesIdPostponed(Integer tvSeriesId) {
        setTVSeriesPostponed(TVSeries.getTVSeriesById(dbPath, tvSeriesId));
    }

    public String getSeason() {
        return getString(DatabaseMediator.Field.SEASON);
    }

    public void setSeason(String season) {
        set(DatabaseMediator.Field.SEASON, season, true);
    }

    public void setSeasonPostponed(String season) {
        set(DatabaseMediator.Field.SEASON, season, false);
    }

    public Integer getMinutes() {
        return getInteger(DatabaseMediator.Field.MINUTES);
    }

    public void setMinutes(int minutes) {
        set(DatabaseMediator.Field.MINUTES, minutes, true);
    }

    public void setMinutesPostponed(int minutes) {
        set(DatabaseMediator.Field.MINUTES, minutes, false);
    }

    public List<VideoFile> getVideoFiles() {
        LazyList<jacz.database.models.VideoFile> models = getReferencedElements(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.VIDEO_FILE_LIST);
        return VideoFile.buildList(dbPath, models);
    }

    public List<String> getVideoFilesIds() {
        return getReferencedElementsIds(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.VIDEO_FILE_LIST);
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

    public void setVideoFilesIds(List<String> videoFilesIds) {
        setReferencedElementsIds(DatabaseMediator.Field.VIDEO_FILE_LIST, videoFilesIds, true);
    }

    public void setVideoFilesIdsPostponed(List<String> videoFilesIds) {
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
        float similarity = super.match(anotherItem);
        Chapter anotherChapterItem = (Chapter) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.durationSimilarity(getMinutes(), anotherChapterItem.getMinutes()));
        return similarity;
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        super.mergePostponed(anotherItem);
        Chapter anotherChapterItem = (Chapter) anotherItem;
        if (getMinutes() == null && anotherChapterItem.getMinutes() != null) {
            setMinutesPostponed(anotherChapterItem.getMinutes());
        }
        for (VideoFile videoFile : anotherChapterItem.getVideoFiles()) {
            addVideoFilePostponed(videoFile);
        }
    }
}
