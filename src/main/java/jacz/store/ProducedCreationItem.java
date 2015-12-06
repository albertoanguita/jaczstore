package jacz.store;

import jacz.store.database.DatabaseMediator;
import jacz.store.util.GenreCode;
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
        LazyList<jacz.store.database.models.Company> models = getReferencedElements(DatabaseMediator.ItemType.COMPANY, DatabaseMediator.Field.COMPANY_LIST);
        return Company.buildList(dbPath, models);
    }

    public <C extends Model> void removeProductionCompanies() {
        removeReferencedElements(DatabaseMediator.Field.COMPANY_LIST);
    }

    public <C extends Model> void removeProductionCompany(Company company) {
        removeReferencedElement(DatabaseMediator.Field.COMPANY_LIST, company);
    }

    public void setProductionCompanies(List<Company> companies) {
        setReferencedElements(DatabaseMediator.Field.COMPANY_LIST, companies);
    }

    public void setProductionCompanies(Company... companies) {
        setReferencedElements(DatabaseMediator.Field.COMPANY_LIST, companies);
    }

    public <C extends Model> void addProductionCompany(Company company) {
        addReferencedElement(DatabaseMediator.Field.COMPANY_LIST, company);
    }

    protected List<GenreCode> getGenres() {
        return getEnums(DatabaseMediator.Field.GENRES, GenreCode.class);
    }

    protected void removeGenres() {
        removeList(DatabaseMediator.Field.GENRES);
    }

    protected boolean removeGenre(GenreCode genre) {
        return removeEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, "name");
    }

    protected void setGenres(List<GenreCode> genres) {
        setEnums(DatabaseMediator.Field.GENRES, GenreCode.class, genres, "name");
    }

    protected boolean addGenre(GenreCode genre) {
        return addEnum(DatabaseMediator.Field.GENRES, GenreCode.class, genre, "name");
    }

    public String getImageHash() {
        return getString(DatabaseMediator.Field.IMAGE_HASH);
    }

    public void setImageHash(String imageHash) {
        set(DatabaseMediator.Field.IMAGE_HASH, imageHash);
    }
}
