package jacz.store.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by Alberto on 12/09/2015.
 */
public class SQLiteMediator implements DatabaseMediator {

    private static final String VERSION = "0.1a";

    public SQLiteMediator(String path, boolean createDatabase) {
        // todo connect

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");

            if (createDatabase) {
                createDatabase(connection);
            }


        } catch (SQLException e) {
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

    public static void main(String[] args) {
        new SQLiteMediator("", true);
    }
}
