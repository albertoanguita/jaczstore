package jacz.store.common;

import jacz.store.ItemInterface;
import jacz.store.Libraries;
import jacz.store.db_mediator.DBException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * A tag that can be applied to any library item
 */
public class Tag extends LibraryItem {

    public static final String VALUE = "value";

    private String value;

    public Tag(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(Libraries.TAG_LIBRARY, itemInterface, values, itemIsNew);
        value = initializeValueString(VALUE, values);
    }

    public List<String> getFields() {
        List<String> fields = super.getFields();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = LibraryItem.getFieldsStatic();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(VALUE);
        return fields;
    }

    @Override
    protected void initValues() {
        value = null;
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        value = initializeValueString(VALUE, values);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) throws DBException, IOException {
        this.value = updateField(this.value, value, VALUE);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case VALUE:
                return getValue();

            default:
                return super.get(field);
        }
    }

    @Override
    public void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof Tag)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        Tag o = (Tag) anotherItem;
        setValue(mergeElement(value, o.value));
    }
}
