package jacz.store.JavaLiteTest2;

import jacz.store.JavaLiteTest2.models.*;
import jacz.store.JavaLiteTest2.models.Movie;
import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 17/11/2015.
 */
public abstract class CreationItem extends LibraryItem {

    public CreationItem() {
        super();
    }

    public CreationItem(String title) {
        super();
        set("title", title, false);
    }

    public CreationItem(Model model) {
        super(model);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        set("title", title);
    }


}
