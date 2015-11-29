package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Chapters to VideoFiles associative relation (no VideoFiles are shared with other items)
 */
@Table("chapters_video_files")
public class ChaptersVideoFiles extends Model {

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }

    static void deleteRecords(String field, Object id) {
        List<ChaptersVideoFiles> chaptersVideoFilesModels = ChaptersVideoFiles.where(field + " = ?", id);
        for (ChaptersVideoFiles chaptersVideoFiles : chaptersVideoFilesModels) {
            if (field.equals("chapter_id")) {
                // also delete the actual video file
                VideoFile videoFile = VideoFile.findFirst("id = ?", chaptersVideoFiles.get("video_file_id"));
                if (videoFile != null) {
                    videoFile.delete();
                }
            }
            chaptersVideoFiles.delete();
        }
    }
}