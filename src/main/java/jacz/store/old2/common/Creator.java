package jacz.store.old2.common;

import jacz.store.old2.Integrator;
import jacz.store.old2.ItemInterface;
import jacz.store.old2.db_mediator.DBException;
import jacz.util.AI.inference.Mycin;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The creator of an artistic creation (a singer, a movie director, a music group...)
 */
public abstract class Creator extends TaggedLibraryItemWithImages {

    public static final String NAME = "name";

    private String name;

    public Creator(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(container, itemInterface, values, itemIsNew);
    }

    public List<String> getFields() {
        List<String> fields = super.getFields();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = TaggedLibraryItemWithImages.getFieldsStatic();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(NAME);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        name = null;
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        name = values.get(NAME);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws DBException, IOException {
        this.name = updateField(this.name, name, NAME);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case NAME:
                return getName();

            default:
                return super.get(field);
        }
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        if (!(anotherItem instanceof Creator)) {
            return -1f;
        }
        Creator anotherCreator = (Creator) anotherItem;
        return (float) Mycin.combine(Integrator.genericNameSimilarity(name, anotherCreator.name), super.match(anotherItem));
    }

    @Override
    protected void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof Creator)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        Creator o = (Creator) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setName(mergeElement(name, o.name));
    }
}
