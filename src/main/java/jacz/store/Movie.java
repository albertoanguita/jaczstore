package jacz.store;

import jacz.store.database.DatabaseMediator;
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
    protected Model buildModel() {
        return new jacz.store.database.models.Movie();
    }

    public static List<Movie> getMovies(String dbPath) {
        return buildList(dbPath, getModels(dbPath, jacz.store.database.models.Movie.class));
    }

    public static Movie getMovieById(String dbPath, int id) {
        Model model = getModelById(dbPath, jacz.store.database.models.Movie.class, id);
        return model != null ? new Movie(model, dbPath) : null;
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


//    @Override
//    Class<? extends Model> getPeopleAssociationModel() {
////        return jacz.store.database.models.MoviesPeople.class;
//        return null;
//    }

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
        LazyList<jacz.store.database.models.VideoFile> models = getReferencedElements(jacz.store.database.models.VideoFile.class, "video_file_list");
        return VideoFile.buildList(dbPath, models);
    }

    public <C extends Model> void removeVideoFiles() {
        removeReferencedElements("video_file_list");
    }

    public <C extends Model> void removeVideoFile(VideoFile videoFile) {
        removeReferencedElement("video_file_list", videoFile);
    }

    public void setVideoFiles(List<VideoFile> videoFiles) {
        setReferencedElements("video_file_list", videoFiles);
    }

    public void setVideoFiles(VideoFile... videoFiles) {
        setReferencedElements("video_file_list", videoFiles);
    }

    public <C extends Model> void addVideoFile(VideoFile videoFile) {
        addReferencedElement("video_file_list", videoFile);
    }
}
