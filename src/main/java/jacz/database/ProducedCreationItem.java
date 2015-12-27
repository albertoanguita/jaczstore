package jacz.database;

import jacz.database.util.GenreCode;
import jacz.util.AI.inference.Mycin;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class ProducedCreationItem extends CreationItem {

//    private List<Company> ProductionCompanies;

//    private List<GenreCode> genres;

//    private ImageFile imageFile;

    public ProducedCreationItem(String dbPath) {
        super(dbPath);
    }

    public ProducedCreationItem(Model model, String dbPath) {
        super(model, dbPath);
    }

    public List<Company> getProductionCompanies() {
        LazyList<jacz.database.models.Company> models = getReferencedElements(DatabaseMediator.ItemType.COMPANY, DatabaseMediator.Field.COMPANY_LIST);
        return Company.buildList(dbPath, models);
    }

    public List<String> getProductionCompaniesIds() {
        return getReferencedElementsIds(DatabaseMediator.ItemType.COMPANY, DatabaseMediator.Field.COMPANY_LIST);
    }

    public <C extends Model> void removeProductionCompanies() {
        removeReferencedElements(DatabaseMediator.Field.COMPANY_LIST, true);
    }

    public <C extends Model> void removeProductionCompaniesPostponed() {
        removeReferencedElements(DatabaseMediator.Field.COMPANY_LIST, false);
    }

    public <C extends Model> void removeProductionCompany(Company company) {
        removeReferencedElement(DatabaseMediator.Field.COMPANY_LIST, company, true);
    }

    public <C extends Model> void removeProductionCompanyPostponed(Company company) {
        removeReferencedElement(DatabaseMediator.Field.COMPANY_LIST, company, false);
    }

    public void setProductionCompanies(List<Company> companies) {
        setReferencedElements(DatabaseMediator.Field.COMPANY_LIST, companies, true);
    }

    public void setProductionCompaniesPostponed(List<Company> companies) {
        setReferencedElements(DatabaseMediator.Field.COMPANY_LIST, companies, false);
    }

    public void setProductionCompaniesIds(List<String> companies) {
        setReferencedElementsIds(DatabaseMediator.Field.COMPANY_LIST, companies, true);
    }

    public void setProductionCompaniesIdsPostponed(List<String> companies) {
        setReferencedElementsIds(DatabaseMediator.Field.COMPANY_LIST, companies, false);
    }

    public void setProductionCompanies(Company... companies) {
        setReferencedElements(DatabaseMediator.Field.COMPANY_LIST, true, companies);
    }

    public void setProductionCompaniesPostponed(Company... companies) {
        setReferencedElements(DatabaseMediator.Field.COMPANY_LIST, false, companies);
    }

    public <C extends Model> void addProductionCompany(Company company) {
        addReferencedElement(DatabaseMediator.Field.COMPANY_LIST, company, true);
    }

    public <C extends Model> void addProductionCompanyPostponed(Company company) {
        addReferencedElement(DatabaseMediator.Field.COMPANY_LIST, company, false);
    }

    public List<GenreCode> getGenres() {
        return getEnums(DatabaseMediator.Field.GENRES, GenreCode.class);
    }

    public void removeGenres() {
        removeList(DatabaseMediator.Field.GENRES, true);
    }

    public void removeGenresPostponed() {
        removeList(DatabaseMediator.Field.GENRES, false);
    }

    public boolean removeGenre(GenreCode genre) {
        return removeEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, "name", true);
    }

    public boolean removeGenrePostponed(GenreCode genre) {
        return removeEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, "name", false);
    }

    public void setGenres(List<GenreCode> genres) {
        setEnums(DatabaseMediator.Field.GENRES, GenreCode.class, genres, "name", true);
    }

    public void setGenresPostponed(List<GenreCode> genres) {
        setEnums(DatabaseMediator.Field.GENRES, GenreCode.class, genres, "name", false);
    }

    public boolean addGenre(GenreCode genre) {
        return addEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, "name", true);
    }

    public boolean addGenrePostponed(GenreCode genre) {
        return addEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, "name", false);
    }

    public String getImageHash() {
        return getString(DatabaseMediator.Field.IMAGE_HASH);
    }

    public void setImageHash(String imageHash) {
        set(DatabaseMediator.Field.IMAGE_HASH, imageHash, true);
    }

    public void setImageHashPostponed(String imageHash) {
        set(DatabaseMediator.Field.IMAGE_HASH, imageHash, false);
    }

    @Override
    public float match(DatabaseItem anotherItem, ListSimilarity... listSimilarities) {
        float similarity = super.match(anotherItem, listSimilarities);
        ProducedCreationItem anotherProducedItem = (ProducedCreationItem) anotherItem;
        List<GenreCode> genres1 = getGenres();
        List<GenreCode> genres2 = anotherProducedItem.getGenres();
        int genreMatches = 0;
        for (GenreCode genreCode : genres1) {
            if (genres2.contains(genreCode)) {
                genreMatches++;
            }
        }
        similarity = Mycin.combine(similarity, evaluateListSimilarity(new ListSimilarity(genres1.size(), genres2.size(), genreMatches), 0.1f));
        for (ListSimilarity listSimilarity : listSimilarities) {
            switch (listSimilarity.referencedList) {
                case COMPANIES:
                    similarity = Mycin.combine(similarity, evaluateListSimilarity(listSimilarity, 0.1f));
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
