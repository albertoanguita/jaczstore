package jacz.store;

import jacz.store.util.GenreCode;
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

    abstract Class<? extends Model> getCompanyAssociationModel();

    public List<Company> getProductionCompanies() {
        List<jacz.store.database.models.Company> modelCompanies = getAssociation(jacz.store.database.models.Company.class);
        return Company.buildList(dbPath, modelCompanies);
    }

    public <C extends Model> void removeProductionCompanies() {
        removeAssociations(getCompanyAssociationModel(), getAssociationIdField(), null);
    }

    public <C extends Model> void removeProductionCompany(Company company) {
        removeAssociation(getCompanyAssociationModel(), getAssociationIdField(), company, "company_id", null);
    }

    public void setProductionCompanies(List<Person> persons) {
        setAssociations(getCompanyAssociationModel(), getAssociationIdField(), "company_id", null, persons);
    }

    public void setProductionCompanies(Person... persons) {
        setAssociations(getCompanyAssociationModel(), getAssociationIdField(), "company_id", null, persons);
    }

    public <C extends Model> void addProductionCompany(Company company) {
        addAssociation(getCompanyAssociationModel(), getAssociationIdField(), "company_id", null, company);
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
