package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database.DatabaseMediator;
import jacz.store.util.GenreCode;
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

    @Override
    public float match(LibraryItem anotherItem, ListSimilarity... listSimilarities) {
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
    public void merge(LibraryItem anotherItem) {

    }
}
