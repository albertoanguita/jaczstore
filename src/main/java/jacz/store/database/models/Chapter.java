package jacz.store.database.models;

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
        // delete people association records
        ChaptersPeople.deleteRecords("chapter_id", getId());
        // delete video files
        ChaptersVideoFiles.deleteRecords("chapter_id", getId());
        DeletedItem.addDeletedItem(this, getTableName());
    }

    static void deleteRecords(Model baseModel) {
        List<Chapter> chapterModels = baseModel.getAll(Chapter.class);
        for (Chapter chapter : chapterModels) {
            chapter.delete();
        }
    }
}
