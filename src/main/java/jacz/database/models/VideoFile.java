package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * VideoFile model (table video_files)
 */
@Table("video_files")
public class VideoFile extends Model {

    @Override
    public void beforeDelete() {
//        SubtitleFile.deleteRecords(this);
        DeletedItem.addDeletedItem(this, getTableName());
    }
}
