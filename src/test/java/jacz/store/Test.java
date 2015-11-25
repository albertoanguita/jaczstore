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
        Assert.assertEquals(null, movie1.getImage());

//        movie1.addLanguage(LanguageCode.es);
//        movie1.addLanguage(LanguageCode.en);
//        List<LanguageCode> languages = new ArrayList<>();
//        languages.add(LanguageCode.es);
//        languages.add(LanguageCode.en);
//        ListAssert.assertEquals(languages, movie1.getLanguages());
    }
}
