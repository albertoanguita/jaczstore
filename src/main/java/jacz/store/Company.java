package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Company extends NamedLibraryItem {

    public Company(String dbPath) {
        super(dbPath);
    }

    Company(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Company();
    }

    static List<Company> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<Company> companies = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    companies.add(new Company(model, dbPath));
                }
            }
            return companies;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public static List<Company> getCompanies(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.COMPANY));
    }

    public List<Movie> getMovies(String dbPath) {
        List<jacz.store.database.models.Movie> modelMovies = getElementsContainingMe(DatabaseMediator.ItemType.MOVIE, DatabaseMediator.Field.COMPANY_LIST);
        return Movie.buildList(dbPath, modelMovies);
    }

    public List<TVSeries> getTVSeries(String dbPath) {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getElementsContainingMe(DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.COMPANY_LIST);
        return TVSeries.buildList(dbPath, modelTVSeries);
    }

    @Override
    public void delete() {
        super.delete();
    }
}
