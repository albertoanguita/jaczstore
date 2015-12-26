package jacz.store.database_old;

import jacz.database.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alberto on 17/11/2015.
 */
public class JavaLiteSQLt3Mediator implements DatabaseMediator {

    @Override
    public List<Movie> getMovies() throws SQLException {
        return null;
    }

    @Override
    public List<TVSeries> getTVSeries() throws SQLException {
        return null;
    }

    @Override
    public List<Person> getPersons() throws SQLException {
        return null;
    }

    @Override
    public List<Company> getCompanies() throws SQLException {
        return null;
    }

    @Override
    public void saveMovie(Movie movie) {

    }

    @Override
    public void saveTVSeries(TVSeries tvSeries) {

    }

    @Override
    public void saveChapter(Chapter chapter) {

    }

    @Override
    public void savePerson(Person person) {

    }

    @Override
    public void saveCompany(Company company) {

    }

    @Override
    public void saveVideoFile(VideoFile videoFile) {

    }

    @Override
    public void saveSubtitleFile(SubtitleFile subtitleFile) {

    }
}
