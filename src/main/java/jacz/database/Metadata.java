package jacz.database;

import java.util.Date;

/**
 * Additional information about a database
 */
public class Metadata {

    private String databaseVersion;

    private String databaseIdentifier;

    private Date creationDate;

    private Date lastAccess;

    private Date lastUpdate;

    private int lastTimestamp;
}
