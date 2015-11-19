package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;

import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class ProducedCreationItem extends CreationItem {

    private List<Company> ProductionCompanies;

    private List<GenreCode> genres;

    private ImageFile imageFile;

    public ProducedCreationItem() {
        super();
    }

}
