package jacz.database;

import jacz.database.util.ItemIntegrator;
import jacz.util.AI.inference.Mycin;
import org.javalite.activejdbc.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class NamedItem extends DatabaseItem {

//    private String name;

//    private List<String> aliases;

    public NamedItem(String dbPath) {
        super(dbPath);
    }

    public NamedItem(String dbPath, String name) {
        super(dbPath, buildInitialValues(name));
    }

    private static Map<DatabaseMediator.Field, Object> buildInitialValues(String name) {
        Map<DatabaseMediator.Field, Object> initialValues = new HashMap<>();
        initialValues.put(DatabaseMediator.Field.NAME, name);
        return  initialValues;
    }

    public NamedItem(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public NamedItem(Model model, String dbPath) {
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
    public float match(DatabaseItem anotherItem) {
        float similarity = super.match(anotherItem);
        // compute the highest similarity between all pairs of names (main name and aliases)
        NamedItem anotherNamedItem = (NamedItem) anotherItem;
        List<String> names1 = getAliases();
        names1.add(getName());
        List<String> names2 = anotherNamedItem.getAliases();
        names2.add(anotherNamedItem.getName());
        float maxNameSimilarity = -1;
        for (String name1 : names1) {
            for (String name2 : names2) {
                float aSimilarity = ItemIntegrator.personsNameSimilarity(name1, name2);
                maxNameSimilarity = aSimilarity > maxNameSimilarity ? aSimilarity : maxNameSimilarity;
            }
        }
        return Mycin.combine(similarity, maxNameSimilarity);
    }

    @Override
    public void mergeBasicPostponed(DatabaseItem anotherItem) {
        NamedItem anotherNamedItem = (NamedItem) anotherItem;
        if (getName() == null && anotherNamedItem.getName() != null) {
            setNamePostponed(anotherNamedItem.getName());
        }
        for (String alias : anotherNamedItem.getAliases()) {
            addAliasPostponed(alias);
        }
    }
}
