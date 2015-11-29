package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Movies to VideoFiles associative relation (no VideoFiles are shared with other items)
 */
@Table("movies_video_files")
public class MoviesVideoFiles extends Model {

    @Override
    public void beforeDelete() {
        DeletedItem.addDeletedItem(this, getTableName());
    }

    static void deleteRecords(String field, Object id) {
        List<MoviesVideoFiles> moviesVideoFilesModels = MoviesVideoFiles.where(field + " = ?", id);
        for (MoviesVideoFiles moviesVideoFiles : moviesVideoFilesModels) {
            if (field.equals("movie_id")) {
                // also delete the actual video file
                VideoFile videoFile = VideoFile.findFirst("id = ?", moviesVideoFiles.get("video_file_id"));
                if (videoFile != null) {
                    videoFile.delete();
                }
            }
            moviesVideoFiles.delete();
        }
    }
}