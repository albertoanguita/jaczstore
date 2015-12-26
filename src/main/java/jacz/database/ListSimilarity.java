package jacz.database;

import org.jetbrains.annotations.NotNull;

/**
 * Stores the similarities of items between two list of items of the same type. Used for match computations.
 */
public class ListSimilarity implements Comparable<ListSimilarity> {

    public final DatabaseMediator.ReferencedList referencedList;

    public final int firstListSize;

    public final int secondListSize;

    public final int commonItems;

    public ListSimilarity(DatabaseMediator.ReferencedList referencedList, int firstListSize, int secondListSize, int commonItems) {
        this.referencedList = referencedList;
        this.firstListSize = firstListSize;
        this.secondListSize = secondListSize;
        this.commonItems = commonItems;
    }

    ListSimilarity(int firstListSize, int secondListSize, int commonItems) {
        this.referencedList = null;
        this.firstListSize = firstListSize;
        this.secondListSize = secondListSize;
        this.commonItems = commonItems;
    }

    @Override
    public int compareTo(@NotNull ListSimilarity o) {
        return referencedList.compareTo(o.referencedList);
    }
}
