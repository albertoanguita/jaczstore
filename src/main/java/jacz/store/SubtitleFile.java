package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class SubtitleFile extends FileWithLanguages {

//    private LanguageCode language;

    public SubtitleFile(String dbPath) {
        super(dbPath);
    }

    SubtitleFile(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.SubtitleFile();
    }

    static List<SubtitleFile> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<SubtitleFile> subtitleFiles = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    subtitleFiles.add(new SubtitleFile(model, dbPath));
                }
            }
            return subtitleFiles;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }
}
