package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class TVSeries extends ProducedCreationItem {

    private List<Chapter> chapters;

    public TVSeries() {
        super();
    }

}
