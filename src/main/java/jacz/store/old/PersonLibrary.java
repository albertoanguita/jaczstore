package jacz.store.old;

import jacz.store.Database;
import jacz.store.common.Person;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Persons library
 */
public class PersonLibrary extends ItemContainer<Person> {

    PersonLibrary(Database database, DBMediator dbMediator) {
        super("PersonLibrary", database, dbMediator);
    }

    @Override
    protected List<String> defineFields() {
        List<String> personFields = new ArrayList<>();
        personFields.add("identifier");
        personFields.add("name");
        personFields.add("aliases");
        return personFields;
    }

    @Override
    protected Person loadItem(Map<String, String> record) throws ParseException {
//        return new Person(database, record);
        return null;
    }

    @Override
    public Person buildNewItem() throws ParseException, DBException, IOException {
//        return new Person(database);
        return null;
    }
}
