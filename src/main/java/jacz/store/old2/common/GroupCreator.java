package jacz.store.old2.common;

import jacz.store.old2.ItemInterface;
import jacz.store.old2.Libraries;
import jacz.store.old2.db_mediator.DBException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Group creator, e.g. U2, Queen, R.E.M
 */
public class GroupCreator extends Creator {

    // todo remove
    public static final String PERSONS = "persons";

    private List<String> persons;

    public GroupCreator(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(Libraries.GROUP_CREATOR_LIBRARY, itemInterface, values, itemIsNew);
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
        fields.add(PERSONS);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        persons = new ArrayList<>();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        persons = initializeValueStringList(PERSONS, values);
    }

    public List<String> getPersons() {
        return persons;
    }

    public void addPerson(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        persons = addElementWithoutRepetition(persons, index, element, PERSONS);
    }

    public void removePerson(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(persons, index, PERSONS);
    }

    public void setPersons(List<String> persons) throws DBException, IOException {
        this.persons = updateList(this.persons, persons, PERSONS);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case PERSONS:
                return getPersons();

            default:
                return super.get(field);
        }
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        return super.match(anotherItem);
    }

    @Override
    public void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        // tags are added from anotherItem to this. Repetitions are checked
        if (!(anotherItem instanceof GroupCreator)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        GroupCreator o = (GroupCreator) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setPersons(mergeList(persons, o.persons));
    }
}
