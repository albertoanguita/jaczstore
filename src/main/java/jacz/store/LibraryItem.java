package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;
import jacz.store.database.DatabaseMediator;
import jacz.store.util.GenreCode;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Alberto on 12/09/2015.
 */
public abstract class LibraryItem {

    private static final String LIST_SEPARATOR = "\n";

    private Model model;

    private int timestamp;

    public LibraryItem() {
        this.model = buildModel();
        updateTimestamp();
        save();
    }

    LibraryItem(Model model) {
        this.model = model;
    }

    protected abstract Model buildModel();

    public Integer getId() {
        return (Integer) model.getId();
    }

    public String getString(String field) {
        return model.getString(field);
    }

    public Integer getInteger(String field) {
        return model.getInteger(field);
    }

    public Long getLong(String field) {
        return model.getLong(field);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        // todo
    }

    protected void set(String field, Object value) {
//        set(field, value, true);
        model.set(field, value);
        save();
    }

//    protected void set(String field, Object value, boolean save) {
//        model.set(field, value);
//        if (save) {
//            save();
//        }
//    }

    protected void save() {
        model.saveIt();
    }

    protected <C extends Model> Model getDirectAssociation(Class<? extends Model> ownedClass) {
        return model.parent(ownedClass);
    }

    protected void setDirectAssociation(LibraryItem item) {
        item.model.add(model);
    }

    protected <C extends Model> LazyList<C> getAssociation(Class<C> clazz) {
        return model.getAll(clazz);
    }

    protected <C extends Model> LazyList<C> getAssociation(Class<C> clazz, String query, Object... params) {
        return model.get(clazz, query, params);
    }

    protected <C extends Model> void removeAssociations(Class<? extends Model> modelClass, String idField, String type) {
        try {
            Method where = modelClass.getMethod("where", String.class, Object[].class);
            List<C> associations;
            if (type == null) {
                associations = (List<C>) where.invoke(null, idField + " = '" + getId() + "' AND type = '" + type + "'", new Object[0]);
            } else {
                associations = (List<C>) where.invoke(null, idField + " = '" + getId() + "'");
            }
            for (C association : associations) {
                association.delete();
            }
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        }
    }

    protected <C extends Model> void setAssociationList(Class<? extends Model> modelClass, String idField, String type, List<? extends LibraryItem> items) {
        removeAssociations(modelClass, idField, type);
        addAssociationList(modelClass, idField, type, items);
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String type, LibraryItem... items) {
        removeAssociations(modelClass, idField, type);
        addAssociations(modelClass, idField, type, items);
    }

    protected <C extends Model> void addAssociationList(Class<? extends Model> modelClass, String idField, String type, List<? extends LibraryItem> items) {
        for (LibraryItem item : items) {
            addAssociation(modelClass, idField, type, item);
        }
    }

    protected <C extends Model> void addAssociations(Class<? extends Model> modelClass, String idField, String type, LibraryItem... items) {
        for (LibraryItem item : items) {
            addAssociation(modelClass, idField, type, item);
        }
    }

    protected <C extends Model> void addAssociation(Class<? extends Model> modelClass, String idField, String type, LibraryItem item) {
        try {
            Model associativeModel = modelClass.newInstance();
            associativeModel.set(idField, getId());
            associativeModel.set("person_id", item.getId());
            if (type != null) {
                associativeModel.set("type", type);
            }
            associativeModel.saveIt();
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        }
    }

    protected List<CountryCode> getCountries() {
        String countryList = getString("countries");
        List<CountryCode> countries = new ArrayList<>();
        for (String countryCode : deserializeList(countryList)) {
            countries.add(CountryCode.valueOf(countryCode));
        }
        return countries;
    }

    protected void setCountries(List<CountryCode> countries) {
        List<String> countryList = new ArrayList<>();
        for (CountryCode countryCode : countries) {
            countryList.add(countryCode.getAlpha2());
        }
        set("countries", serializeList(countryList));
    }

    protected List<String> getExternalURLs() {
        return deserializeList(getString("externalURLs"));
    }

    protected void setExternalURLs(List<String> externalURLs) {
        set("externalURLs", serializeList(externalURLs));
    }

    protected List<GenreCode> getGenres() {
        String genreList = getString("genres");
        List<GenreCode> genres = new ArrayList<>();
        for (String genreCode : deserializeList(genreList)) {
            genres.add(GenreCode.valueOf(genreCode));
        }
        return genres;
    }

    protected void setGenres(List<GenreCode> genres) {
        List<String> genreList = new ArrayList<>();
        for (GenreCode genreCode : genres) {
            genreList.add(genreCode.name());
        }
        set("genres", serializeList(genreList));
    }

    protected List<LanguageCode> getLanguages() {
        String languageList = getString("languages");
        List<LanguageCode> languages = new ArrayList<>();
        for (String languageCode : deserializeList(languageList)) {
            languages.add(LanguageCode.valueOf(languageCode));
        }
        return languages;
    }

    protected void setLanguages(List<LanguageCode> languages) {
        List<String> languageList = new ArrayList<>();
        for (LanguageCode languageCode : languages) {
            languageList.add(languageCode.name());
        }
        set("languages", serializeList(languageList));
    }

    private String serializeList(List<String> list) {
        if (list.isEmpty()) {
            return "";
        } else {
            StringBuilder serList = new StringBuilder(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                serList.append(LIST_SEPARATOR).append(list.get(i));
            }
            return serList.toString();
        }
    }

    private List<String> deserializeList(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value, LIST_SEPARATOR);
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    public void delete() {
        model.deleteCascadeShallow();
    }


}
