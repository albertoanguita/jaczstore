package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

import java.util.List;

/**
 * Chapter model (table chapters)
 */
@BelongsTo(parent = TVSeries.class, foreignKeyName = "tv_series_id")
public class Chapter extends Model {

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }

//    static void deleteRecords(Model baseModel) {
//        if (DatabaseMediator.mustAutoComplete()) {
//            List<Chapter> chapterModels = baseModel.getAll(Chapter.class);
//            for (Chapter chapter : chapterModels) {
//                chapter.delete();
//            }
//        }
//    }
}
