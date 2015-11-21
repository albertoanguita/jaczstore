package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import jacz.store.util.GenreCode;
import org.javalite.activejdbc.Model;

import java.util.*;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class TVSeries extends ProducedCreationItem {

//    private List<Chapter> chapters;

    public TVSeries() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.TVSeries();
    }

    @Override
    Class<? extends Model> getPeopleAssociationModel() {
        return jacz.store.database.models.TVSeriesPeople.class;
    }

    @Override
    Class<? extends Model> getCompanyAssociationModel() {
        return jacz.store.database.models.TVSeriesCompanies.class;
    }

    @Override
    String getAssociationIdField() {
        return "tv_series_id";
    }

    public List<Person> getCreators() {
        return super.getCreatorsDirectors();
    }

    public <C extends Model> void removeCreators() {
        super.removeCreatorsDirectors();
    }

    public void setCreators(List<Person> persons) {
        super.setCreatorsDirectors(persons);
    }

    public void setCreators(Person... persons) {
        super.setCreatorsDirectors(persons);
    }

//    public <C extends Model> void addCreators(List<Person> persons) {
//        super.addCreatorDirectors(persons);
//    }
//
//    public <C extends Model> void addCreators(Person... persons) {
//        super.addCreatorDirectors(persons);
//    }

    public <C extends Model> void addCreators(Person person) {
        super.addCreatorDirector(person);
    }

    public List<String> getSeasons() {
        List<Chapter> chapters = getChapters();
        return getSeasons(chapters);
    }

    private static List<String> getSeasons(List<Chapter> chapters) {
        Set<String> seasonSet = new HashSet<>();
        for (Chapter chapter : chapters) {
            seasonSet.add(chapter.getSeason());
        }
        List<String> seasons = new ArrayList<>(seasonSet);
        Collections.sort(seasons);
        return seasons;
    }

    public List<Chapter> getChapters() {
        return Chapter.buildList(getDirectAssociationChildren(jacz.store.database.models.Chapter.class));
    }

    public List<Chapter> getChapters(String season) {
        return Chapter.buildList(getDirectAssociationChildren(jacz.store.database.models.Chapter.class, "season = ?", season));
    }

    public void deleteChapters() {
        deleteDirectAssociationChildren(jacz.store.database.models.Chapter.class);
    }

    public void setChapters(List<Chapter> chapters) {
        setDirectAssociationChildren(jacz.store.database.models.Chapter.class, chapters);
    }

    public void setChapters(Chapter... chapters) {
        setDirectAssociationChildren(jacz.store.database.models.Chapter.class, chapters);
    }

    public void addChapter(Chapter chapter) {
        addDirectAssociationChild(chapter);
    }
}
