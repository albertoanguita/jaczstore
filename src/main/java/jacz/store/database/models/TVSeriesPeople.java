package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * TVSeries to Persons associative relation
 */
@Table("tv_series_people")
public class TVSeriesPeople extends Model {

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
        List<TVSeriesPeople> tvSeriesPeopleModels = where(field + " = ?", id);
        for (TVSeriesPeople tvSeriesPeople : tvSeriesPeopleModels) {
            tvSeriesPeople.delete();
        }
    }
}
