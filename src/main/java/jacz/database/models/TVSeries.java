package jacz.database.models;

import jacz.database.DatabaseMediator;
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
            Chapter.deleteRecords(this);
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
