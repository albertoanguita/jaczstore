package jacz.store.old2.test;

import jacz.store.old2.Database;
import jacz.store.old2.Libraries;
import jacz.store.old2.ResultSet;
import jacz.store.old2.db_mediator.CSVDBMediator;
import jacz.store.old2.music.AudioAlbum;

/**
 * Created with IntelliJ IDEA.
 * User: Alberto
 * Date: 12/01/14
 * Time: 23:34
 * To change this template use OldFile | Settings | OldFile Templates.
 */
public class TestLoad {

    public static void main(String[] args) throws Exception {

        try {
            Database database = new Database(new CSVDBMediator(".\\etc\\CSVdatabase4\\"));

            ResultSet resultSet = database.getAllItems(Libraries.AUDIO_ALBUM_LIBRARY);


            AudioAlbum audioAlbum = (AudioAlbum) database.createNewItem(Libraries.AUDIO_ALBUM_LIBRARY);
//            audioAlbum.addSong(0, new AlbumSongFile("aaa", "comment", 5L, "mp3", "aaa.mp3", 6L, ShortQuality._4, "studio", null));
//            audioAlbum.addSong(0, new AlbumSongFile("bbb", "commen2t", 26L, "wav", "bbb.mp3", 3L, ShortQuality._2, "live", "000"));

////            Person person = (Person) database.getItem(Libraries.PERSON_LIBRARY, "0000000000");
//            ResultSet resultSet = database.getAllItems(Libraries.PERSON_LIBRARY);
//            while (resultSet.hasNext()) {
//                LibraryItem item = resultSet.next();
//                System.out.println("ey");
//            }

            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
