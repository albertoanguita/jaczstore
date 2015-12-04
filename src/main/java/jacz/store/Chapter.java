package jacz.store;

import jacz.store.database.DatabaseMediator;
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
    protected Model buildModel() {
        return new jacz.store.database.models.Chapter();
    }

    public static List<Chapter> getChapters(String dbPath) {
        return buildList(dbPath, getModels(dbPath, jacz.store.database.models.Chapter.class));
    }

    public static Chapter getChapterById(String dbPath, int id) {
        Model model = getModelById(dbPath, jacz.store.database.models.Chapter.class, id);
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

    @Override
    Class<? extends Model> getPeopleAssociationModel() {
        return jacz.store.database.models.ChaptersPeople.class;
    }

    private Class<? extends Model> getVideoFileAssociationModel() {
        return jacz.store.database.models.ChaptersVideoFiles.class;
    }

    @Override
    String getAssociationIdField() {
        return "chapter_id";
    }

    public String getSeason() {
        return getString("season");
    }

    public void setSeason(String season) {
        set("season", season);
    }

    public Integer getMinutes() {
        return getInteger("minutes");
    }

    public void setMinutes(int minutes) {
        set("minutes", minutes);
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
        List<jacz.store.database.models.VideoFile> modelVideoFiles = getAssociation(jacz.store.database.models.VideoFile.class);
        return VideoFile.buildList(dbPath, modelVideoFiles);
    }

    public <C extends Model> void removeVideoFiles() {
        removeAssociations(jacz.store.database.models.ChaptersVideoFiles.class, getAssociationIdField(), null);
    }

    public <C extends Model> void removeVideoFile(VideoFile videoFile) {
        removeAssociation(getVideoFileAssociationModel(), getAssociationIdField(), videoFile, "video_file_id", null);
    }

    public void setVideoFiles(List<VideoFile> videoFiles) {
        setAssociations(getVideoFileAssociationModel(), getAssociationIdField(), "video_file_id", null, videoFiles);
    }

    public void setVideoFiles(VideoFile... videoFiles) {
        setAssociations(getVideoFileAssociationModel(), getAssociationIdField(), "video_file_id", null, videoFiles);
    }

    public <C extends Model> void addVideoFile(VideoFile videoFile) {
        addAssociation(getVideoFileAssociationModel(), getAssociationIdField(), "video_file_id", null, videoFile);
    }
}
