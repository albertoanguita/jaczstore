package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class SubtitleFile extends FileWithLanguages {

//    private LanguageCode language;

    public SubtitleFile() {
        super();
    }

    public SubtitleFile(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.SubtitleFile();
    }

    static List<SubtitleFile> buildList(List<? extends Model> models) {
        List<SubtitleFile> subtitleFiles = new ArrayList<>();
        for (Model model : models) {
            if (model != null) {
                subtitleFiles.add(new SubtitleFile(model));
            }
        }
        return subtitleFiles;
    }
}
