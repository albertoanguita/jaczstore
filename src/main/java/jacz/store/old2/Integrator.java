package jacz.store.old2;

import jacz.store.old2.common.LibraryItem;
import jacz.store.old2.db_mediator.CorruptDataException;
import jacz.store.old2.db_mediator.DBException;
import jacz.store.old2.db_mediator.DBMediator;
import jacz.store.old.ItemContainer;

import java.io.IOException;
import java.util.Collection;

/**
 * Code for integrating disparate databases into a single one
 */
public class Integrator {

    /**
     * Threshold for considering two items equal
     */
    private static final float threshold = 0.95f;

    public static Database integrateDatabases(DBMediator integratedDBMediator, boolean loadInMemory, Collection<Database> databases) throws DBException, IOException, CorruptDataException {
        // we merge the databases one by one into a "central" database
        Database integratedDatabase = new Database(integratedDBMediator);
        for (Database oneDatabase : databases) {
            integrateDatabase(integratedDatabase, oneDatabase);
        }
        return integratedDatabase;
    }

    private static void integrateDatabase(Database integratedDatabase, Database oneDatabase) {
        // each item of oneDatabase is checked against each item of the integrated database, searching for a match
        // a match is determined by applying Mycin inference over a set of factors
        // we start by the most independent items (Persons, Creators...)
//        integrateLibrary(integratedDatabase.getPersonLibrary(), oneDatabase.getPersonLibrary());
//        integrateLibrary(integratedDatabase.getCreatorLibrary(), oneDatabase.getCreatorLibrary());
//        integrateLibrary(integratedDatabase.getSongLibrary(), oneDatabase.getSongLibrary());
//        integrateLibrary(integratedDatabase.getAudioAlbumLibrary(), oneDatabase.getAudioAlbumLibrary());
    }

    private static <T extends LibraryItem> void integrateLibrary(ItemContainer<T> integratedLibrary, ItemContainer<T> anotherLibrary) {
//        for (Map.Entry<String, T> onePerson : anotherLibrary.getAllItems()) {
//            boolean matchFound = false;
//            String integratedIdentifier = null;
//            String anotherIdentifier = null;
//            for (Map.Entry<String, T> integratedPerson : integratedLibrary.getAllItems()) {
//                if (isMatch(integratedPerson.getValue(), onePerson.getValue())) {
//                    // match found. Stop search and merge elements
//                    matchFound = true;
//                    integratedIdentifier = integratedPerson.getKey();
//                    anotherIdentifier = onePerson.getKey();
//                    break;
//                }
//            }
//            if (matchFound) {
//                // values must be merged
//                integratedLibrary.getItem(integratedIdentifier).merge(anotherLibrary.getItem(anotherIdentifier));
//                // todo we should now compare the new item with the rest of existing items to check if it must be further merged
//            }
//        }
    }

    private static boolean isMatch(LibraryItem value1, LibraryItem value2) {
        return value1.match(value2) >= threshold;
    }

    public static float genericNameSimilarity(String name1, String name2) {
        if (name1 != null && name2 != null) {
            if (name1.equals(name2)) {
                return 0.99f;
            } else if (name1.equalsIgnoreCase(name2)) {
                return 0.98f;
            } else {
                return -0.5f;
            }
        } else {
            return 0f;
        }
    }

    public static float personsNameSimilarity(String name1, String name2) {
        // todo include abbreviations (Will Smith, W. Smith).
        if (name1 != null && name2 != null) {
            if (name1.equals(name2)) {
                return 0.99f;
            } else if (name1.equalsIgnoreCase(name2)) {
                return 0.98f;
            } else {
                return -0.5f;
            }
        } else {
            return 0f;
        }
    }

    public static float creationsTitleSimilarity(String title1, String title2, String originalTitle1, String originalTitle2) {
        float titlesSimilarity = 0f;
        float originalTitlesSimilarity = 0f;
        float title1WithOriginalTitle2 = 0f;
        float title2WithOriginalTitle1 = 0f;
        if (title1 != null && title2 != null) {
            titlesSimilarity = titlesSimilarity(title1, title2);
        }
        if (originalTitle1 != null && originalTitle2 != null) {
            originalTitlesSimilarity = originalTitlesSimilarity(originalTitle1, originalTitle2);
        }
        return 0f;
    }

    public static float titlesSimilarity(String title1, String title2) {
        return 0f;
    }

    public static float originalTitlesSimilarity(String title1, String title2) {
        return 0f;
    }

    public static float eventsYearSimilarity(Integer year1, Integer year2) {
        if (year1 != null && year2 != null) {
            if (year1.equals(year2)) {
                return 0.05f;
            } else if (Math.abs(year1 - year2) == 1) {
                return -0.2f;
            } else {
                return -0.99f;
            }
        } else {
            return 0f;
        }
    }
}
