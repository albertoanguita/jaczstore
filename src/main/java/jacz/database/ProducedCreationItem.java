package jacz.database;

import jacz.database.util.GenreCode;
import jacz.database.util.ImageHash;
import jacz.database.util.ItemIntegrator;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 16/11/2015.
 */
public abstract class ProducedCreationItem extends CreationItem {

//    private List<Company> ProductionCompanies;

//    private List<GenreCode> genres;

//    private ImageFile imageFile;

    private static final String GENRES_NAME_METHOD = "name";

    private static final float GENRES_SIMILARITY_CONFIDENCE = 0.1f;

    private static final float COMPANIES_SIMILARITY_CONFIDENCE = 0.1f;

    public ProducedCreationItem(String dbPath) {
        super(dbPath);
    }

    public ProducedCreationItem(String dbPath, Integer id) {
        super(dbPath, id);
    }

    public ProducedCreationItem(Model model, String dbPath) {
        super(model, dbPath);
    }

    public List<Company> getProductionCompanies() {
        LazyList<jacz.database.models.Company> models = getReferencedElements(DatabaseMediator.ItemType.COMPANY, DatabaseMediator.Field.COMPANY_LIST);
        if (models != null) {
            return Company.buildList(dbPath, models);
        } else {
            return new ArrayList<>();
        }
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
        return removeEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, GENRES_NAME_METHOD, true);
    }

    public boolean removeGenrePostponed(GenreCode genre) {
        return removeEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, GENRES_NAME_METHOD, false);
    }

    public void setGenres(List<GenreCode> genres) {
        setEnums(DatabaseMediator.Field.GENRES, GenreCode.class, genres, GENRES_NAME_METHOD, true);
    }

    public void setGenresPostponed(List<GenreCode> genres) {
        setEnums(DatabaseMediator.Field.GENRES, GenreCode.class, genres, GENRES_NAME_METHOD, false);
    }

    public boolean addGenre(GenreCode genre) {
        return addEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, GENRES_NAME_METHOD, true);
    }

    public boolean addGenrePostponed(GenreCode genre) {
        return addEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, GENRES_NAME_METHOD, false);
    }

    public ImageHash getImageHash() {
        String imageHashStr = getString(DatabaseMediator.Field.IMAGE_HASH);
        return imageHashStr != null ? ImageHash.deserialize(imageHashStr) : null;
    }

    public void setImageHash(ImageHash imageHash) {
        String imageHashStr = imageHash != null ? imageHash.serialize() : null;
        set(DatabaseMediator.Field.IMAGE_HASH, imageHashStr, true);
    }

    public void setImageHashPostponed(ImageHash imageHash) {
        String imageHashStr = imageHash != null ? imageHash.serialize() : null;
        set(DatabaseMediator.Field.IMAGE_HASH, imageHashStr, false);
    }

    @Override
    public float match(DatabaseItem anotherItem) {
        float similarity = super.match(anotherItem);
        ProducedCreationItem anotherProducedItem = (ProducedCreationItem) anotherItem;
        similarity = ItemIntegrator.addListSimilarity(similarity, getGenres(), anotherProducedItem.getGenres(), GENRES_SIMILARITY_CONFIDENCE);
        similarity = ItemIntegrator.addListSimilarity(similarity, getProductionCompaniesIds(), anotherProducedItem.getProductionCompaniesIds(), COMPANIES_SIMILARITY_CONFIDENCE);
        return similarity;
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        super.mergePostponed(anotherItem);
        ProducedCreationItem anotherProducedItem = (ProducedCreationItem) anotherItem;
        for (GenreCode genreCode : anotherProducedItem.getGenres()) {
            addGenrePostponed(genreCode);
        }
        for (Company company : anotherProducedItem.getProductionCompanies()) {
            addProductionCompany(company);
        }
    }
}
