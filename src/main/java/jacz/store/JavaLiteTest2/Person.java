package jacz.store.JavaLiteTest2;

import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 17/11/2015.
 */
public class Person extends LibraryItem {

    public Person(String name) {
        super();
        set("name", name, true);
    }

    private Person(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return null;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }



    static List<Person> buildList(List<? extends Model> models) {
        List<Person> persons = new ArrayList<>();
        for (Model model : models) {
            persons.add(new Person(model));
        }
        return persons;
    }

//    public static List<Person> findAll() {
//        return buildList(jacz.store.JavaLiteTest2.models.Person.findAll());
//    }
//
//    public static Person findById(Object id) {
//        return new Person(jacz.store.JavaLiteTest2.models.Person.findById(id));
//    }
}
