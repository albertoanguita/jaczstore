package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class NamedLibraryItem extends LibraryItem {

//    private String name;

//    private List<String> aliases;

    public NamedLibraryItem(String dbPath) {
        super(dbPath);
    }

    public NamedLibraryItem(Model model, String dbPath) {
        super(model, dbPath);
    }

    public String getName() {
        return getString(DatabaseMediator.Field.NAME);
    }

    public void setName(String name) {
        set(DatabaseMediator.Field.NAME, name, true);
    }

    public void setNamePostponed(String name) {
        set(DatabaseMediator.Field.NAME, name, false);
    }

    public List<String> getAliases() {
        return getStringList(DatabaseMediator.Field.ALIASES);
    }

    public void removeAliases() {
        removeStringList(DatabaseMediator.Field.ALIASES, true);
    }

    public void removeAliasesPostponed() {
        removeStringList(DatabaseMediator.Field.ALIASES, false);
    }

    public boolean removeAlias(String alias) {
        return removeStringValue(DatabaseMediator.Field.ALIASES, alias, true);
    }

    public boolean removeAliasPostponed(String alias) {
        return removeStringValue(DatabaseMediator.Field.ALIASES, alias, false);
    }

    public void setAliases(List<String> aliases) {
        setStringList(DatabaseMediator.Field.ALIASES, aliases, true);
    }

    public void setAliasesPostponed(List<String> aliases) {
        setStringList(DatabaseMediator.Field.ALIASES, aliases, false);
    }

    public boolean addAlias(String alias) {
        return addStringValue(DatabaseMediator.Field.ALIASES, alias, true);
    }

    public boolean addAliasPostponed(String alias) {
        return addStringValue(DatabaseMediator.Field.ALIASES, alias, false);
    }

    @Override
    public float match(LibraryItem anotherItem, ListSimilarity... listSimilarities) {
        NamedLibraryItem anotherNamedItem = (NamedLibraryItem) anotherItem;
        List<String> names1 = getAliases();
        names1.add(getName());
        List<String> names2 = anotherNamedItem.getAliases();
        names2.add(anotherNamedItem.getName());
        float maxSimilarity = -1;
        for (String name1 : names1) {
            for (String name2 : names2) {
                float similarity = ItemIntegrator.personsNameSimilarity(name1, name2);
                maxSimilarity = similarity > maxSimilarity ? similarity : maxSimilarity;
            }
        }
        return maxSimilarity;
    }

    @Override
    public void merge(LibraryItem anotherItem) {
        NamedLibraryItem anotherNamedItem = (NamedLibraryItem) anotherItem;
        if (getName() == null && anotherNamedItem.getName() != null) {
            setName(anotherNamedItem.getName());
        }
        for (String alias : anotherNamedItem.getAliases()) {
            addAlias(alias);
        }
    }

    @Override
    public void mergePostponed(LibraryItem anotherItem) {
        NamedLibraryItem anotherNamedItem = (NamedLibraryItem) anotherItem;
        if (getName() == null && anotherNamedItem.getName() != null) {
            setNamePostponed(anotherNamedItem.getName());
        }
        for (String alias : anotherNamedItem.getAliases()) {
            addAliasPostponed(alias);
        }
    }
}
