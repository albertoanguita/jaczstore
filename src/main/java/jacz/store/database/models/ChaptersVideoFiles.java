package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Chapters to VideoFiles associative relation (no VideoFiles are shared with other items)
 */
@Table("chapters_video_files")
public class ChaptersVideoFiles extends Model {}