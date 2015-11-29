package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 30/11/2015.
 */
public abstract class FileWithLanguages extends File {

    public FileWithLanguages() {
        super();
    }

    public FileWithLanguages(Model model) {
        super(model);
    }

    protected List<LanguageCode> getLanguages() {
        return getEnums("languages", LanguageCode.class);
    }

    protected void removeLanguages() {
        removeList("languages");
    }

    protected boolean removeLanguage(LanguageCode language) {
        return removeEnum("languages", LanguageCode.class, language, "name");
    }

    protected void setLanguages(List<LanguageCode> languages) {
        setEnums("languages", LanguageCode.class, languages, "name");
    }

    protected boolean addLanguage(LanguageCode language) {
        return addEnum("languages", LanguageCode.class, language, "name");
    }
}
