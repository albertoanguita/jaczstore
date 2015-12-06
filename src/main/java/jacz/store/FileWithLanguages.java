package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 30/11/2015.
 */
public abstract class FileWithLanguages extends File {

    public FileWithLanguages(String dbPath) {
        super(dbPath);
    }

    public FileWithLanguages(Model model, String dbPath) {
        super(model, dbPath);
    }

    protected List<LanguageCode> getLanguages() {
        return getEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class);
    }

    protected void removeLanguages() {
        removeList(DatabaseMediator.Field.LANGUAGES);
    }

    protected boolean removeLanguage(LanguageCode language) {
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name");
    }

    protected void setLanguages(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, "name");
    }

    protected boolean addLanguage(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name");
    }

    @Override
    public void merge(LibraryItem anotherItem) {
        super.merge(anotherItem);
        FileWithLanguages anotherFileWithLanguages = (FileWithLanguages) anotherItem;
        addEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, anotherFileWithLanguages.getLanguages(), "name");
    }
}
