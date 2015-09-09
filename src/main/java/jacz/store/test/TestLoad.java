package jacz.store.test;

import jacz.store.Database;
import jacz.store.Libraries;
import jacz.store.ResultSet;
import jacz.store.common.LibraryItem;
import jacz.store.common.Person;
import jacz.store.db_mediator.CSVDBMediator;
import jacz.store.files.AlbumSongFile;
import jacz.store.files.ShortQuality;
import jacz.store.music.AudioAlbum;

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
