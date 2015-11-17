package jacz.store.JalaLiteTest;

import org.javalite.activejdbc.Model;

/**
 * Created by Alberto on 17/11/2015.
 */
public class Person extends Model {

    enum TYPE {actor, director}

    public Person() {
    }

    public Person(String name) {
        set("name", name);
    }
}
