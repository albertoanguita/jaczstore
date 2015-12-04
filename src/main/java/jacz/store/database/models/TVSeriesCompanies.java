package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Movies to Companies associative relation
 */
@Table("tv_series_companies")
public class TVSeriesCompanies extends Model {

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
        List<TVSeriesCompanies> tvSeriesCompaniesModels = where(field + " = ?", id);
        for (TVSeriesCompanies tvSeriesCompanies : tvSeriesCompaniesModels) {
            tvSeriesCompanies.delete();
        }
    }
}
