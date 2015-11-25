package jacz.store;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 20/11/2015.
 */
public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseMediator.dropAndCreate();
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:store.db", "", "");

//        testTVSeries();
        testMovies();
    }

    public static void testTVSeries() {
        TVSeries bb = new TVSeries();
        bb.setTitle("Breaking Bad");
        Chapter chapter1 = new Chapter();
        chapter1.setTitle("ch1");
        Chapter chapter2 = new Chapter();
        chapter2.setTitle("ch2");
        bb.setChapters(chapter1, chapter2);
        for (Chapter chapter : bb.getChapters()) {
            System.out.println(chapter.getTitle());
        }
        System.out.println("---");
        chapter1.delete();
        for (Chapter chapter : bb.getChapters()) {
            System.out.println(chapter.getTitle());
        }
        System.out.println("---");
        bb.removeChapters();
        for (Chapter chapter : bb.getChapters()) {
            System.out.println(chapter.getTitle());
        }
        System.out.println("---");
    }

    public static void testMovies() {
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

        for (Person person : movie.getCreatorsDirectors()) {
            System.out.println(person.getName());
        }
        for (Person person : movie.getActors()) {
            System.out.println(person.getName());
        }
        System.out.println("---");

        movie.removeDirector(arnold);
        movie.removeActor(christian);
        for (Person person : movie.getCreatorsDirectors()) {
            System.out.println(person.getName());
        }
        for (Person person : movie.getActors()) {
            System.out.println(person.getName());
        }
        System.out.println("---");

        movie.removeCreatorsDirectors();
        movie.removeActors();
        for (Person person : movie.getCreatorsDirectors()) {
            System.out.println(person.getName());
        }
        for (Person person : movie.getActors()) {
            System.out.println(person.getName());
        }
        System.out.println("---");

        VideoFile videoFile1 = new VideoFile();
        videoFile1.setName("video1");
        VideoFile videoFile2 = new VideoFile();
        videoFile2.setName("video2");
        movie.addVideoFile(videoFile1);
        movie.addVideoFile(videoFile2);
        for (VideoFile videoFile : movie.getVideoFiles()) {
            System.out.println(videoFile.getName());
        }
        System.out.println("---");

        movie.removeVideoFile(videoFile1);
        for (VideoFile videoFile : movie.getVideoFiles()) {
            System.out.println(videoFile.getName());
        }
        System.out.println("---");

        movie.removeVideoFiles();
        for (VideoFile videoFile : movie.getVideoFiles()) {
            System.out.println(videoFile.getName());
        }
        System.out.println("---");

        ImageFile imageFile = new ImageFile();
        imageFile.setHash("image1");
        movie.setImage(imageFile);
        System.out.println(movie.getImage().getHash());
        movie.removeImage();
        System.out.println(movie.getImage());
        ImageFile imageFile1 = new ImageFile();
        imageFile1.setHash("image2");
        movie.setImage(imageFile1);
        System.out.println(movie.getImage().getHash());
        System.out.println(movie.getImage());


        List<String> externalURLs = new ArrayList<>();
        externalURLs.add("url1");
        externalURLs.add("url2");
        movie.setExternalURLs(externalURLs);
        System.out.println(movie.getExternalURLs());
        movie.addExternalURL("url3");
        System.out.println(movie.getExternalURLs());
        movie.removeExternalURL("url1");
        System.out.println(movie.getExternalURLs());
        movie.removeExternalURLs();
        System.out.println(movie.getExternalURLs());
    }
}
