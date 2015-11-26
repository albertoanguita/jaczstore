package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database.DatabaseMediator;
import jacz.store.util.GenreCode;
import junitx.framework.ListAssert;
import org.javalite.activejdbc.Base;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 25/11/2015.
 */
public class Test {

    @org.junit.Test
    public void testMovies() throws SQLException, ClassNotFoundException {
        DatabaseMediator.dropAndCreate();
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:store.db", "", "");


        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        Movie movie3 = new Movie();
        movie1.setTitle("Predator");
        movie2.setTitle("Alien");
        movie3.setTitle("Rambo");

        Assert.assertEquals(3, Movie.getMovies().size());
        Assert.assertEquals("Predator", Movie.getMovies().get(0).getTitle());
        Assert.assertEquals("Alien", Movie.getMovies().get(1).getTitle());
        Assert.assertEquals("Rambo", Movie.getMovies().get(2).getTitle());

        movie2.delete();
        Assert.assertEquals(2, Movie.getMovies().size());
        Assert.assertEquals("Predator", Movie.getMovies().get(0).getTitle());
        Assert.assertEquals("Rambo", Movie.getMovies().get(1).getTitle());

        movie1.setTitle("Titanic");
        movie1.setOriginalTitle("Titanic original");
        movie1.setYear(1999);
        movie1.addCountry(CountryCode.ES);
        movie1.addCountry(CountryCode.US);
        movie1.addExternalURL("url1");
        movie1.addExternalURL("url2");
        movie1.addGenre(GenreCode.DOCUMENTARY);
        movie1.addGenre(GenreCode.FAMILY);
        ImageFile imageFile = new ImageFile();
        imageFile.setHash("image");
        movie1.setImage(imageFile);

        List<CountryCode> countries = new ArrayList<>();
        countries.add(CountryCode.ES);
        countries.add(CountryCode.US);
        List<String> externalURLs = new ArrayList<>();
        externalURLs.add("url1");
        externalURLs.add("url2");
        List<GenreCode> genres = new ArrayList<>();
        genres.add(GenreCode.DOCUMENTARY);
        genres.add(GenreCode.FAMILY);
        Assert.assertEquals("Titanic", movie1.getTitle());
        Assert.assertEquals("Titanic original", movie1.getOriginalTitle());
        Assert.assertEquals(new Integer(1999), movie1.getYear());
        ListAssert.assertEquals(countries, movie1.getCountries());
        ListAssert.assertEquals(externalURLs, movie1.getExternalURLs());
        ListAssert.assertEquals(genres, movie1.getGenres());
        Assert.assertEquals("image", movie1.getImage().getHash());

        imageFile.delete();
        movie1 = Movie.getMovieById(1);
        Assert.assertEquals(null, movie1.getImage());

        Person arnold = new Person();
        arnold.setName("Arnold");
        Person silvester = new Person();
        silvester.setName("Silvester");
        Person christian = new Person();
        christian.setName("Christian");
        Person george = new Person();
        george.setName("George");

        Movie movie = new Movie();
        movie.setTitle("Predator");

        movie.addDirector(arnold);
        movie.addDirector(silvester);
        movie.addActor(christian);
        movie.addActor(george);
        List<Person> actors = movie.getActors();
        List<Person> directors = movie.getDirectors();
        Assert.assertEquals(2, actors.size());
        Assert.assertEquals(2, directors.size());
        Assert.assertEquals("Christian", actors.get(0).getName());
        Assert.assertEquals("George", actors.get(1).getName());
        Assert.assertEquals("Arnold", directors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(1).getName());
        movie.removeDirector(arnold);
        movie.removeActor(christian);
        actors = movie.getActors();
        directors = movie.getDirectors();
        Assert.assertEquals(1, actors.size());
        Assert.assertEquals(1, directors.size());
        Assert.assertEquals("George", actors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(0).getName());
        movie.removeDirectors();
        movie.removeActors();
        Assert.assertEquals(0, movie.getDirectors().size());
        Assert.assertEquals(0, movie.getActors().size());

        Company hbo = new Company();
        hbo.setName("HBO");
        Company pixar = new Company();
        pixar.setName("Pixar");
        movie.addProductionCompany(hbo);
        movie.addProductionCompany(pixar);
        List<Company> companies = movie.getProductionCompanies();
        Assert.assertEquals(2, companies.size());
        Assert.assertEquals("HBO", companies.get(0).getName());
        Assert.assertEquals("Pixar", companies.get(1).getName());
        movie.removeProductionCompany(hbo);
        companies = movie.getProductionCompanies();
        Assert.assertEquals(1, companies.size());
        Assert.assertEquals("Pixar", companies.get(0).getName());
        movie.removeProductionCompanies();
        Assert.assertEquals(0, movie.getProductionCompanies().size());

        VideoFile videoFile1 = new VideoFile();
        videoFile1.setName("video1");
        VideoFile videoFile2 = new VideoFile();
        videoFile2.setName("video2");
        movie.addVideoFile(videoFile1);
        movie.addVideoFile(videoFile2);
        List<VideoFile> videoFiles = movie.getVideoFiles();
        Assert.assertEquals(2, videoFiles.size());
        Assert.assertEquals("video1", videoFiles.get(0).getName());
        Assert.assertEquals("video2", videoFiles.get(1).getName());
        movie.removeVideoFile(videoFile1);
        videoFiles = movie.getVideoFiles();
        Assert.assertEquals(1, videoFiles.size());
        Assert.assertEquals("video2", videoFiles.get(0).getName());
        movie.removeVideoFiles();
        Assert.assertEquals(0, movie.getVideoFiles().size());


//        movie1.addLanguage(LanguageCode.es);
//        movie1.addLanguage(LanguageCode.en);
//        List<LanguageCode> languages = new ArrayList<>();
//        languages.add(LanguageCode.es);
//        languages.add(LanguageCode.en);
//        ListAssert.assertEquals(languages, movie1.getLanguages());
    }

    @org.junit.Test
    public void testTVSeries() throws SQLException, ClassNotFoundException {
    }
}
