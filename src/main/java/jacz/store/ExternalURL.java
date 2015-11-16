package jacz.store;

/**
 * Created by Alberto on 15/09/2015.
 */
public final class ExternalURL extends LibraryItem {

    public enum Type {
        MOVIE,
        TV_SERIES,
        CHAPTER
    }

    private String URL;

    private Type type;
}
