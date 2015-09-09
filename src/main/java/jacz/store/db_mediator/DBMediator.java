package jacz.store.db_mediator;

import jacz.store.Filter;
import jacz.util.lists.Duple;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Database interface
 */
public interface DBMediator {

    public void connect() throws IOException, CorruptDataException;

    public void disconnect() throws IOException;

    public void create(Map<String, List<String>> containers) throws IOException;

    public void add(String container, Map<String, String> fieldsAndValues) throws DBException, IOException;

    public int getColumnIndex(String container, String column) throws DBException;

    public Duple<List<String>, List<List<String>>> select(String container, List<Filter> filters) throws DBException;

    public void set(String container, List<Filter> filters, Map<String, String> fieldsAndValues, boolean firstOccurrence) throws DBException, IOException;

    public void remove(String container, List<Filter> filters, boolean firstOccurrence) throws DBException, IOException;

    public Date stringToDate(String s) throws ParseException;

    public String dateToString(Date d);

    public List<String> stringToList(String s) throws ParseException;

    public String listToString(List<?> list);
}
