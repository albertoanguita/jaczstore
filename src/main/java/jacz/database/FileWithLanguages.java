package jacz.database;

import com.neovisionaries.i18n.LanguageCode;
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
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", true);
    }

    public boolean removeLanguagePostponed(LanguageCode language) {
        return removeEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", false);
    }

    public void setLanguages(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, "name", true);
    }

    public void setLanguagesPostponed(List<LanguageCode> languages) {
        setEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, languages, "name", false);
    }

    public boolean addLanguage(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", true);
    }

    public boolean addLanguagePostponed(LanguageCode language) {
        return addEnum(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, language, "name", false);
    }

    @Override
    public void merge(DatabaseItem anotherItem) {
        super.merge(anotherItem);
        FileWithLanguages anotherFileWithLanguages = (FileWithLanguages) anotherItem;
        addEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, anotherFileWithLanguages.getLanguages(), "name", true);
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        super.mergePostponed(anotherItem);
        FileWithLanguages anotherFileWithLanguages = (FileWithLanguages) anotherItem;
        addEnums(DatabaseMediator.Field.LANGUAGES, LanguageCode.class, anotherFileWithLanguages.getLanguages(), "name", false);
    }
}
