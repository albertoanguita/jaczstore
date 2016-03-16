package jacz.database.models;

import org.javalite.activejdbc.Model;

/**
 * Movie model (table movies)
 */
public class Movie extends Model {

    @Override
    public void beforeDelete() {
        // todo delete from tag table
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
