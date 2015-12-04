package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * TVSeries model (table tv_series)
 */
@Table("tv_series")
public class TVSeries extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // delete image
            ImageFile.deleteRecord(this);
            // delete people association records
            TVSeriesPeople.deleteRecords("tv_series_id", getId());
            // delete companies association records
            TVSeriesCompanies.deleteRecords("tv_series_id", getId());
            // delete chapters
            Chapter.deleteRecords(this);
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }

    static void deleteImageLink(Model imageModel) {
        List<TVSeries> tvSeriesModels = imageModel.getAll(TVSeries.class);
        for (TVSeries tvSeries : tvSeriesModels) {
            tvSeries.set("image_file_id", null);
            tvSeries.saveIt();
        }
    }
}
