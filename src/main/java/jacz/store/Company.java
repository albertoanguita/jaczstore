package jacz.store;

import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Company extends NamedLibraryItem {

    public Company() {
        super();
    }

    Company(Model model) {
        super(model);
    }

    static List<Company> buildList(List<? extends Model> models) {
        List<Company> companies = new ArrayList<>();
        for (Model model : models) {
            companies.add(new Company(model));
        }
        return companies;
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Company();
    }
}
