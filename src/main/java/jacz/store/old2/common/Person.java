package jacz.store.old2.common;

import jacz.store.old2.Integrator;
import jacz.store.old2.ItemInterface;
import jacz.store.old2.Libraries;
import jacz.store.old2.db_mediator.DBException;
import jacz.util.AI.inference.Mycin;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A person, represented by its name and optional aliases
 */
public class Person extends Creator {

    public static final String ALIASES = "aliases";

    private List<String> aliases;

    public Person(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(Libraries.PERSON_LIBRARY, itemInterface, values, itemIsNew);
    }

    public List<String> getFields() {
        List<String> fields = super.getFields();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = Creator.getFieldsStatic();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(ALIASES);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        aliases = new ArrayList<>();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        aliases = itemInterface.stringToList(values.get(ALIASES));
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addAlias(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        aliases = addElementWithoutRepetition(aliases, index, element, ALIASES);
    }

    public void removeAlias(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(aliases, index, ALIASES);
    }

    public void setAliases(List<String> aliases) throws DBException, IOException {
        this.aliases = updateList(this.aliases, aliases, ALIASES);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case ALIASES:
                return getAliases();

            default:
                return super.get(field);
        }
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        if (!(anotherItem instanceof Person)) {
            return -1f;
        }
        Person anotherPerson = (Person) anotherItem;
        float nameSimilarity = -1f;
        for (String myName : getAllNames()) {
            for (String anotherName : anotherPerson.getAllNames()) {
                nameSimilarity = Math.max(nameSimilarity, Integrator.personsNameSimilarity(myName, anotherName));
            }
        }
        return (float) Mycin.combine(nameSimilarity, super.match(anotherItem));
    }

    private List<String> getAllNames() {
        List<String> names = new ArrayList<String>();
        if (getName() != null) {
            names.add(getName());
        }
        if (aliases != null) {
            names.addAll(aliases);
        }
        return names;
    }

    @Override
    protected void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        // tags are added from anotherItem to this. Repetitions are checked
        if (!(anotherItem instanceof Person)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        Person o = (Person) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setAliases(mergeList(aliases, o.aliases));
    }
}
