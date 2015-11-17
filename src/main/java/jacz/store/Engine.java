package jacz.store;

/**
 * Main interface to the system store. Provides a high-level description of the store,
 * and simple methods for querying, retrieving and modifying data.
 * <p/>
 * The underlying model is relational, handled by an SQLite engine.
 */
public class Engine {

    /**
     * @param path
     * @param version
     * @return
     */
    public static Connection createStore(String path, String version) {
        return null;
    }

    /**
     * @param path
     * @return
     */
    public static Connection connect(String path) {
        return null;
    }


}
