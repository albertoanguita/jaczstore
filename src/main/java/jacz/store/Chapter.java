package jacz.store;

import jacz.store.database.DatabaseMediator;
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

    public Chapter(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.CHAPTER;
    }

    public static List<Chapter> getChapters(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.CHAPTER));
    }

    public static Chapter getChapterById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.CHAPTER, id);
        return model != null ? new Chapter(model, dbPath) : null;
    }

    static List<Chapter> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<Chapter> chapters = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    chapters.add(new Chapter(model, dbPath));
                }
            }
            return chapters;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
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

    public List<Person> getDirectors() {
        return getCreatorsDirectors();
    }

    public <C extends Model> void removeDirectors() {
        removeCreatorsDirectors();
    }

    public <C extends Model> void removeDirector(Person person) {
        removeCreatorDirector(person);
    }

    public void setDirectors(List<Person> persons) {
        setCreatorsDirectors(persons);
    }

    public void setDirectors(Person... persons) {
        setCreatorsDirectors(persons);
    }

    public <C extends Model> void addDirector(Person person) {
        addCreatorDirector(person);
    }


    public List<VideoFile> getVideoFiles() {
        LazyList<jacz.store.database.models.VideoFile> models = getReferencedElements(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.VIDEO_FILE_LIST);
        return VideoFile.buildList(dbPath, models);
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
    public float match(LibraryItem anotherItem, ListSimilarity... listSimilarities) {
        float similarity = super.match(anotherItem, listSimilarities);
        Movie anotherMovieItem = (Movie) anotherItem;
        similarity = Mycin.combine(similarity, ItemIntegrator.durationSimilarity(getMinutes(), anotherMovieItem.getMinutes()));
        return similarity;
    }

    @Override
    public void merge(LibraryItem anotherItem) {

    }

    @Override
    public void mergePostponed(LibraryItem anotherItem) {

    }
}
