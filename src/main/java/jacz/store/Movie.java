package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Movie extends ProducedCreationItem {

//    private int minutes;

//    private List<VideoFile> videoFiles;

    public Movie() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Movie();
    }

    @Override
    Class<? extends Model> getPeopleAssociationModel() {
        return jacz.store.database.models.MoviesPeople.class;
    }

    @Override
    Class<? extends Model> getCompanyAssociationModel() {
        return jacz.store.database.models.MoviesCompanies.class;
    }

    private Class<? extends Model> getVideoFileAssociationModel() {
        return jacz.store.database.models.MoviesVideoFiles.class;
    }

    @Override
    String getAssociationIdField() {
        return "movie_id";
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

    public Integer getMinutes() {
        return getInteger("minutes");
    }

    public void setMinutes(Integer minutes) {
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

    public <C extends Model> void addVideoFile(VideoFile videoFile) {
        addAssociation(getVideoFileAssociationModel(), getAssociationIdField(), null, videoFile);
    }

}
