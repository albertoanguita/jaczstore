package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
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

    public Chapter() {
        super();
    }

    public Chapter(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Chapter();
    }

    static List<Chapter> buildList(List<? extends Model> models) {
        List<Chapter> chapters = new ArrayList<>();
        for (Model model : models) {
            chapters.add(new Chapter(model));
        }
        return chapters;
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

    public int getMinutes() {
        return getInteger("minutes");
    }

    public void setMinutes(int minutes) {
        set("minutes", minutes);
    }

    public List<VideoFile> getVideoFiles() {
        List<jacz.store.database.models.VideoFile> modelVideoFiles = getAssociation(jacz.store.database.models.VideoFile.class);
        return VideoFile.buildList(modelVideoFiles);
    }

    public <C extends Model> void removeVideoFiles() {
        removeAssociations(jacz.store.database.models.MoviesVideoFiles.class, getAssociationIdField(), null);
    }

    public void setVideoFiles(List<VideoFile> videoFiles) {
        setAssociations(getVideoFileAssociationModel(), getAssociationIdField(), null, videoFiles);
    }

    public void setVideoFiles(VideoFile... videoFiles) {
        setAssociations(getVideoFileAssociationModel(), getAssociationIdField(), null, videoFiles);
    }

//    public <C extends Model> void addVideoFiles(List<VideoFile> videoFiles) {
//        addAssociation(getVideoFileAssociationModel(), getAssociationIdField(), null, videoFiles);
//    }
//
//    public <C extends Model> void addVideoFiles(VideoFile... videoFiles) {
//        addAssociations(getVideoFileAssociationModel(), getAssociationIdField(), null, videoFiles);
//    }

    public <C extends Model> void addVideoFile(VideoFile videoFile) {
        addAssociation(getVideoFileAssociationModel(), getAssociationIdField(), null, videoFile);
    }
}
