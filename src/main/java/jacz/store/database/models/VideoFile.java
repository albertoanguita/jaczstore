package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * VideoFile model (table video_files)
 */
@Table("video_files")
public class VideoFile extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            // delete association records
//            MoviesVideoFiles.deleteRecords("video_file_id", getId());
//            ChaptersVideoFiles.deleteRecords("video_file_id", getId());
            // delete subtitle files
            SubtitleFile.deleteRecords(this);
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }
}
