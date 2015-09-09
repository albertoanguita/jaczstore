package jacz.store;

import jacz.store.common.LibraryItem;
import jacz.store.db_mediator.CorruptDataException;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Methods for the library items
 */
public class ItemInterface {

    private final Database database;

    private final DBMediator dbMediator;

    ItemInterface(Database database, DBMediator dbMediator) {
        this.database = database;
        this.dbMediator = dbMediator;
    }

    public synchronized void updateFields(String container, String identifier, Map<String, String> fieldsAndValues) throws DBException, IOException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(LibraryItem.IDENTIFIER, Filter.Operator.EQUALS, identifier));
        dbMediator.set(container, filters, fieldsAndValues, true);
    }

    public Date stringToDate(String s) throws ParseException {
        return dbMediator.stringToDate(s);
    }

    public String dateToString(Date d) {
        return dbMediator.dateToString(d);
    }

    public List<String> stringToList(String s) throws ParseException {
        return dbMediator.stringToList(s);
    }

    public String listToString(List<?> list) {
        return dbMediator.listToString(list);
    }

    public LibraryItem getItem(String library, String id) throws DBException, CorruptDataException, ParseException, IOException {
        return database.getItem(library, id);
    }
}
