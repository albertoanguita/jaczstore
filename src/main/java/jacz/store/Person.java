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

    Person(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Person();
    }


    static List<Person> buildList(List<? extends Model> models) {
        List<Person> persons = new ArrayList<>();
        for (Model model : models) {
            if (model != null) {
                persons.add(new Person(model));
            }
        }
        return persons;
    }

    @Override
    public void delete() {
        super.delete();
        // todo delete associations
        removeAssociations(jacz.store.database.models.MoviesPeople.class, "person_id", null);
        removeAssociations(jacz.store.database.models.TVSeriesPeople.class, "person_id", null);
        removeAssociations(jacz.store.database.models.ChaptersPeople.class, "person_id", null);
    }
}
