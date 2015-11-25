package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;
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
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
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

    protected <C extends Model> Model getDirectAssociationParent(Class<C> parentClass) {
        return model.parent(parentClass);
    }

    protected void setDirectAssociationParent(LibraryItem item) {
        item.model.add(model);
    }

    protected <C extends Model> void removeDirectAssociationParent(Class<C> parentClass) {
        deleteModel(model.parent(parentClass));
    }

    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass) {
        return model.getAll(childClass);
    }

    protected <C extends Model> LazyList<C> getDirectAssociationChildren(Class<C> childClass, String query, Object... params) {
        return model.get(childClass, query, params);
    }

    protected <C extends Model> void removeDirectAssociationChildren(Class<C> childClass) {
        List<C> children = getDirectAssociationChildren(childClass);
        for (C child : children) {
            deleteModel(child);
//            child.delete();
        }
    }

    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, List<? extends LibraryItem> items) {
        removeDirectAssociationChildren(childrenClass);
        for (LibraryItem item : items) {
            addDirectAssociationChild(item);
        }
    }

    protected <C extends Model> void setDirectAssociationChildren(Class<C> childrenClass, LibraryItem... items) {
        removeDirectAssociationChildren(childrenClass);
        for (LibraryItem item : items) {
            addDirectAssociationChild(item);
        }
    }

    protected void addDirectAssociationChild(LibraryItem item) {
        model.add(item.model);
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
            if (type != null) {
                associations = (List<C>) where.invoke(null, idField + " = '" + getId() + "' AND type = '" + type + "'", new Object[0]);
            } else {
                associations = (List<C>) where.invoke(null, idField + " = '" + getId() + "'", new Object[0]);
            }
            for (C association : associations) {
//                association.delete();
                deleteModel(association);
            }
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        }
    }

    protected <C extends Model> void removeAssociation(Class<? extends Model> modelClass, String idField, LibraryItem otherItem, String otherIdField, String type) {
        try {
            Method findFirst = modelClass.getMethod("findFirst", String.class, Object[].class);
            C  association;
            if (type != null) {
                association = (C) findFirst.invoke(null, idField + " = '" + getId() + "' AND " + otherIdField + " = '" + otherItem.getId() + "' AND type = '" + type + "'", new Object[0]);
            } else {
                association = (C) findFirst.invoke(null, idField + " = '" + getId() + "' AND " + otherIdField + " = '" + otherItem.getId() + "'", new Object[0]);
            }
//            association.delete();
            deleteModel(association);
        } catch (Exception e) {
            // todo internal error
            e.printStackTrace();
        }
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String childIdField, String type, List<? extends LibraryItem> items) {
        removeAssociations(modelClass, idField, type);
        for (LibraryItem item : items) {
            addAssociation(modelClass, idField, childIdField, type, item);
        }
    }

    protected <C extends Model> void setAssociations(Class<? extends Model> modelClass, String idField, String childIdField, String type, LibraryItem... items) {
        removeAssociations(modelClass, idField, type);
        for (LibraryItem item : items) {
            addAssociation(modelClass, idField, childIdField, type, item);
        }
    }

    protected <C extends Model> void addAssociation(Class<? extends Model> modelClass, String idField, String childIdField, String type, LibraryItem item) {
        try {
            Model associativeModel = modelClass.newInstance();
            associativeModel.set(idField, getId());
            associativeModel.set(childIdField, item.getId());
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

    protected void removeCountries() {
        set("countries", null);
    }

    protected boolean removeCountry(CountryCode country) {
        List<CountryCode> countries = getCountries();
        boolean removed = countries.remove(country);
        setCountries(countries);
        return removed;
    }

    protected void setCountries(List<CountryCode> countries) {
        List<String> countryList = new ArrayList<>();
        for (CountryCode countryCode : countries) {
            countryList.add(countryCode.getAlpha2());
        }
        set("countries", serializeList(countryList));
    }

    protected boolean addCountry(CountryCode country) {
        List<CountryCode> countries = getCountries();
        boolean notContains = !countries.contains(country);
        if (notContains) {
            countries.add(country);
        }
        setCountries(countries);
        return notContains;
    }

    protected List<String> getExternalURLs() {
        return deserializeList(getString("externalURLs"));
    }

    protected void removeExternalURLs() {
        set("externalURLs", null);
    }

    protected boolean removeExternalURL(String externalURL) {
        List<String> externalURLs = getExternalURLs();
        boolean removed = externalURLs.remove(externalURL);
        setExternalURLs(externalURLs);
        return removed;
    }

    protected void setExternalURLs(List<String> externalURLs) {
        set("externalURLs", serializeList(externalURLs));
    }

    protected boolean addExternalURL(String externalURL) {
        List<String> externalURLs = getExternalURLs();
        boolean notContains = !externalURLs.contains(externalURL);
        if (notContains) {
            externalURLs.add(externalURL);
        }
        setExternalURLs(externalURLs);
        return notContains;
    }

    protected List<GenreCode> getGenres() {
        String genreList = getString("genres");
        List<GenreCode> genres = new ArrayList<>();
        for (String genreCode : deserializeList(genreList)) {
            genres.add(GenreCode.valueOf(genreCode));
        }
        return genres;
    }

    protected void removeGenres() {
        set("genres", null);
    }

    protected boolean removeGenre(GenreCode genre) {
        List<GenreCode> genres = getGenres();
        boolean removed = genres.remove(genre);
        setGenres(genres);
        return removed;
    }

    protected void setGenres(List<GenreCode> genres) {
        List<String> genreList = new ArrayList<>();
        for (GenreCode genreCode : genres) {
            genreList.add(genreCode.name());
        }
        set("genres", serializeList(genreList));
    }

    protected boolean addGenre(GenreCode genre) {
        List<GenreCode> genres = getGenres();
        boolean notContains = !genres.contains(genre);
        if (notContains) {
            genres.add(genre);
        }
        setGenres(genres);
        return notContains;
    }

    protected List<LanguageCode> getLanguages() {
        String languageList = getString("languages");
        List<LanguageCode> languages = new ArrayList<>();
        for (String languageCode : deserializeList(languageList)) {
            languages.add(LanguageCode.valueOf(languageCode));
        }
        return languages;
    }

    protected void removeLanguages() {
        set("languages", null);
    }

    protected boolean removeLanguages(LanguageCode language) {
        List<LanguageCode> languages = getLanguages();
        boolean removed = languages.remove(language);
        setLanguages(languages);
        return removed;
    }

    protected void setLanguages(List<LanguageCode> languages) {
        List<String> languageList = new ArrayList<>();
        for (LanguageCode languageCode : languages) {
            languageList.add(languageCode.name());
        }
        set("languages", serializeList(languageList));
    }

    protected boolean addLanguage(LanguageCode language) {
        List<LanguageCode> languages = getLanguages();
        boolean notContains = !languages.contains(language);
        if (notContains) {
            languages.add(language);
        }
        setLanguages(languages);
        return notContains;
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
        value = value == null ? "" : value;
        StringTokenizer tokenizer = new StringTokenizer(value, LIST_SEPARATOR);
        List<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    public void delete() {
        deleteModel(model);
//        model.deleteCascadeShallow();
    }

    private void deleteModel(Model model) {
        model.set("alive", false);
    }
}
