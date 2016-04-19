package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * SubtitleFile model (table subtitle_files)
 */
@DbName(DatabaseMediator.DATABASE_NAME)
@Table("subtitle_files")
public class SubtitleFile extends Model {

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }

//    static void deleteRecords(Model baseModel) {
//        List<SubtitleFile> subtitleFileModels = baseModel.getAll(SubtitleFile.class);
//        for (SubtitleFile subtitleFile : subtitleFileModels) {
//            subtitleFile.delete();
//        }
//    }
}
