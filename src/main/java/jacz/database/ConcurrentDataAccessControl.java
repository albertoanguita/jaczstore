package jacz.database;

import jacz.util.concurrency.concurrency_controller.ConcurrencyController;
import jacz.util.concurrency.concurrency_controller.ConcurrencyControllerAction;
import jacz.util.maps.ObjectCount;

import java.util.Set;

/**
 * Concurrency controller implementation for the access control of different database
 * todo remove
 */
public class ConcurrentDataAccessControl implements ConcurrencyControllerAction {

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
            return true;
        } else {
            Set<String> openDatabases = numberOfExecutionsOfActivities.objectSet();
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
        System.out.println("CONNECTION WITH " + database);
    }

    @Override
    public void activityHasEnded(String database, ObjectCount<String> numberOfExecutionsOfActivities) {
        // ignore
        System.out.println("DISCONNECTED FROM " + database);
    }
}
