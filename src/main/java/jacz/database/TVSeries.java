package jacz.database;

import jacz.database.util.ItemIntegrator;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.*;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class TVSeries extends ProducedCreationItem {

//    private List<Chapter> chapters;

    private static final float CHAPTERS_SIMILARITY_CONFIDENCE = 1.0f;

    public TVSeries(String dbPath) {
        super(dbPath);
    }

    public TVSeries(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public TVSeries(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    public DatabaseMediator.ItemType getItemType() {
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
        LazyList<jacz.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.CHAPTER_LIST);
        return Chapter.buildList(dbPath, models);
    }

    public List<Chapter> getChapters(String season) {
        LazyList<jacz.database.models.Person> models = getReferencedElements(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.CHAPTER_LIST);
        return Chapter.buildList(dbPath, models, season);
    }

    public List<String> getChaptersIds() {
        return getReferencedElementsIds(DatabaseMediator.ItemType.CHAPTER, DatabaseMediator.Field.CHAPTER_LIST);
    }

    public void removeChapters() {
        removeReferencedElements(DatabaseMediator.Field.CHAPTER_LIST, true);
    }

    public void removeChaptersPostponed() {
        removeReferencedElements(DatabaseMediator.Field.CHAPTER_LIST, false);
    }

    public void removeChapter(Chapter chapter) {
        removeReferencedElement(DatabaseMediator.Field.CHAPTER_LIST, chapter, true);
    }

    public void removeChapterPostponed(Chapter chapter) {
        removeReferencedElement(DatabaseMediator.Field.CHAPTER_LIST, chapter, false);
    }

    public void setChapters(List<Chapter> chapters) {
        setReferencedElements(DatabaseMediator.Field.CHAPTER_LIST, chapters, true);
    }

    public void setChaptersPostponed(List<Chapter> chapters) {
        setReferencedElements(DatabaseMediator.Field.CHAPTER_LIST, chapters, false);
    }

    public void setChaptersIds(List<String> chapters) {
        setReferencedElementsIds(DatabaseMediator.Field.CHAPTER_LIST, chapters, true);
    }

    public void setChaptersIdsPostponed(List<String> chapters) {
        setReferencedElementsIds(DatabaseMediator.Field.CHAPTER_LIST, chapters, false);
    }

    public void setChapters(Chapter... chapters) {
        setReferencedElements(DatabaseMediator.Field.CHAPTER_LIST, true, chapters);
    }

    public void setChaptersPostponed(Chapter... chapters) {
        setReferencedElements(DatabaseMediator.Field.CHAPTER_LIST, false, chapters);
    }

    public void addChapter(Chapter chapter) {
        addReferencedElement(DatabaseMediator.Field.CHAPTER_LIST, chapter, true);
    }

    public void addChapterPostponed(Chapter chapter) {
        addReferencedElement(DatabaseMediator.Field.CHAPTER_LIST, chapter, false);
    }

    @Override
    public float match(DatabaseItem anotherItem) {
        float similarity = super.match(anotherItem);
        TVSeries anotherTVSeriesItem = (TVSeries) anotherItem;
        similarity = ItemIntegrator.addListSimilarity(similarity, getChaptersIds(), anotherTVSeriesItem.getChaptersIds(), CHAPTERS_SIMILARITY_CONFIDENCE);
        return similarity;
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        super.mergePostponed(anotherItem);
        TVSeries anotherTVSeriesItem = (TVSeries) anotherItem;
        for (Chapter chapter : anotherTVSeriesItem.getChapters()) {
            addChapterPostponed(chapter);
        }
    }
}
