package jacz.store;

import org.javalite.activejdbc.Model;

import java.util.ArrayList;
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

    public Movie(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Movie();
    }

    public static List<Movie> getMovies() {
        return buildList(getModels(jacz.store.database.models.Movie.class));
    }

    public static Movie getMovieById(int id) {
        Model model = getModelById(jacz.store.database.models.Movie.class, id);
        return model != null ? new Movie(model) : null;
    }

    static List<Movie> buildList(List<? extends Model> models) {
        List<Movie> movies = new ArrayList<>();
        for (Model model : models) {
            if (model != null) {
                movies.add(new Movie(model));
            }
        }
        return movies;
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
        List<jacz.store.database.models.VideoFile> modelVideoFiles = getAssociation(jacz.store.database.models.VideoFile.class, jacz.store.database.models.MoviesVideoFiles.class);
        return VideoFile.buildList(modelVideoFiles);
    }

    public <C extends Model> void removeVideoFiles() {
        removeAssociations(jacz.store.database.models.MoviesVideoFiles.class, getAssociationIdField(), null);
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
