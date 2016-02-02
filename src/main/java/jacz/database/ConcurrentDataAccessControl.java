package jacz.database;

import jacz.util.concurrency.concurrency_controller.ConcurrencyController;
import jacz.util.concurrency.concurrency_controller.ConcurrencyControllerAction;
import jacz.util.maps.ObjectCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Concurrency controller implementation for the access control of different database
 */
public class ConcurrentDataAccessControl implements ConcurrencyControllerAction {

    private final static Logger logger = LoggerFactory.getLogger(ConcurrentDataAccessControl.class);

    private static ConcurrentDataAccessControl instance = null;

    private final ConcurrencyController concurrencyController;

    public static synchronized ConcurrentDataAccessControl getInstance() {
        if (instance == null) {
            instance = new ConcurrentDataAccessControl();
        }
        return instance;
    }

    private ConcurrentDataAccessControl() {
        concurrencyController = new ConcurrencyController(this);
    }

    public ConcurrencyController getConcurrencyController() {
        return concurrencyController;
    }

    @Override
    public int maxNumberOfExecutionsAllowed() {
        // no limit
        return 0;
    }

    @Override
    public int getActivityPriority(String database) {
        // same priority for all database
        return 0;
    }

    @Override
    public boolean activityCanExecute(String database, ObjectCount<String> numberOfExecutionsOfActivities) {
        if (numberOfExecutionsOfActivities.getTotalCount() == 0) {
            // no database open
            logger.info("activityCanExecute with db " + database + ": no activities executing");
            return true;
        } else {
            Set<String> openDatabases = numberOfExecutionsOfActivities.objectSet();
            logger.info("activityCanExecute with db " + database + ": activities executing -> " + openDatabases);
            if (openDatabases.size() == 1 && openDatabases.iterator().next().equals(database)) {
                // this database is already open
                return true;
            }
        }
        return false;
    }

    @Override
    public void activityIsGoingToBegin(String database, ObjectCount<String> numberOfExecutionsOfActivities) {
        // ignore
    }

    @Override
    public void activityHasEnded(String database, ObjectCount<String> numberOfExecutionsOfActivities) {
        // ignore
    }
}
