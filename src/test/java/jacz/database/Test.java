package jacz.database;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;
import jacz.database.util.GenreCode;
import jacz.database.util.ImageHash;
import jacz.database.util.QualityCode;
import junitx.framework.ListAssert;
import org.javalite.activejdbc.Base;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Database items management tests
 */
public class Test {

    @org.junit.Test
    public void testMovies() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        DatabaseMediator.connect("store.db");
        try {
            System.out.println(Base.connection().getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseMediator.disconnect("store.db");


        Movie movie1 = new Movie("store.db", 517);
        Movie movie2 = new Movie("store.db");
        Movie movie3 = new Movie("store.db");
        movie1.setTitle("Predator");
        movie2.setTitle("Alien");
        movie3.setTitle("Rambo");

        Assert.assertEquals(3, Movie.getMovies("store.db").size());
        Assert.assertEquals("Predator", Movie.getMovies("store.db").get(0).getTitle());
        Assert.assertEquals("Alien", Movie.getMovies("store.db").get(1).getTitle());
        Assert.assertEquals("Rambo", Movie.getMovies("store.db").get(2).getTitle());

        movie2.delete();
        Assert.assertEquals(2, Movie.getMovies("store.db").size());
        Assert.assertEquals("Predator", Movie.getMovies("store.db").get(0).getTitle());
        Assert.assertEquals("Rambo", Movie.getMovies("store.db").get(1).getTitle());

        movie1.setTitle("Titanic");
        movie1.setOriginalTitle("Titanic original");
        movie1.setYear(1999);
        movie1.setSynopsis("A synopsis...");
        movie1.addCountry(CountryCode.ES);
        movie1.addCountry(CountryCode.US);
        movie1.addExternalURL("url1");
        movie1.addExternalURL("url2");
        movie1.addGenre(GenreCode.DOCUMENTARY);
        movie1.addGenre(GenreCode.FAMILY);
        movie1.setImageHash(new ImageHash("image", "jpg"));
        movie1.setMinutes(150);

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
        Assert.assertEquals("A synopsis...", movie1.getSynopsis());
        ListAssert.assertEquals(countries, movie1.getCountries());
        ListAssert.assertEquals(externalURLs, movie1.getExternalURLs());
        ListAssert.assertEquals(genres, movie1.getGenres());
        Assert.assertEquals(new ImageHash("image", "jpg"), movie1.getImageHash());
        Assert.assertEquals(new Integer(150), movie1.getMinutes());

        Person arnold = new Person("store.db");
        arnold.setName("Arnold");
        Person silvester = new Person("store.db");
        silvester.setName("Silvester");
        Person christian = new Person("store.db");
        christian.setName("Christian");
        Person george = new Person("store.db");
        george.setName("George");

        Movie movie = new Movie("store.db");
        movie.setTitle("Predator");

        movie.addCreator(arnold);
        movie.addCreator(silvester);
        movie.addActor(christian);
        movie.addActor(george);
        List<Person> actors = movie.getActors();
        List<Person> directors = movie.getCreators();
        Assert.assertEquals(2, actors.size());
        Assert.assertEquals(2, directors.size());
        Assert.assertEquals("Christian", actors.get(0).getName());
        Assert.assertEquals("George", actors.get(1).getName());
        Assert.assertEquals("Arnold", directors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(1).getName());
        movie.removeCreator(arnold);
        movie.removeActor(christian);
        actors = movie.getActors();
        directors = movie.getCreators();
        Assert.assertEquals(1, actors.size());
        Assert.assertEquals(1, directors.size());
        Assert.assertEquals("George", actors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(0).getName());
        movie.removeCreators();
        movie.removeActors();
        Assert.assertEquals(0, movie.getCreators().size());
        Assert.assertEquals(0, movie.getActors().size());

        Company hbo = new Company("store.db");
        hbo.setName("HBO");
        Company pixar = new Company("store.db");
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

        VideoFile videoFile1 = new VideoFile("store.db");
        videoFile1.setName("video1");
        VideoFile videoFile2 = new VideoFile("store.db");
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

        boolean added;
        added = movie.addTag("favorite");
        Assert.assertEquals(true, added);
        added = movie.addTag("star");
        Assert.assertEquals(true, added);
        added = movie.addTag("favorite");
        Assert.assertEquals(false, added);

        List<Movie> favoriteMovies = Tag.getMoviesWithTag("store.db", "favorite");
        Assert.assertEquals(1, favoriteMovies.size());
        Assert.assertEquals("Predator", favoriteMovies.get(0).getTitle());
        Assert.assertEquals(2, movie.getTags().size());
        Assert.assertEquals("favorite", movie.getTags().get(0));
        Assert.assertEquals("star", movie.getTags().get(1));
        Assert.assertEquals(true, movie.removeTag("favorite"));
        Assert.assertEquals(false, movie.removeTag("favorite"));
        Assert.assertEquals(1, movie.getTags().size());
        Assert.assertEquals("star", movie.getTags().get(0));

//            movie.removeVideoFiles();
//            Assert.assertEquals(0, movie.getVideoFiles().size());
    }

    @org.junit.Test
    public void testTVSeries() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        TVSeries tvSeries1 = new TVSeries("store.db");
        TVSeries tvSeries2 = new TVSeries("store.db");
        TVSeries tvSeries3 = new TVSeries("store.db");
        tvSeries1.setTitle("Predator");
        tvSeries2.setTitle("Alien");
        tvSeries3.setTitle("Rambo");

        Assert.assertEquals(3, TVSeries.getTVSeries("store.db").size());
        Assert.assertEquals("Predator", TVSeries.getTVSeries("store.db").get(0).getTitle());
        Assert.assertEquals("Alien", TVSeries.getTVSeries("store.db").get(1).getTitle());
        Assert.assertEquals("Rambo", TVSeries.getTVSeries("store.db").get(2).getTitle());

        tvSeries2.delete();
        Assert.assertEquals(2, TVSeries.getTVSeries("store.db").size());
        Assert.assertEquals("Predator", TVSeries.getTVSeries("store.db").get(0).getTitle());
        Assert.assertEquals("Rambo", TVSeries.getTVSeries("store.db").get(1).getTitle());

        tvSeries1.setTitle("Titanic");
        tvSeries1.setOriginalTitle("Titanic original");
        tvSeries1.setYear(1999);
        tvSeries1.addCountry(CountryCode.ES);
        tvSeries1.addCountry(CountryCode.US);
        tvSeries1.addExternalURL("url1");
        tvSeries1.addExternalURL("url2");
        tvSeries1.addGenre(GenreCode.DOCUMENTARY);
        tvSeries1.addGenre(GenreCode.FAMILY);
        tvSeries1.setImageHash(new ImageHash("image", "jpg"));

        List<CountryCode> countries = new ArrayList<>();
        countries.add(CountryCode.ES);
        countries.add(CountryCode.US);
        List<String> externalURLs = new ArrayList<>();
        externalURLs.add("url1");
        externalURLs.add("url2");
        List<GenreCode> genres = new ArrayList<>();
        genres.add(GenreCode.DOCUMENTARY);
        genres.add(GenreCode.FAMILY);
        Assert.assertEquals("Titanic", tvSeries1.getTitle());
        Assert.assertEquals("Titanic original", tvSeries1.getOriginalTitle());
        Assert.assertEquals(new Integer(1999), tvSeries1.getYear());
        ListAssert.assertEquals(countries, tvSeries1.getCountries());
        ListAssert.assertEquals(externalURLs, tvSeries1.getExternalURLs());
        ListAssert.assertEquals(genres, tvSeries1.getGenres());
        Assert.assertEquals(new ImageHash("image", "jpg"), tvSeries1.getImageHash());

        tvSeries1 = TVSeries.getTVSeriesById("store.db", 1);

        Person arnold = new Person("store.db");
        arnold.setName("Arnold");
        Person silvester = new Person("store.db");
        silvester.setName("Silvester");
        Person christian = new Person("store.db");
        christian.setName("Christian");
        Person george = new Person("store.db");
        george.setName("George");

        TVSeries tvSeries = new TVSeries("store.db");
        tvSeries1.setTitle("Predator");

        tvSeries.addCreator(arnold);
        tvSeries.addCreator(silvester);
        tvSeries.addActor(christian);
        tvSeries.addActor(george);
        List<Person> actors = tvSeries.getActors();
        List<Person> directors = tvSeries.getCreators();
        Assert.assertEquals(2, actors.size());
        Assert.assertEquals(2, directors.size());
        Assert.assertEquals("Christian", actors.get(0).getName());
        Assert.assertEquals("George", actors.get(1).getName());
        Assert.assertEquals("Arnold", directors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(1).getName());
        tvSeries.removeCreator(arnold);
        tvSeries.removeActor(christian);
        actors = tvSeries.getActors();
        directors = tvSeries.getCreators();
        Assert.assertEquals(1, actors.size());
        Assert.assertEquals(1, directors.size());
        Assert.assertEquals("George", actors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(0).getName());
        tvSeries.removeCreators();
        tvSeries.removeActors();
        Assert.assertEquals(0, tvSeries.getCreators().size());
        Assert.assertEquals(0, tvSeries.getActors().size());

        Company hbo = new Company("store.db");
        hbo.setName("HBO");
        Company pixar = new Company("store.db");
        pixar.setName("Pixar");
        tvSeries.addProductionCompany(hbo);
        tvSeries.addProductionCompany(pixar);
        List<Company> companies = tvSeries.getProductionCompanies();
        Assert.assertEquals(2, companies.size());
        Assert.assertEquals("HBO", companies.get(0).getName());
        Assert.assertEquals("Pixar", companies.get(1).getName());
        tvSeries.removeProductionCompany(hbo);
        companies = tvSeries.getProductionCompanies();
        Assert.assertEquals(1, companies.size());
        Assert.assertEquals("Pixar", companies.get(0).getName());
        tvSeries.removeProductionCompanies();
        Assert.assertEquals(0, tvSeries.getProductionCompanies().size());

        Chapter chapter1 = new Chapter("store.db");
        chapter1.setTitle("ch1");
        chapter1.setSeason("s01");
        Chapter chapter2 = new Chapter("store.db");
        chapter2.setTitle("ch2");
        chapter2.setSeason("s01");
        tvSeries.setChapters(chapter1, chapter2);
        List<String> seasons = new ArrayList<>();
        seasons.add("s01");
        Assert.assertEquals(2, tvSeries.getChapters().size());
        Assert.assertEquals("ch1", tvSeries.getChapters().get(0).getTitle());
        Assert.assertEquals("ch2", tvSeries.getChapters().get(1).getTitle());
        Assert.assertEquals(seasons, tvSeries.getSeasons());
        chapter1.delete();
        Assert.assertEquals(1, tvSeries.getChapters().size());
        Assert.assertEquals("ch2", tvSeries.getChapters().get(0).getTitle());
        tvSeries.removeChapters();
        Assert.assertEquals(0, tvSeries.getChapters().size());
    }

    @org.junit.Test
    public void testChapters() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        Chapter chapter1 = new Chapter("store.db");
        Chapter chapter2 = new Chapter("store.db");
        Chapter chapter3 = new Chapter("store.db");
        chapter1.setTitle("Predator");
        chapter2.setTitle("Alien");
        chapter3.setTitle("Rambo");

        Assert.assertEquals(3, Chapter.getChapters("store.db").size());
        Assert.assertEquals("Predator", Chapter.getChapters("store.db").get(0).getTitle());
        Assert.assertEquals("Alien", Chapter.getChapters("store.db").get(1).getTitle());
        Assert.assertEquals("Rambo", Chapter.getChapters("store.db").get(2).getTitle());

        chapter2.delete();
        Assert.assertEquals(2, Chapter.getChapters("store.db").size());
        Assert.assertEquals("Predator", Chapter.getChapters("store.db").get(0).getTitle());
        Assert.assertEquals("Rambo", Chapter.getChapters("store.db").get(1).getTitle());

        chapter1.setTitle("Titanic");
        chapter1.setOriginalTitle("Titanic original");
        chapter1.setYear(1999);
        chapter1.addCountry(CountryCode.ES);
        chapter1.addCountry(CountryCode.US);
        chapter1.addExternalURL("url1");
        chapter1.addExternalURL("url2");
        chapter1.setSeason("01");
        chapter1.setMinutes(150);

        List<CountryCode> countries = new ArrayList<>();
        countries.add(CountryCode.ES);
        countries.add(CountryCode.US);
        List<String> externalURLs = new ArrayList<>();
        externalURLs.add("url1");
        externalURLs.add("url2");
        Assert.assertEquals("Titanic", chapter1.getTitle());
        Assert.assertEquals("Titanic original", chapter1.getOriginalTitle());
        Assert.assertEquals(new Integer(1999), chapter1.getYear());
        ListAssert.assertEquals(countries, chapter1.getCountries());
        ListAssert.assertEquals(externalURLs, chapter1.getExternalURLs());
        Assert.assertEquals("01", chapter1.getSeason());
        Assert.assertEquals(new Integer(150), chapter1.getMinutes());

        TVSeries tvSeries = new TVSeries("store.db");
        tvSeries.addChapter(chapter1);
        tvSeries.addChapter(chapter3);
        tvSeries.delete();
        Assert.assertEquals(2, Chapter.getChapters("store.db").size());

        Person arnold = new Person("store.db");
        arnold.setName("Arnold");
        Person silvester = new Person("store.db");
        silvester.setName("Silvester");
        Person christian = new Person("store.db");
        christian.setName("Christian");
        Person george = new Person("store.db");
        george.setName("George");

        Chapter chapter = new Chapter("store.db");
        chapter.setTitle("Predator");

        chapter.addCreator(arnold);
        chapter.addCreator(silvester);
        chapter.addActor(christian);
        chapter.addActor(george);
        List<Person> actors = chapter.getActors();
        List<Person> directors = chapter.getCreators();
        Assert.assertEquals(2, actors.size());
        Assert.assertEquals(2, directors.size());
        Assert.assertEquals("Christian", actors.get(0).getName());
        Assert.assertEquals("George", actors.get(1).getName());
        Assert.assertEquals("Arnold", directors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(1).getName());
        chapter.removeCreator(arnold);
        chapter.removeActor(christian);
        actors = chapter.getActors();
        directors = chapter.getCreators();
        Assert.assertEquals(1, actors.size());
        Assert.assertEquals(1, directors.size());
        Assert.assertEquals("George", actors.get(0).getName());
        Assert.assertEquals("Silvester", directors.get(0).getName());
        chapter.removeCreators();
        chapter.removeActors();
        Assert.assertEquals(0, chapter.getCreators().size());
        Assert.assertEquals(0, chapter.getActors().size());

        VideoFile videoFile1 = new VideoFile("store.db");
        videoFile1.setName("video1");
        VideoFile videoFile2 = new VideoFile("store.db");
        videoFile2.setName("video2");
        chapter.addVideoFile(videoFile1);
        chapter.addVideoFile(videoFile2);
        List<VideoFile> videoFiles = chapter.getVideoFiles();
        Assert.assertEquals(2, videoFiles.size());
        Assert.assertEquals("video1", videoFiles.get(0).getName());
        Assert.assertEquals("video2", videoFiles.get(1).getName());
        chapter.removeVideoFile(videoFile1);
        videoFiles = chapter.getVideoFiles();
        Assert.assertEquals(1, videoFiles.size());
        Assert.assertEquals("video2", videoFiles.get(0).getName());
        chapter.removeVideoFiles();
        Assert.assertEquals(0, chapter.getVideoFiles().size());
    }

    @org.junit.Test
    public void testPeople() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        Person arnold = new Person("store.db");
        arnold.setName("Arnold");
        arnold.addAlias("Arnold S.");
        Person silvester = new Person("store.db");
        silvester.setName("Silvester");
        silvester.addAlias("Silvester S.");
        Person christian = new Person("store.db");
        christian.setName("Christian");
        christian.addAlias("Christian B.");
        christian.addAlias("Christian Bale");
        Person george = new Person("store.db");
        george.setName("George");
        george.addAlias("George C.");
        george.addAlias("George Cl.");
        george.addAlias("George Clooney");

        List<String> aliases = new ArrayList<>();
        aliases.add("Arnold S.");
        Assert.assertEquals("Arnold", arnold.getName());
        ListAssert.assertEquals(aliases, arnold.getAliases());

        aliases.clear();
        aliases.add("Silvester S.");
        Assert.assertEquals("Silvester", silvester.getName());
        ListAssert.assertEquals(aliases, silvester.getAliases());

        aliases.clear();
        aliases.add("Christian B.");
        aliases.add("Christian Bale");
        Assert.assertEquals("Christian", christian.getName());
        ListAssert.assertEquals(aliases, christian.getAliases());

        aliases.clear();
        aliases.add("George C.");
        aliases.add("George Cl.");
        aliases.add("George Clooney");
        Assert.assertEquals("George", george.getName());
        ListAssert.assertEquals(aliases, george.getAliases());

        Movie movie = new Movie("store.db");
        movie.setTitle("Predator");
        movie.addCreator(arnold);
        movie.addActor(silvester);
        movie.addActor(christian);

        TVSeries tvSeries = new TVSeries("store.db");
        tvSeries.setTitle("Breaking bad");
        tvSeries.addCreator(arnold);
        tvSeries.addCreator(christian);
        tvSeries.addActor(silvester);
        tvSeries.addActor(george);

        Chapter chapter = new Chapter("store.db");
        chapter.setTitle("The white walkers");
        chapter.addActor(arnold);
        chapter.addActor(christian);
        chapter.addCreator(silvester);
        chapter.addCreator(george);

        Assert.assertEquals(4, Person.getPeople("store.db").size());

        Assert.assertEquals(0, arnold.getMoviesAsActor("store.db").size());
        Assert.assertEquals("Predator", arnold.getMoviesAsDirector("store.db").get(0).getTitle());
        Assert.assertEquals("Predator", silvester.getMoviesAsActor("store.db").get(0).getTitle());
        Assert.assertEquals(0, silvester.getMoviesAsDirector("store.db").size());
        Assert.assertEquals("Predator", christian.getMoviesAsActor("store.db").get(0).getTitle());
        Assert.assertEquals(0, christian.getMoviesAsDirector("store.db").size());
        Assert.assertEquals(0, george.getMoviesAsActor("store.db").size());
        Assert.assertEquals(0, george.getMoviesAsDirector("store.db").size());

        Assert.assertEquals(0, arnold.getTVSeriesAsActor("store.db").size());
        Assert.assertEquals("Breaking bad", arnold.getTVSeriesAsCreator("store.db").get(0).getTitle());
        Assert.assertEquals("Breaking bad", silvester.getTVSeriesAsActor("store.db").get(0).getTitle());
        Assert.assertEquals(0, silvester.getTVSeriesAsCreator("store.db").size());
        Assert.assertEquals(0, christian.getTVSeriesAsActor("store.db").size());
        Assert.assertEquals("Breaking bad", christian.getTVSeriesAsCreator("store.db").get(0).getTitle());
        Assert.assertEquals("Breaking bad", george.getTVSeriesAsActor("store.db").get(0).getTitle());
        Assert.assertEquals(0, george.getTVSeriesAsCreator("store.db").size());

        Assert.assertEquals("The white walkers", arnold.getChaptersAsActor("store.db").get(0).getTitle());
        Assert.assertEquals(0, arnold.getChaptersAsDirector("store.db").size());
        Assert.assertEquals(0, silvester.getChaptersAsActor("store.db").size());
        Assert.assertEquals("The white walkers", silvester.getChaptersAsDirector("store.db").get(0).getTitle());
        Assert.assertEquals("The white walkers", christian.getChaptersAsActor("store.db").get(0).getTitle());
        Assert.assertEquals(0, christian.getChaptersAsDirector("store.db").size());
        Assert.assertEquals(0, george.getChaptersAsActor("store.db").size());
        Assert.assertEquals("The white walkers", george.getChaptersAsDirector("store.db").get(0).getTitle());

        movie.delete();
        tvSeries.delete();
        chapter.delete();

        Assert.assertEquals(4, Person.getPeople("store.db").size());
    }

    @org.junit.Test
    public void testCompanies() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        Company hbo = new Company("store.db");
        hbo.setName("HBO");
        hbo.addAlias("H.B.O.");
        Company pixar = new Company("store.db");
        pixar.setName("Pixar");
        pixar.addAlias("Disney Pixar");
        pixar.addAlias("Pixar Inc.");

        Movie movie = new Movie("store.db");
        movie.setTitle("Predator");
        movie.addProductionCompany(hbo);

        TVSeries tvSeries = new TVSeries("store.db");
        tvSeries.setTitle("Breaking bad");
        tvSeries.addProductionCompany(pixar);

        Assert.assertEquals(2, Company.getCompanies("store.db").size());
        Assert.assertEquals(1, hbo.getMovies("store.db").size());
        Assert.assertEquals("Predator", hbo.getMovies("store.db").get(0).getTitle());
        Assert.assertEquals(0, hbo.getTVSeries("store.db").size());
        Assert.assertEquals(0, pixar.getMovies("store.db").size());
        Assert.assertEquals(1, pixar.getTVSeries("store.db").size());
        Assert.assertEquals("Breaking bad", pixar.getTVSeries("store.db").get(0).getTitle());

        movie.delete();

        Assert.assertEquals(2, Company.getCompanies("store.db").size());

        List<String> aliases = new ArrayList<>();
        aliases.add("H.B.O.");
        Assert.assertEquals("HBO", hbo.getName());
        ListAssert.assertEquals(aliases, hbo.getAliases());

        aliases.clear();
        aliases.add("Disney Pixar");
        aliases.add("Pixar Inc.");
        Assert.assertEquals("Pixar", pixar.getName());
        ListAssert.assertEquals(aliases, pixar.getAliases());
    }

    @org.junit.Test
    public void testVideoFiles() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        VideoFile videoFile = new VideoFile("store.db");
        videoFile.setHash("hash");
        videoFile.setLength(150L);
        videoFile.setName("video1");
        videoFile.addAdditionalSource("torrent1");
        videoFile.addAdditionalSource("torrent2");
        videoFile.setMinutes(120);
        videoFile.setResolution(1080);
        videoFile.setQuality(QualityCode.HD);
        videoFile.addLanguage(LanguageCode.es);
        videoFile.addLanguage(LanguageCode.en);
        videoFile.addLanguage(LanguageCode.ru);
        List<String> additionalSources = new ArrayList<>();
        additionalSources.add("torrent1");
        additionalSources.add("torrent2");
        List<LanguageCode> languages = new ArrayList<>();
        languages.add(LanguageCode.es);
        languages.add(LanguageCode.en);
        languages.add(LanguageCode.ru);
        Assert.assertEquals("hash", videoFile.getHash());
        Assert.assertEquals(new Long(150L), videoFile.getLength());
        Assert.assertEquals("video1", videoFile.getName());
        ListAssert.assertEquals(additionalSources, videoFile.getAdditionalSources());
        Assert.assertEquals(new Integer(120), videoFile.getMinutes());
        Assert.assertEquals(new Integer(1080), videoFile.getResolution());
        Assert.assertEquals(QualityCode.HD, videoFile.getQuality());
        ListAssert.assertEquals(languages, videoFile.getLanguages());

        SubtitleFile subtitleFile1 = new SubtitleFile("store.db");
        subtitleFile1.setName("sub1");
        SubtitleFile subtitleFile2 = new SubtitleFile("store.db");
        subtitleFile2.setName("sub2");
        videoFile.addSubtitleFile(subtitleFile1);
        videoFile.addSubtitleFile(subtitleFile2);

        Assert.assertEquals(2, videoFile.getSubtitleFiles().size());
        Assert.assertEquals("sub1", videoFile.getSubtitleFiles().get(0).getName());
        Assert.assertEquals("sub2", videoFile.getSubtitleFiles().get(1).getName());

        subtitleFile1.delete();

        Assert.assertEquals(1, videoFile.getSubtitleFiles().size());
        Assert.assertEquals("sub2", videoFile.getSubtitleFiles().get(0).getName());
    }

    @org.junit.Test
    public void testSubtitleFiles() {
        DatabaseMediator.dropAndCreate("store.db", "a");

        SubtitleFile subtitleFile = new SubtitleFile("store.db");
        subtitleFile.setHash("hash");
        subtitleFile.setLength(150L);
        subtitleFile.setName("name");
        subtitleFile.addLanguage(LanguageCode.es);
        subtitleFile.addLanguage(LanguageCode.en);
        subtitleFile.addLanguage(LanguageCode.ru);
        List<LanguageCode> languages = new ArrayList<>();
        languages.add(LanguageCode.es);
        languages.add(LanguageCode.en);
        languages.add(LanguageCode.ru);
        Assert.assertEquals("hash", subtitleFile.getHash());
        Assert.assertEquals(new Long(150L), subtitleFile.getLength());
        Assert.assertEquals("name", subtitleFile.getName());
        ListAssert.assertEquals(languages, subtitleFile.getLanguages());
    }
}