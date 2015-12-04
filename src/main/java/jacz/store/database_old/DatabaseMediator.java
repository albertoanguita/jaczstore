package jacz.store.database_old;

import jacz.store.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public interface DatabaseMediator {

    List<Movie> getMovies() throws SQLException;

    List<TVSeries> getTVSeries() throws SQLException;

    List<Person> getPersons() throws SQLException;

    List<Company> getCompanies() throws SQLException;

    void saveMovie(Movie movie);

    void saveTVSeries(TVSeries tvSeries);

    void saveChapter(Chapter chapter);

    void savePerson(Person person);

    void saveCompany(Company company);

    void saveVideoFile(VideoFile videoFile);

    void saveSubtitleFile(SubtitleFile subtitleFile);
}
