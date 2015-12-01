package jacz.store;

import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 16/11/2015.
 */
public class ImageFile extends File {

    public ImageFile(String dbPath) {
        super(dbPath);
    }

    ImageFile(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.ImageFile();
    }
}
