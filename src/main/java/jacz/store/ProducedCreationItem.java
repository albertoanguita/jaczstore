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

    public ProducedCreationItem() {
        super();
    }

    abstract Class<? extends Model> getCompanyAssociationModel();

    public List<Company> getProductionCompanies() {
        List<jacz.store.database.models.Company> modelCompanies = getAssociation(jacz.store.database.models.Company.class);
        return Company.buildList(modelCompanies);
    }

    public <C extends Model> void removeProductionCompanies() {
        removeAssociations(getCompanyAssociationModel(), getAssociationIdField(), null);
    }

    public <C extends Model> void removeProductionCompany(Company company) {
        removeAssociation(getCompanyAssociationModel(), getAssociationIdField(), company, "company_id", null);
    }

    public void setProductionCompanies(List<Person> persons) {
        setAssociations(getCompanyAssociationModel(), getAssociationIdField(), null, persons);
    }

    public void setProductionCompanies(Person... persons) {
        setAssociations(getCompanyAssociationModel(), getAssociationIdField(), null, persons);
    }

    public <C extends Model> void addProductionCompany(Person person) {
        addAssociation(getCompanyAssociationModel(), getAssociationIdField(), null, person);
    }

    @Override
    public List<GenreCode> getGenres() {
        return super.getGenres();
    }

    @Override
    public void setGenres(List<GenreCode> genres) {
        super.setGenres(genres);
    }

    public ImageFile getImage() {
        return new ImageFile(getDirectAssociationParent(jacz.store.database.models.ImageFile.class));
    }

    public void setImage(ImageFile image) {
        setDirectAssociationChildren(jacz.store.database.models.ImageFile.class, image);
    }
}
