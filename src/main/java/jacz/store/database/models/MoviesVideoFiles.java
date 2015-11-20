package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Movies to VideoFiles associative relation (no VideoFiles are shared with other items)
 */
@Table("movies_video_files")
public class MoviesVideoFiles extends Model {}