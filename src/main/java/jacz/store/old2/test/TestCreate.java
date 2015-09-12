package jacz.store.old2.test;

import jacz.store.old2.Database;
import jacz.store.old2.db_mediator.CSVDBMediator;
import jacz.store.old2.db_mediator.CorruptDataException;
import jacz.store.old2.db_mediator.DBException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alberto
 * Date: 19/01/14
 * Time: 12:34
 * To change this template use OldFile | Settings | OldFile Templates.
 */
public class TestCreate {

    public static void main(String[] args) throws DBException, IOException, CorruptDataException {

        Database.createEmpty(new CSVDBMediator(".\\etc\\CSVdatabase4\\"));


    }
}
