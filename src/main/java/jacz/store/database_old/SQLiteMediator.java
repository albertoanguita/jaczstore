package jacz.store.database_old;

import java.sql.*;

/**
 * Created by Alberto on 12/09/2015.
 */
public class SQLiteMediator {

    private static final String VERSION = "0.1a";

//    private static final String METADATA = "metadata";
//    private static final String MOVIE = "movie";
//    private static final String TV_SERIES = "tv_series";
//    private static final String CHAPTER = "chapter";
//    private static final String PERSON = "person";
//    private static final String COMPANY = "company";
//    private static final String VIDEO_FILE = "video_file";
//    private static final String SUBTITLE_FILE = "subtitle_file";
//    private static final String IMAGE_FILE = "image_file";
//
//    private static final String VERSION = "version";
//    private static final String CREATION_DATE = "creationDate";
//    private static final String LAST_READ = "lastRead";
//    private static final String LAST_UPDATE = "lastUpdate";
//    private static final String ID = "id";
//    private static final String LAST_TIMESTAMP = "lastTimestamp";
//    private static final String TITLE = "title";
//    private static final String ORIGINAL_TITIE = "originalTitle";
//    private static final String YEAR = "year";
//    private static final String M = "movie";
//    private static final String M = "movie";
//    private static final String M = "movie";
//    private static final String M = "movie";



    private Connection connection;

    public SQLiteMediator(String path, boolean createDatabase) {
        // todo connect

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");

            if (createDatabase) {
                createDatabase(connection);
            }


        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }

    private void createDatabase(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate(
                "CREATE TABLE metadata (" +
                        "version TEXT, " +
                        "creationDate DATETIME DEFAULT (datetime('now','localtime')), " +
                        "lastRead DATETIME DEFAULT (datetime('now','localtime')), " +
                        "lastUpdate DATETIME DEFAULT (datetime('now','localtime')) " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE movie (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT, " +
                        "originalTitle TEXT, " +
                        "year INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE tv_series (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "title TEXT, " +
                        "originalTitle TEXT, " +
                        "year INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE chapter (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "title TEXT, " +
                        "originalTitle TEXT, " +
                        "year INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE person (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "name TEXT, " +
                        "aliases TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE company (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "name TEXT, " +
                        "aliases TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE video_file (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "name TEXT, " +
                        "hash TEXT, " +
                        "length INTEGER " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE subtitle_file (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "hash TEXT " +
                        ")"
        );
        connection.createStatement().executeUpdate(
                "CREATE TABLE image_file (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "hash TEXT " +
                        ")"
        );
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO metadata (version) VALUES (?)"
        );
        preparedStatement.setString(1, VERSION);
        preparedStatement.executeUpdate();

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        ContentValues initialValues = new ContentValues();
//        initialValues.put("date_created", dateFormat.format(date));
//        long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    private ResultSet getTable(String table) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM " + table);
    }

//    @Override
//    public List<Movie> getMovies() throws SQLException {
//        ResultSet resultSet = getTable("movie");
//        List<Movie> movies = new ArrayList<>();
//        while (resultSet.next()) {
////            int id  = rs.getInt("id");
////            int age = rs.getInt("age");
////            String first = rs.getString("first");
////            String last = rs.getString("last");
//
//            movies.add(new Movie());
//        }
//    }
//
//    @Override
//    public List<TVSeries> getTVSeries() throws SQLException {
//        ResultSet resultSet = getTable("tv_series");
//    }
//
//    @Override
//    public List<Person> getPersons() throws SQLException {
//        ResultSet resultSet = getTable("person");
//    }
//
//    @Override
//    public List<Company> getCompanies() throws SQLException {
//        ResultSet resultSet = getTable("company");
//    }




    public static void main(String[] args) {
        new SQLiteMediator("", true);
    }

}
