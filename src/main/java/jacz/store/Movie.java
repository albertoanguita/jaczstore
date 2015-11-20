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

    private List<VideoFile> videoFiles;

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
    String getAssociationIdField() {
        return "movie_id";
    }

    protected List<Person> getDirectors() {
        return getCreatorsDirectors();
    }

    protected <C extends Model> void removeDirectors() {
        removeCreatorsDirectors();
    }

    protected void setDirectors(List<Person> persons) {
        setCreatorsDirectors(persons);
    }

    protected void setDirectors(Person... persons) {
        setCreatorsDirectors(persons);
    }

    protected <C extends Model> void addDirectors(List<Person> persons) {
        addCreatorDirectors(persons);
    }

    protected <C extends Model> void addDirectors(Person... persons) {
        addCreatorDirectors(persons);
    }

    protected <C extends Model> void addDirector(Person person) {
        addCreatorDirector(person);
    }

    public Integer getMinutes() {
        return getInteger("minutes");
    }

    public void setMinutes(Integer minutes) {
        set("minutes", minutes);
    }


}
