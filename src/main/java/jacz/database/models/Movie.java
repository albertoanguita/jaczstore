package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;

/**
 * Movie model (table movies)
 */
@DbName(DatabaseMediator.DATABASE_NAME)
public class Movie extends Model {

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
