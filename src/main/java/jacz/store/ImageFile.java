package jacz.store;

import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 16/11/2015.
 */
public class ImageFile extends File {

    public ImageFile() {
        super();
    }

    ImageFile(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.ImageFile();
    }
}
