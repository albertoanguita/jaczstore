package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

/**
 * Movie model (table movies)
 */
public class Movie extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // todo delete from tag table
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
