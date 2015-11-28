package jacz.store;

import jacz.store.database.models.*;
import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public class ImageFile extends File {

    public ImageFile() {
        super();
    }

    ImageFile(Model model) {
        super(model);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.ImageFile();
    }

    @Override
    public void delete() {
        super.delete();
//        List<Movie> movies = Movie.buildList(getDirectAssociationChildren(jacz.store.database.models.Movie.class));
//        for (Movie movie : movies) {
//            movie.set("image_file_id", -1);
//        }
//        List<TVSeries> tvSeries = TVSeries.buildList(getDirectAssociationChildren(jacz.store.database.models.TVSeries.class));
//        for (TVSeries oneTVSeries : tvSeries) {
//            oneTVSeries.set("image_file_id", -1);
//        }
    }
}
