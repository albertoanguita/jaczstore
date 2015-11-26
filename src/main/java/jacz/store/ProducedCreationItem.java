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

    public ProducedCreationItem(Model model) {
        super(model);
    }

    abstract Class<? extends Model> getCompanyAssociationModel();

    public List<Company> getProductionCompanies() {
        List<jacz.store.database.models.Company> modelCompanies = getAssociation(jacz.store.database.models.Company.class, getCompanyAssociationModel());
        return Company.buildList(modelCompanies);
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

    @Override
    public List<GenreCode> getGenres() {
        return super.getGenres();
    }

    @Override
    public void removeGenres() {
        super.removeGenres();
    }

    @Override
    public boolean removeGenre(GenreCode genre) {
        return super.removeGenre(genre);
    }

    @Override
    public void setGenres(List<GenreCode> genres) {
        super.setGenres(genres);
    }

    @Override
    public boolean addGenre(GenreCode genre) {
        return super.addGenre(genre);
    }

    public ImageFile getImage() {
        Model model = getDirectAssociationParent(jacz.store.database.models.ImageFile.class);
        return model != null ? new ImageFile(model) : null;
    }

    public void setImage(ImageFile image) {
        setDirectAssociationParent(image);
    }

    public void removeImage() {
        removeDirectAssociationParent(jacz.store.database.models.ImageFile.class);
        set("image_file_id", -1);
    }

    @Override
    public void delete() {
        super.delete();
        removeProductionCompanies();
        removeImage();
    }
}
