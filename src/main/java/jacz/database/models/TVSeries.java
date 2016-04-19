package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

/**
 * TVSeries model (table tv_series)
 */
@DbName(DatabaseMediator.DATABASE_NAME)
@Table("tv_series")
public class TVSeries extends Model {

    @Override
    public void beforeDelete() {
//        Chapter.deleteRecords(this);
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
