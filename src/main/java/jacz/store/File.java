package jacz.store;

import jacz.store.database_old.DatabaseMediator;
import jacz.store.old2.common.IdentifierMap;
import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class File extends LibraryItem {

//    private String hash;
//
//    private Long length;

    public File() {
        super();
    }

    public File(Model model) {
        super(model);
    }

    public String getHash() {
        return getString("hash");
    }

    public void setHash(String hash) {
        set("hash", hash);
    }

    public Long getLength() {
        return getLong("length");
    }

    public void setLength(Long length) {
        set("length", length);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    @Override
    public float match(LibraryItem anotherItem) {
        File anotherFile = (File) anotherItem;
        if (getHash() != null && anotherFile.getHash() != null && getHash().equals(anotherFile.getHash())) {
            return 1f;
        } else {
            return super.match(anotherItem);
        }
    }

    @Override
    public void merge(LibraryItem anotherItem) {
        File anotherFile = (File) anotherItem;
        if (getLength() == null && anotherFile.getLength() != null) {
            setLength(anotherFile.getLength());
        }
        if (getName() == null && anotherFile.getName() != null) {
            setName(anotherFile.getName());
        }
    }
}
