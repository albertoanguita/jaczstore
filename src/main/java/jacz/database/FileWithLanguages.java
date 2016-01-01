package jacz.database;

import com.neovisionaries.i18n.LanguageCode;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 *
 */
public abstract class FileWithLanguages extends File {

    private static final String LANGUAGES_NAME_METHOD = "name";

    public FileWithLanguages(String dbPath) {
        super(dbPath);
    }

    public FileWithLanguages(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public FileWithLanguages(Model model, String dbPath) {
        super(model, dbPath);
    }

    public List<LanguageCode> getLanguages() {
        return getEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class);
    }

    public void removeLanguages() {
        removeList(DatabaseMediator.Field.LANGUAGES, true);
    }

    public void removeLanguagesPostponed() {
        removeList(DatabaseMediator.Field.LANGUAGES, false);
    }

    public boolean removeLanguage(LanguageCode language) {
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, LANGUAGES_NAME_METHOD, true);
    }

    public boolean removeLanguagePostponed(LanguageCode language) {
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, LANGUAGES_NAME_METHOD, false);
    }

    public void setLanguages(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, LANGUAGES_NAME_METHOD, true);
    }

    public void setLanguagesPostponed(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, LANGUAGES_NAME_METHOD, false);
    }

    public boolean addLanguage(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, LANGUAGES_NAME_METHOD, true);
    }

    public boolean addLanguagePostponed(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, LANGUAGES_NAME_METHOD, false);
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        super.mergePostponed(anotherItem);
        FileWithLanguages anotherFileWithLanguages = (FileWithLanguages) anotherItem;
        addEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, anotherFileWithLanguages.getLanguages(), LANGUAGES_NAME_METHOD, false);
    }
}
