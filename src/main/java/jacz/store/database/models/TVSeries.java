package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * TVSeries model (table tv_series)
 */
@Table("tv_series")
public class TVSeries extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // delete people association records
//            TVSeriesPeople.deleteRecords("tv_series_id", getId());
            // delete companies association records
//            TVSeriesCompanies.deleteRecords("tv_series_id", getId());
            // delete chapters
            Chapter.deleteRecords(this);
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
