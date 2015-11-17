package jacz.store;

import jacz.store.database.DatabaseMediator;

/**
 * Created by Alberto on 16/11/2015.
 */
public class ImageFile extends File {

    public ImageFile(DatabaseMediator databaseMediator) {
        super(databaseMediator);
    }

    public ImageFile(DatabaseMediator databaseMediator, Integer id, int timestamp, String hash, Long length) {
        super(databaseMediator, id, timestamp, hash, length);
    }

    @Override
    public void save() {
        databaseMediator.saveImageFile(this);
    }

    @Override
    public void inflate() {

    }
}
