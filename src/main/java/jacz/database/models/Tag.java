package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;

/**
 * Tag model (table tags)
 */
@DbName(DatabaseMediator.DATABASE_NAME)
public class Tag extends Model {
}
