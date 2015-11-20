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
    String getAssociationIdField() {
        return "tv_series_id";
    }

}
