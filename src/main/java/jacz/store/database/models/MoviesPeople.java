package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Movies to Persons associative relation
 */
@Table("movies_people")
public class MoviesPeople extends Model {

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
