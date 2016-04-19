package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Metadata model (table metadata)
 */
@DbName(DatabaseMediator.DATABASE_NAME)
@Table("metadata")
public class Metadata extends Model {}
