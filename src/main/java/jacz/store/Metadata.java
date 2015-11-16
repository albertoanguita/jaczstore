package jacz.store;

import java.util.Date;

/**
 * Additional information about a database
 */
public class Metadata {

    private String databaseVersion;

    private String databaseIdentifier;

    private Date lastAccess;

    private Date lastUpdate;

    private Date creationDate;

    private int lastTimestamp;
}
