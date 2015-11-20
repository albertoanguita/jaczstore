package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class SubtitleFile extends File {

    private LanguageCode language;

    public SubtitleFile() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.SubtitleFile();
    }

}
