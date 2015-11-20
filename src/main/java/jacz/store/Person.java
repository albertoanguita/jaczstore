package jacz.store;

import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Person extends NamedLibraryItem {

    public Person() {
        super();
    }

    public Person(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Person();
    }


    static List<Person> buildList(List<? extends Model> models) {
        List<Person> persons = new ArrayList<>();
        for (Model model : models) {
            persons.add(new Person(model));
        }
        return persons;
    }

}
