package jacz.store.test;

import jacz.store.Database;
import jacz.store.Libraries;
import jacz.store.ResultSet;
import jacz.store.common.GroupCreator;
import jacz.store.common.Person;
import jacz.store.db_mediator.CSVDBMediator;
import jacz.store.db_mediator.DBMediator;
import jacz.store.music.AlbumSong;
import jacz.store.music.AudioAlbum;

/**
 * Created with IntelliJ IDEA.
 * User: Alberto
 * Date: 23/07/14
 * Time: 21:08
 * To change this template use OldFile | Settings | OldFile Templates.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        DBMediator dbMediator = new CSVDBMediator(".\\etc\\CSVdatabase3\\");
        Database.createEmpty(dbMediator);

        Database database = new Database(dbMediator);

        Person person = (Person) database.createNewItem(Libraries.PERSON_LIBRARY);

        ResultSet resultSet = database.getAllItems(Libraries.PERSON_LIBRARY);


//        person.setName("alb");
//        person.addAlias(0, "albb");
//
//        GroupCreator groupCreator = (GroupCreator) database.createNewItem(Libraries.GROUP_CREATOR_LIBRARY);
//        groupCreator.setName("Jacuzzi co.");
//        groupCreator.addPerson(0, person.getIdentifier());
//
//        AudioAlbum audioAlbum = (AudioAlbum) database.createNewItem(Libraries.AUDIO_ALBUM_LIBRARY);
//        audioAlbum.setTitle("Nevermind");
//        audioAlbum.addSong(0, new AlbumSong("111", "come as you are", null));
//        audioAlbum.addSong(1, new AlbumSong("222", "bleach", "333"));

        database.close();

        System.out.println("end");
    }
}
