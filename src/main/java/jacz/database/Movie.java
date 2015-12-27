package jacz.database;

import jacz.util.AI.inference.Mycin;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Movie extends ProducedCreationItem {

//    private int minutes;

//    private List<VideoFile> videoFiles;

    public Movie(String dbPath) {
        super(dbPath);
    }

    public Movie(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.MOVIE;
    }

    public static List<Movie> getMovies(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.MOVIE));
    }

    public static Movie getMovieById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.MOVIE, id);
        return model != null ? new Movie(model, dbPath) : null;
    }

    public static List<Movie> getMoviesFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
    }

    static List<Movie> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<Movie> movies = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    movies.add(new Movie(model, dbPath));
                }
            }
            return movies;
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

    public List<String> getTags() {
        return Tag.getItemTags(dbPath, this, DatabaseMediator.ItemType.MOVIE);
    }

    public boolean addTag(String tag) {
        return Tag.addTag(dbPath, this, tag, DatabaseMediator.ItemType.MOVIE);
    }

    public boolean removeTag(String tag) {
        return Tag.removeTag(dbPath, this, tag, DatabaseMediator.ItemType.MOVIE);
    }

    @Override
    public float match(DatabaseItem anotherItem, ListSimilarity... listSimilarities) {
        float similarity = super.match(anotherItem, listSimilarities);
        Movie anotherMovieItem = (Movie) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.durationSimilarity(getMinutes(), anotherMovieItem.getMinutes()));
        return similarity;
    }

    @Override
    public void merge(DatabaseItem anotherItem) {

    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {

    }
}
