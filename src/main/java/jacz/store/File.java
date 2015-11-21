package jacz.store;

import jacz.store.database_old.DatabaseMediator;
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
}
