package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * SubtitleFile model (table subtitle_files)
 */
@Table("subtitle_files")
public class SubtitleFile extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
//            // delete people association records
//            ChaptersPeople.deleteRecords("chapter_id", getId());
//            // delete video files
//            ChaptersVideoFiles.deleteRecords("chapter_id", getId());
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }

    static void deleteRecords(Model baseModel) {
        List<SubtitleFile> subtitleFileModels = baseModel.getAll(SubtitleFile.class);
        for (SubtitleFile subtitleFile : subtitleFileModels) {
            subtitleFile.delete();
        }
    }
}
