package jacz.database;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tag api
 */
public class Tag {

    public static Set<String> getAllTags(String dbPath) {
        // todo
        DatabaseMediator.connect(dbPath);
        List<? extends Model> models = DatabaseItem.getModels(dbPath, DatabaseMediator.ItemType.TAG);
        try {
            Set<String> tags = new HashSet<>();
            for (Model model : models) {
                if (model != null) {
                    tags.add(model.getString(DatabaseMediator.Field.NAME.value));
                }
            }
            return tags;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public static List<Movie> getMoviesWithTag(String dbPath, String tag) {
        LazyList<? extends Model> movies = getModelsWithTag(dbPath, tag, DatabaseMediator.ItemType.MOVIE);
        if (movies != null) {
            return Movie.buildList(dbPath, movies);
        } else {
            return new ArrayList<>();
        }
    }

    public static List<TVSeries> getTVSeriesWithTag(String dbPath, String tag) {
        LazyList<? extends Model> tvSeries = getModelsWithTag(dbPath, tag, DatabaseMediator.ItemType.TV_SERIES);
        if (tvSeries != null) {
            return TVSeries.buildList(dbPath, tvSeries);
        } else {
            return new ArrayList<>();
        }
    }

    public static List<Chapter> getChaptersWithTag(String dbPath, String tag) {
        LazyList<? extends Model> chapters = getModelsWithTag(dbPath, tag, DatabaseMediator.ItemType.CHAPTER);
        if (chapters != null) {
            return Chapter.buildList(dbPath, chapters);
        } else {
            return new ArrayList<>();
        }
    }

    public static List<Person> getPeopleWithTag(String dbPath, String tag) {
        LazyList<? extends Model> people = getModelsWithTag(dbPath, tag, DatabaseMediator.ItemType.PERSON);
        if (people != null) {
            return Person.buildList(dbPath, people);
        } else {
            return new ArrayList<>();
        }
    }

    public static List<Company> getCompaniesWithTag(String dbPath, String tag) {
        LazyList<? extends Model> companies = getModelsWithTag(dbPath, tag, DatabaseMediator.ItemType.COMPANY);
        if (companies != null) {
            return Company.buildList(dbPath, companies);
        } else {
            return new ArrayList<>();
        }
    }

    private static LazyList<? extends Model> getModelsWithTag(String dbPath, String tag, DatabaseMediator.ItemType type) {
        DatabaseMediator.connect(dbPath);
        try {
            List<jacz.database.models.Tag> tags =
                    jacz.database.models.Tag.where(
                            DatabaseMediator.Field.ITEM_TYPE.value + " = ?" +
                                    " AND " + DatabaseMediator.Field.NAME.value + " = ?",
                            type.name(),
                            tag);
            Object[] ids = new Object[tags.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = tags.get(i).getString(DatabaseMediator.Field.ITEM_ID.value);
            }
            String query = "id = -1";
            for (int i = 0; i < ids.length; i++) {
                query += " OR id = ?";
            }
            Method where = type.modelClass.getMethod("where", String.class, Object[].class);
            return  (LazyList<? extends Model>) where.invoke(null, query, ids);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            DatabaseMediator.reportError("Error retrieving models with tag", dbPath, tag, type, e);
            return null;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public static List<String> getItemTags(String dbPath, DatabaseItem item, DatabaseMediator.ItemType type) {
        DatabaseMediator.connect(dbPath);
        try {
            LazyList<jacz.database.models.Tag> tagRelations = jacz.database.models.Tag.where(
                    DatabaseMediator.Field.ITEM_TYPE.value + " = ?" +
                            " AND " + DatabaseMediator.Field.ITEM_ID.value + " = ?",
                    type.name(),
                    item.getId());
            List<String> tags = new ArrayList<>();
            for (jacz.database.models.Tag tagRelation : tagRelations) {
                tags.add(tagRelation.getString((DatabaseMediator.Field.NAME.value)));
            }
            return tags;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public static boolean addTag(String dbPath, DatabaseItem item, String tag, DatabaseMediator.ItemType type) {
        DatabaseMediator.connect(dbPath);
        try {
            jacz.database.models.Tag tagRelation = findTag(tag, type);
            if (tagRelation == null) {
                tagRelation = new jacz.database.models.Tag();
                tagRelation.setString(DatabaseMediator.Field.ITEM_TYPE.value, type.name());
                tagRelation.setString(DatabaseMediator.Field.ITEM_ID.value, item.getId());
                tagRelation.setString(DatabaseMediator.Field.NAME.value, tag);
                tagRelation.saveIt();
                return true;
            } else {
                return false;
            }
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    public static boolean removeTag(String dbPath, DatabaseItem item, String tag, DatabaseMediator.ItemType type) {
        DatabaseMediator.connect(dbPath);
        try {
            jacz.database.models.Tag tagRelation = findTag(tag, type);
            if (tagRelation != null) {
                tagRelation.delete();
                return true;
            } else {
                return false;
            }
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    private static jacz.database.models.Tag findTag(String tag, DatabaseMediator.ItemType type) {
        return jacz.database.models.Tag.findFirst(
                DatabaseMediator.Field.ITEM_TYPE.value + " = ?" +
                        " AND " + DatabaseMediator.Field.NAME.value + " = ?",
                type.name(),
                tag);
    }

}
