package jacz.store.old2.db_mediator;

import jacz.store.old2.Filter;
import jacz.util.files.FileUtil;
import jacz.util.io.csv.CSV;
import jacz.util.io.object_serialization.Serializer;
import jacz.util.lists.tuple.Duple;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CSV implementation of the database mediator interface
 */
public class CSVDBMediator implements DBMediator {

    private static final String FILE_START = "CSV_";

    private static final String FILE_END = ".csv";

    private static final String separator = "º";

    private static final String nullValue = "@null@";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");

    private final String baseDir;

    /**
     * Each CSV file that forms our CSV database, indexed by the CSV container name
     */
    private Map<String, CSV> csvMap;

    public CSVDBMediator(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void connect() throws IOException, CorruptDataException {
        // load CSV files into memory
        try {
            File dir = new File(baseDir);
            if (dir.isDirectory()) {
                File[] csvFiles = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.startsWith(FILE_START) && name.endsWith(FILE_END);
                    }
                });
                csvMap = new HashMap<>();
                for (File csvFile : csvFiles) {
                    String containerName = csvFile.getName().substring(FILE_START.length(), csvFile.getName().length() - FILE_END.length());
                    csvMap.put(containerName, CSV.load(csvFile.getPath(), separator, nullValue));
                }
            } else {
                throw new IOException("Database files not accessible: " + baseDir);
            }
        } catch (ParseException e) {
            throw new CorruptDataException(e.getMessage());
        }
    }

    @Override
    public void disconnect() throws IOException {
//        for (String name : csvMap.keySet()) {
//            String path = buildCSVPath(baseDir, name);
//            csvMap.get(name).save(path, nullValue);
//        }
        // not needed
    }

    private String buildCSVPath(String baseDir, String name) {
        return FileUtil.generatePath(FILE_START + name + FILE_END, baseDir);
    }

    @Override
    public void create(Map<String, List<String>> containers) throws IOException {
        FileUtil.createDirectory(baseDir);
        for (String container : containers.keySet()) {
            CSV csv = new CSV(containers.get(container), separator);
            String path = buildCSVPath(baseDir, container);
            csv.save(path, nullValue);
        }
    }

    @Override
//    public void add(String container, Map<String, String> fieldsAndValues, String defaultValue) throws DBException, IOException {
//        CSV csv = csvMap.get(container);
//        if (csv == null) {
//            throw new DBException("Invalid container name: " + container);
//        }
//        // build the new record
//        Map<String, String> newRecord = new HashMap<>();
//        for (String column : csv.getColumns()) {
//            newRecord.put(column, defaultValue);
//        }
//        for (Map.Entry<String, String> entry : fieldsAndValues.entrySet()) {
//            newRecord.put(entry.getKey(), entry.getValue());
//        }
//        // add the new record to the data set
//        csv.getData().add(newRecord);
//        String path = buildCSVPath(baseDir, container);
//        csv.save(path, nullValue);
//    }

    public void add(String container, Map<String, String> fieldsAndValues) throws DBException, IOException {
        CSV csv = csvMap.get(container);
        if (csv == null) {
            throw new DBException("Invalid container name: " + container);
        }
        // build the new record
        List<String> row = new ArrayList<>();
        for (int i = 0; i < csv.getColumns().size(); i++) {
            row.add(null);
        }
        for (Map.Entry<String, String> entry : fieldsAndValues.entrySet()) {
            row.set(csv.getColumnIndex(entry.getKey()), entry.getValue());
        }
        // add the new record to the data set
        csv.addRow(row);
        String path = buildCSVPath(baseDir, container);
        csv.save(path, nullValue);
    }

    @Override
    public int getColumnIndex(String container, String column) throws DBException {
        CSV csv = csvMap.get(container);
        if (csv == null) {
            throw new DBException("Invalid container name: " + container);
        }
        return csv.getColumnIndex(column);
    }

//    private boolean fulfillsFilters(Map<String, String> record, List<Filter> filters) {
//        for (Filter filter : filters) {
//            if (!fulfillsFilter(record.get(filter.field), filter)) {
//                return false;
//            }
//        }
//        return true;
//    }

    private boolean fulfillsFilter(String value, Filter filter) {
        switch (filter.operator) {

            case EQUALS:
                return value.equals(filter.value);

            case NOT_EQUALS:
                return !value.equals(filter.value);

            case LESS:
                return Float.parseFloat(value) < Float.parseFloat(filter.value);

            case LESS_THAN:
                return Float.parseFloat(value) <= Float.parseFloat(filter.value);

            case GREATER:
                return Float.parseFloat(value) > Float.parseFloat(filter.value);

            case GREATER_THAN:
                return Float.parseFloat(value) >= Float.parseFloat(filter.value);

            case CONTAINS:
                return value.contains(filter.value);

            default:
                throw new IllegalArgumentException("Invalid filter operator: " + filter.operator);
        }
    }

    @Override
//    public List<Map<String, String>> select(String container, List<String> fields, List<Filter> filters) throws DBException {
//        List<Map<String, String>> values = new ArrayList<>();
//        CSV csv = csvMap.get(container);
//        if (csv == null) {
//            throw new DBException("Invalid container name: " + container);
//        }
//        for (Map<String, String> record : csv.getData()) {
//            if (fulfillsFilters(record, filters)) {
//                Map<String, String> retrievedRecord = new HashMap<>();
//                for (String field : fields) {
//                    retrievedRecord.put(field, record.get(field));
//                }
//                values.add(retrievedRecord);
//            }
//        }
//        return values;
//    }

    public Duple<List<String>, List<List<String>>> select(String container, List<Filter> filters) throws DBException {
        List<List<String>> data = new ArrayList<>();
        CSV csv = csvMap.get(container);
        if (csv == null) {
            throw new DBException("Invalid container name: " + container);
        }
        Iterator<List<String>> it = csv.getDataIterator();
        while (it.hasNext()) {
            List<String> row = it.next();
            if (fulfillsFilters(csv, row, filters)) {
                data.add(row);
            }
        }
        return new Duple<>(csv.getColumns(), data);
    }

    private boolean fulfillsFilters(CSV csv, List<String> row, List<Filter> filters) {
        for (Filter filter : filters) {
            if (!fulfillsFilter(row.get(csv.getColumnIndex(filter.field)), filter)) {
                return false;
            }
        }
        return true;
    }

    @Override
//    public void set(String container, List<Filter> filters, Map<String, String> fieldsAndValues) throws DBException, IOException {
//        CSV csv = csvMap.get(container);
//        if (csv == null) {
//            throw new DBException("Invalid container name: " + container);
//        }
//        boolean hasChanged = false;
//        for (Map<String, String> record : csv.getData()) {
//            if (fulfillsFilters(record, filters)) {
//                // record found
//                for (String field : fieldsAndValues.keySet()) {
//                    record.put(field, fieldsAndValues.get(field));
//                }
//                hasChanged = true;
//            }
//        }
//        if (hasChanged) {
//            String path = buildCSVPath(baseDir, container);
//            csv.save(path, nullValue);
//        }
//    }

    public void set(String container, List<Filter> filters, Map<String, String> fieldsAndValues, boolean firstOccurrence) throws DBException, IOException {
        CSV csv = csvMap.get(container);
        if (csv == null) {
            throw new DBException("Invalid container name: " + container);
        }
        boolean hasChanged = false;
        Iterator<List<String>> it = csv.getDataIterator();
        while (it.hasNext()) {
            List<String> row = it.next();
            if (fulfillsFilters(csv, row, filters)) {
                // record found
                for (String field : fieldsAndValues.keySet()) {
                    row.set(csv.getColumnIndex(field), fieldsAndValues.get(field));
                }
                hasChanged = true;
                if (firstOccurrence) {
                    break;
                }
            }
        }
        if (hasChanged) {
            String path = buildCSVPath(baseDir, container);
            csv.save(path, nullValue);
        }
    }

    @Override
//    public void remove(String container, List<Filter> filters) throws DBException, IOException {
//        CSV csv = csvMap.get(container);
//        if (csv == null) {
//            throw new DBException("Invalid container name: " + container);
//        }
//        List<Map<String, String>> data = csv.getData();
//        boolean hasChanged = false;
//        int i = 0;
//        while (i < data.size()) {
//            if (fulfillsFilters(data.get(i), filters)) {
//                csv.getData().remove(i);
//                hasChanged = true;
//            } else {
//                i++;
//            }
//        }
//        if (hasChanged) {
//            String path = buildCSVPath(baseDir, container);
//            csv.save(path, nullValue);
//        }
//    }

    public void remove(String container, List<Filter> filters, boolean firstOccurrence) throws DBException, IOException {
        CSV csv = csvMap.get(container);
        if (csv == null) {
            throw new DBException("Invalid container name: " + container);
        }
        boolean hasChanged = false;
        Iterator<List<String>> it = csv.getDataIterator();
        while (it.hasNext()) {
            List<String> row = it.next();
            if (fulfillsFilters(csv, row, filters)) {
                // record found
                it.remove();
                hasChanged = true;
                if (firstOccurrence) {
                    break;
                }
            }
        }
        if (hasChanged) {
            String path = buildCSVPath(baseDir, container);
            csv.save(path, nullValue);
        }
    }

    @Override
    public Date stringToDate(String s) throws ParseException {
        return simpleDateFormat.parse(s);
    }

    @Override
    public String dateToString(Date d) {
        return simpleDateFormat.format(d);
    }

    @Override
    public List<String> stringToList(String s) throws ParseException {
        if (s != null) {
            return Serializer.deserializeListFromReadableString(s, "<<", ">>");
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String listToString(List<?> list) {
        return Serializer.serializeListToReadableString(list, "<<", ">>");
    }
}
