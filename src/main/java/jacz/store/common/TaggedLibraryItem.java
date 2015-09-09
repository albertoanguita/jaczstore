package jacz.store.common;

import jacz.store.ItemInterface;
import jacz.store.db_mediator.DBException;
import jacz.util.AI.inference.Mycin;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Library items with a list of associated tags. The tag list does not support repetition (addition of the same item is ignored)
 */
public class TaggedLibraryItem extends LibraryItem {

    public static final String TAGS = "tags";

    private List<String> tags;

    protected TaggedLibraryItem(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(container, itemInterface, values, itemIsNew);
//        tags = initializeValueListIdentifier(TAGS, values);
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
        fields.add(TAGS);
        return fields;
    }

    @Override
    protected void initValues() {
        tags = new ArrayList<>();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        tags = initializeValueStringList(TAGS, values);
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        tags = addElementWithoutRepetition(tags, index, element, TAGS);
    }

    public void removeTag(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(tags, index, TAGS);
    }

    public void setTags(List<String> tags) throws DBException, IOException {
        this.tags = updateList(this.tags, tags, TAGS);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case TAGS:
                return getTags();

            default:
                return super.get(field);
        }
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        // no fields of this class indicate equality
        return (float) Mycin.combine(0d, super.match(anotherItem));
    }

    @Override
    protected void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof TaggedLibraryItem)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        TaggedLibraryItem o = (TaggedLibraryItem) anotherItem;
        setTags(mergeList(tags, o.tags));
    }
}
