package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

/**
 * Person model (table people)
 */
public class Person extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // delete people association records
            MoviesPeople.deleteRecords("person_id", getId());
            TVSeriesPeople.deleteRecords("person_id", getId());
            ChaptersPeople.deleteRecords("person_id", getId());
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
