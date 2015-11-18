package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database_old.DatabaseMediator;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class SubtitleFile extends File {

    private LanguageCode language;

    public SubtitleFile(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public SubtitleFile(
            DatabaseMediator databaseMediator,
            Integer id,
            int timestamp,
            String hash,
            Long length,
            LanguageCode language) {
        super(databaseMediator, id, timestamp, hash, length);
        this.language = language;
    }

    @Override
    public void save() {
        databaseMediator.saveSubtitleFile(this);
    }

    @Override
    public void inflate() {

    }
}
