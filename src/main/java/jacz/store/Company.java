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

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Company();
    }

    static List<Company> buildList(List<? extends Model> models) {
        List<Company> companies = new ArrayList<>();
        for (Model model : models) {
            if (model != null) {
                companies.add(new Company(model));
            }
        }
        return companies;
    }

    public static List<Company> getCompanies() {
        return buildList(getModels(jacz.store.database.models.Company.class));
    }

    public List<Movie> getMovies() {
        List<jacz.store.database.models.Movie> modelMovies = getAssociation(jacz.store.database.models.Movie.class, jacz.store.database.models.MoviesPeople.class);
        return Movie.buildList(modelMovies);
    }

    public List<TVSeries> getTVSeries() {
        List<jacz.store.database.models.TVSeries> modelTVSeries = getAssociation(jacz.store.database.models.TVSeries.class, jacz.store.database.models.TVSeriesPeople.class);
        return TVSeries.buildList(modelTVSeries);
    }


}
