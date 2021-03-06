package jacz.database;

import jacz.database.util.ItemIntegrator;
import org.aanguita.jacuzzi.AI.inference.Mycin;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Movie extends ProducedCreationItem {

//    private int minutes;

//    private List<VideoFile> videoFiles;

    Movie(String dbPath) {
        super(dbPath);
    }

    public Movie(String dbPath, String title) {
        super(dbPath, title);
    }

    public Movie(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public Movie(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    public DatabaseMediator.ItemType getItemType() {
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
        float similarity = super.match(anotherItem);
        Movie anotherMovieItem = (Movie) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.durationSimilarity(getMinutes(), anotherMovieItem.getMinutes()));
        return similarity;
    }

    @Override
    public void mergeBasicPostponed(DatabaseItem anotherItem) {
        super.mergeBasicPostponed(anotherItem);
        Movie anotherMovieItem = (Movie) anotherItem;
        if (getMinutes() == null && anotherMovieItem.getMinutes() != null) {
            setMinutesPostponed(anotherMovieItem.getMinutes());
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
    public String toString() {
        return super.toString() +
                ", minutes='" + getMinutes() +
                ", video file ids='" + getVideoFilesIds();
    }
}
