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

    private int minutes;

    private List<VideoFile> videoFiles;

    public Movie() {
        super();
    }

    @Override
    protected Model buildModel() {
        return null;
    }


}
