package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.*;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class TVSeries extends ProducedCreationItem {

//    private List<Chapter> chapters;

    public TVSeries(String dbPath) {
        super(dbPath);
    }

    public TVSeries(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.TVSeries();
    }

    public static List<TVSeries> getTVSeries(String dbPath) {
        return buildList(dbPath, getModels(dbPath, jacz.store.database.models.TVSeries.class));
    }

    public static TVSeries getTVSeriesById(String dbPath, int id) {
        Model model = getModelById(dbPath, jacz.store.database.models.TVSeries.class, id);
        return model != null ? new TVSeries(model, dbPath) : null;
    }

    static List<TVSeries> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<TVSeries> tvSeries = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    tvSeries.add(new TVSeries(model, dbPath));
                }
            }
            return tvSeries;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public List<Person> getCreators() {
        return super.getCreatorsDirectors();
    }

    public <C extends Model> void removeCreators() {
        super.removeCreatorsDirectors();
    }

    public <C extends Model> void removeCreator(Person person) {
        removeCreatorDirector(person);
    }

    public void setCreators(List<Person> persons) {
        super.setCreatorsDirectors(persons);
    }

    public void setCreators(Person... persons) {
        super.setCreatorsDirectors(persons);
    }

    public <C extends Model> void addCreator(Person person) {
        addCreatorDirector(person);
    }

    public List<String> getSeasons() {
        List<Chapter> chapters = getChapters();
        return getSeasons(chapters);
    }

    private static List<String> getSeasons(List<Chapter> chapters) {
        Set<String> seasonSet = new HashSet<>();
        for (Chapter chapter : chapters) {
            seasonSet.add(chapter.getSeason());
        }
        List<String> seasons = new ArrayList<>(seasonSet);
        Collections.sort(seasons);
        return seasons;
    }

    public List<Chapter> getChapters() {
        return Chapter.buildList(dbPath, getDirectAssociationChildren(jacz.store.database.models.Chapter.class));
    }

    public List<Chapter> getChapters(String season) {
        return Chapter.buildList(dbPath, getDirectAssociationChildren(jacz.store.database.models.Chapter.class, "season = ?", season));
    }

    public void removeChapters() {
        removeDirectAssociationChildren(jacz.store.database.models.Chapter.class);
    }

    public void setChapters(List<Chapter> chapters) {
        setDirectAssociationChildren(jacz.store.database.models.Chapter.class, chapters);
    }

    public void setChapters(Chapter... chapters) {
        setDirectAssociationChildren(jacz.store.database.models.Chapter.class, chapters);
    }

    public void addChapter(Chapter chapter) {
        addDirectAssociationChild(chapter);
    }
}
