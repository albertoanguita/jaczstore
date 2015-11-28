package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * VideoFile model (table video_files)
 */
@Table("video_files")
public class VideoFile extends Model {

    @Override
    public void beforeDelete() {
        System.out.println("VideoFile deleted!!!");
    }
}
