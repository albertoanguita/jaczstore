package jacz.store.database.models;

import org.javalite.activejdbc.Model;

/**
 * Person model (table people)
 */
public class Person extends Model {

    @Override
    public void beforeDelete() {
        System.out.println("Person deleted!!!");
    }
}
