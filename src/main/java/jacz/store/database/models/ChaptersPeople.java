package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Chapters to Persons associative relation
 */
@Table("chapters_people")
public class ChaptersPeople extends Model {

    @Override
    protected void afterCreate() {
        if (DatabaseMediator.mustAutoComplete()) {
            super.afterCreate();
            set("timestamp", DatabaseMediator.getNewTimestamp()).saveIt();
        }
    }

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }

    static void deleteRecords(String field, Object id) {
        List<ChaptersPeople> chaptersPeopleModels = where(field + " = ?", id);
        for (ChaptersPeople chaptersPeople : chaptersPeopleModels) {
            chaptersPeople.delete();
        }
    }
}
