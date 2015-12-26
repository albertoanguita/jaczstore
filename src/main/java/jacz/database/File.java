package jacz.database;

import org.javalite.activejdbc.Model;

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

    @Override
    public float match(DatabaseItem anotherItem, ListSimilarity... listSimilarities) {
        File anotherFile = (File) anotherItem;
        if (getHash() != null && anotherFile.getHash() != null && getHash().equals(anotherFile.getHash())) {
            return 1f;
        } else {
            return super.match(anotherItem);
        }
    }

    @Override
    public void merge(DatabaseItem anotherItem) {
        File anotherFile = (File) anotherItem;
        if (getLength() == null && anotherFile.getLength() != null) {
            setLength(anotherFile.getLength());
        }
        if (getName() == null && anotherFile.getName() != null) {
            setName(anotherFile.getName());
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
