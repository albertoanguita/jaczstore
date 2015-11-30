package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Movies to Persons associative relation
 */
@Table("movies_people")
public class MoviesPeople extends Model {

    @Override
    protected void afterCreate() {
        super.afterCreate();
        set("timestamp", DatabaseMediator.getNewTimestamp()).saveIt();
    }

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }

    static void deleteRecords(String field, Object id) {
        List<MoviesPeople> moviesPeopleModels = where(field + " = ?", id);
        for (MoviesPeople moviesPeople : moviesPeopleModels) {
            moviesPeople.delete();
        }
    }
}
