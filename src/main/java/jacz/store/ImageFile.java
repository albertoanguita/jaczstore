package jacz.store;

import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 16/11/2015.
 */
public class ImageFile extends File {

    public ImageFile() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.ImageFile();
    }

}
