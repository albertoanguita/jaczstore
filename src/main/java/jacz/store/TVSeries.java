package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class TVSeries extends ProducedCreationItem {

    private List<Chapter> chapters;

    public TVSeries() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.TVSeries();
    }

    @Override
    Class<? extends Model> getPeopleAssociationModel() {
        return jacz.store.database.models.TVSeriesPeople.class;
    }

    @Override
    Class<? extends Model> getCompanyAssociationModel() {
        return jacz.store.database.models.TVSeriesCompanies.class;
    }

    @Override
    String getAssociationIdField() {
        return "tv_series_id";
    }

    public List<Person> getCreators() {
        return getCreatorsDirectors();
    }

    public <C extends Model> void removeCreators() {
        removeCreatorsDirectors();
    }

    public void setCreators(List<Person> persons) {
        setCreatorsDirectors(persons);
    }

    public void setCreators(Person... persons) {
        setCreatorsDirectors(persons);
    }

    public <C extends Model> void addCreators(List<Person> persons) {
        addCreatorDirectors(persons);
    }

    public <C extends Model> void addCreators(Person... persons) {
        addCreatorDirectors(persons);
    }

    public <C extends Model> void addCreators(Person person) {
        addCreatorDirector(person);
    }


}
