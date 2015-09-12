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
 * e.g. "Disney", "Pixar", "Warner", "20th Century Fox", "Canal+", "Sony", "BMG"
 */
public class CompanyCreator extends Creator {

    public CompanyCreator(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(Libraries.COMPANY_CREATOR_LIBRARY, itemInterface, values, itemIsNew);
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
        return new ArrayList<>();
    }

    @Override
    protected void initValues() {
        super.initValues();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        if (!(anotherItem instanceof CompanyCreator)) {
            return -1f;
        }
        return super.match(anotherItem);
    }

    @Override
    public void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof CompanyCreator)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        super.mergeData(anotherItem, identifierMap);
    }
}
