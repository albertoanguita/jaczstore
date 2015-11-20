package jacz.store;

import jacz.store.database.models.*;
import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class NamedLibraryItem extends LibraryItem {

    private String name;

    private List<String> aliases;

    public NamedLibraryItem() {
        super();
    }

    public NamedLibraryItem(Model model) {
        super(model);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }
}
