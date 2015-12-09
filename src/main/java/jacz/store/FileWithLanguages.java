package jacz.store;

import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * todo remove strings
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
        removeList(DatabaseMediator.Field.LANGUAGES, true);
    }

    protected void removeLanguagesPostponed() {
        removeList(DatabaseMediator.Field.LANGUAGES, false);
    }

    protected boolean removeLanguage(LanguageCode language) {
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", true);
    }

    protected boolean removeLanguagePostponed(LanguageCode language) {
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", false);
    }

    protected void setLanguages(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, "name", true);
    }

    protected void setLanguagesPostponed(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, "name", false);
    }

    protected boolean addLanguage(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", true);
    }

    protected boolean addLanguagePostponed(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", false);
    }

    @Override
    public void merge(LibraryItem anotherItem) {
        super.merge(anotherItem);
        FileWithLanguages anotherFileWithLanguages = (FileWithLanguages) anotherItem;
        addEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, anotherFileWithLanguages.getLanguages(), "name", true);
    }

    @Override
    public void mergePostponed(LibraryItem anotherItem) {
        super.mergePostponed(anotherItem);
        FileWithLanguages anotherFileWithLanguages = (FileWithLanguages) anotherItem;
        addEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, anotherFileWithLanguages.getLanguages(), "name", false);
    }
}
