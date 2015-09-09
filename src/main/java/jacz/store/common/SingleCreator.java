package jacz.store.common;

import jacz.store.IllegalDataException;
import jacz.store.ItemInterface;
import jacz.store.Libraries;
import jacz.store.db_mediator.CorruptDataException;
import jacz.store.db_mediator.DBException;
import jacz.util.AI.inference.Mycin;
import jacz.util.bool.MutableBoolean;

/**
 * Single creator, e.g. Freddie Mercury
 *
 * todo remove
 */
public class SingleCreator  {
//
//    private static final String PERSON = "person";
//
//    private String person;
//
////    public SingleCreator(Database database) throws DBException, IOException {
////        super("SingleCreator", database);
////    }
////
////    public SingleCreator(Database database, String identifier) throws DBException, IOException {
////        super("SingleCreator", database, identifier);
////    }
//
//    public SingleCreator(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
//        super("", itemInterface, values, itemIsNew);
//    }
//
//    public List<String> getFields() {
//        List<String> fields = super.getFields();
//        fields.add(PERSON);
//        return fields;
//    }
//
//    @Override
//    protected void initValues() {
//        super.initValues();
//        person = null;
//    }
//
//    @Override
//    protected void loadValues(Map<String, String> values) throws ParseException {
//        super.loadValues(values);
//        person = values.get(PERSON);
//    }
//
//    //    public static SingleCreator buildItem(Database database, Map<String, String> values) throws ParseException {
////        return new SingleCreator(database, values);
////    }
//
//    public String getPerson() {
//        return person;
//    }
//
//    public void setPerson(String person) throws DBException, IOException {
//        this.person = updateField(this.person, person, PERSON);
//    }
//
//    @Override
//    public Float match(LibraryItem anotherItem) {
//        if (!(anotherItem instanceof SingleCreator)) {
//            return -1f;
//        }
//        SingleCreator anotherSingleCreator = (SingleCreator) anotherItem;
////        double personSimilarity = database.getLibrary(Database.Library.PERSON_LIBRARY).getItem(person).match(database.getLibrary(Database.Library.PERSON_LIBRARY).getItem(anotherSingleCreator.person));
//        float personSimilarity = 0;
//        try {
//            personSimilarity = itemInterface.getItem(Libraries.PERSON_LIBRARY, person).match(itemInterface.getItem(Libraries.PERSON_LIBRARY, anotherSingleCreator.person));
//        } catch (DBException e) {
//            // todo
//            e.printStackTrace();  //To change body of catch statement use OldFile | Settings | OldFile Templates.
//        } catch (CorruptDataException e) {
//            e.printStackTrace();  //To change body of catch statement use OldFile | Settings | OldFile Templates.
//        } catch (ParseException e) {
//            e.printStackTrace();  //To change body of catch statement use OldFile | Settings | OldFile Templates.
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use OldFile | Settings | OldFile Templates.
//        }
//        return (float) Mycin.combine(personSimilarity, super.match(anotherItem));
//    }
//
//    @Override
//    public boolean mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalDataException, DBException, ParseException, IOException {
//        // tags are added from anotherItem to this. Repetitions are checked
//        if (!(anotherItem instanceof SingleCreator)) {
//            throw new IllegalDataException("Invalid argument: " + anotherItem.getClass().getName());
//        }
//        SingleCreator o = (SingleCreator) anotherItem;
//        boolean result = super.mergeData(anotherItem, identifierMap);
//        MutableBoolean isChanged = new MutableBoolean(result);
//        person = mergeElement(person, o.person, isChanged);
//        return isChanged.isValue();
//    }
//
//    @Override
//    public boolean mergeCrossReferencesAux(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalDataException, DBException, ParseException, IOException {
//        // no cross reference data to merge
//        if (!(anotherItem instanceof GroupCreator)) {
//            throw new IllegalDataException("Invalid argument: " + anotherItem.getClass().getName());
//        }
//        return super.mergeCrossReferencesAux(anotherItem, identifierMap);
//    }
//
//    @Override
//    public void merge(LibraryItem anotherItem) {
//        super.merge(anotherItem);    //To change body of overridden methods use OldFile | Settings | OldFile Templates.
//    }
}
