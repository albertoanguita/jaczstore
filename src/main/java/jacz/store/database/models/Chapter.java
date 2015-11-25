package jacz.store.database.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

/**
 * Chapter model (table chapters)
 */
@BelongsTo(parent = TVSeries.class, foreignKeyName = "tv_series_id")
public class Chapter extends Model {
}
