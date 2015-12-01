package jacz.store;

import jacz.util.concurrency.concurrency_controller.ConcurrencyController;
import jacz.util.concurrency.concurrency_controller.ConcurrencyControllerAction;
import jacz.util.maps.ObjectCount;
import org.javalite.activejdbc.Base;

import java.util.Set;

/**
 * Concurrency controller implementation for the access control of different databases
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
        // same priority for all databases
        return 0;
    }

    @Override
    public boolean activityCanExecute(String database, ObjectCount<String> numberOfExecutionsOfActivities) {
        if (numberOfExecutionsOfActivities.getTotalCount() == 0) {
            // no databases open
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
    }

    @Override
    public void activityHasEnded(String database, ObjectCount<String> numberOfExecutionsOfActivities) {
        // ignore
    }
}
