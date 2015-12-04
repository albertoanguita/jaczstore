package jacz.store;

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
        LazyList<jacz.store.database.models.Company> models = getReferencedElements(jacz.store.database.models.Company.class, "company_list");
        return Company.buildList(dbPath, models);
    }

    public <C extends Model> void removeProductionCompanies() {
        removeReferencedElements("company_list");
    }

    public <C extends Model> void removeProductionCompany(Company company) {
        removeReferencedElement("company_list", company);
    }

    public void setProductionCompanies(List<Company> companies) {
        setReferencedElements("company_list", companies);
    }

    public void setProductionCompanies(Company... companies) {
        setReferencedElements("company_list", companies);
    }

    public <C extends Model> void addProductionCompany(Company company) {
        addReferencedElement("company_list", company);
    }

    protected List<GenreCode> getGenres() {
        return getEnums("genres", GenreCode.class);
    }

    protected void removeGenres() {
        removeList("genres");
    }

    protected boolean removeGenre(GenreCode genre) {
        return removeEnum("genres", GenreCode.class, genre, "name");
    }

    protected void setGenres(List<GenreCode> genres) {
        setEnums("genres", GenreCode.class, genres, "name");
    }

    protected boolean addGenre(GenreCode genre) {
        return addEnum("genres", GenreCode.class, genre, "name");
    }

    public String getImageHash() {
        return getString("image_hash");
    }

    public void setImageHash(String imageHash) {
        set("image_hash", imageHash);
    }
}
