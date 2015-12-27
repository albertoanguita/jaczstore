package jacz.database;

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
    protected DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.TV_SERIES;
    }

    public static List<TVSeries> getTVSeries(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.TV_SERIES));
    }

    public static TVSeries getTVSeriesById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.TV_SERIES, id);
        return model != null ? new TVSeries(model, dbPath) : null;
    }

    public static List<TVSeries> getTVSeriesFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.TV_SERIES, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
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
        return Chapter.buildList(dbPath, getDirectAssociationChildren(jacz.database.models.Chapter.class));
    }

    public List<Chapter> getChapters(String season) {
        return Chapter.buildList(dbPath, getDirectAssociationChildren(jacz.database.models.Chapter.class, "season = ?", season));
    }

    public void removeChapters() {
        removeDirectAssociationChildren(jacz.database.models.Chapter.class);
    }

    public void setChapters(List<Chapter> chapters) {
        setDirectAssociationChildren(jacz.database.models.Chapter.class, chapters);
    }

    public void setChapters(Chapter... chapters) {
        setDirectAssociationChildren(jacz.database.models.Chapter.class, chapters);
    }

    public void addChapter(Chapter chapter) {
        addDirectAssociationChild(chapter);
    }

    @Override
    public float match(DatabaseItem anotherItem, ListSimilarity... listSimilarities) {
        float similarity = super.match(anotherItem, listSimilarities);
        for (ListSimilarity listSimilarity : listSimilarities) {
            switch (listSimilarity.referencedList) {
                case CHAPTERS:
                    return evaluateListSimilarity(listSimilarity, 1.0f);
            }
        }
        return similarity;
    }

    @Override
    public void merge(DatabaseItem anotherItem) {

    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {

    }
}
