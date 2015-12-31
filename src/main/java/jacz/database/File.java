package jacz.database;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class File extends DatabaseItem {

//    private String hash;
//
//    private Long length;

    public File(String dbPath) {
        super(dbPath);
    }

    public File(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public File(Model model, String dbPath) {
        super(model, dbPath);
    }

    public String getHash() {
        return getString(DatabaseMediator.Field.HASH);
    }

    public void setHash(String hash) {
        set(DatabaseMediator.Field.HASH, hash, true);
    }

    public void setHashPostponed(String hash) {
        set(DatabaseMediator.Field.HASH, hash, false);
    }

    public Long getLength() {
        return getLong(DatabaseMediator.Field.LENGTH);
    }

    public void setLength(Long length) {
        set(DatabaseMediator.Field.LENGTH, length, true);
    }

    public void setLengthPostponed(Long length) {
        set(DatabaseMediator.Field.LENGTH, length, false);
    }

    public String getName() {
        return getString(DatabaseMediator.Field.NAME);
    }

    public void setName(String name) {
        set(DatabaseMediator.Field.NAME, name, true);
    }

    public void setNamePostponed(String name) {
        set(DatabaseMediator.Field.NAME, name, false);
    }

    public List<String> getAdditionalSources() {
        return getStringList(DatabaseMediator.Field.ADDITIONAL_SOURCES);
    }

    public void removeAdditionalSources() {
        removeStringList(DatabaseMediator.Field.ADDITIONAL_SOURCES, true);
    }

    public void removeAdditionalSourcesPostponed() {
        removeStringList(DatabaseMediator.Field.ADDITIONAL_SOURCES, false);
    }

    public boolean removeAdditionalSource(String additionalSource) {
        return removeStringValue(DatabaseMediator.Field.ADDITIONAL_SOURCES, additionalSource, true);
    }

    public boolean removeAdditionalSourcePostponed(String additionalSource) {
        return removeStringValue(DatabaseMediator.Field.ADDITIONAL_SOURCES, additionalSource, false);
    }

    public void setAdditionalSources(List<String> additionalSources) {
        setStringList(DatabaseMediator.Field.ADDITIONAL_SOURCES, additionalSources, true);
    }

    public void setAdditionalSourcesPostponed(List<String> additionalSources) {
        setStringList(DatabaseMediator.Field.ADDITIONAL_SOURCES, additionalSources, false);
    }

    public boolean addAdditionalSource(String additionalSource) {
        return addStringValue(DatabaseMediator.Field.ADDITIONAL_SOURCES, additionalSource, true);
    }

    public boolean addAdditionalSourcePostponed(String additionalSource) {
        return addStringValue(DatabaseMediator.Field.ADDITIONAL_SOURCES, additionalSource, false);
    }

    @Override
    public float match(DatabaseItem anotherItem) {
        File anotherFile = (File) anotherItem;
        if (getHash() != null && anotherFile.getHash() != null && getHash().equals(anotherFile.getHash())) {
            return 1f;
        } else {
            return super.match(anotherItem);
        }
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        File anotherFile = (File) anotherItem;
        if (getLength() == null && anotherFile.getLength() != null) {
            setLengthPostponed(anotherFile.getLength());
        }
        if (getName() == null && anotherFile.getName() != null) {
            setNamePostponed(anotherFile.getName());
        }
    }
}
