package jacz.store;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class NamedLibraryItem extends LibraryItem {

//    private String name;

//    private List<String> aliases;

    public NamedLibraryItem() {
        super();
    }

    public NamedLibraryItem(Model model) {
        super(model);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public List<String> getAliases() {
        return deserializeList(getString("aliases"));
    }

    public void removeAliases() {
        set("aliases", null);
    }

    public boolean removeAlias(String alias) {
        List<String> Aliases = getAliases();
        boolean removed = Aliases.remove(alias);
        setAliases(Aliases);
        return removed;
    }

    public void setAliases(List<String> aliases) {
        set("aliases", serializeList(aliases));
    }

    public boolean addAlias(String alias) {
        List<String> aliases = getAliases();
        boolean notContains = !aliases.contains(alias);
        if (notContains) {
            aliases.add(alias);
        }
        setAliases(aliases);
        return notContains;
    }
}
