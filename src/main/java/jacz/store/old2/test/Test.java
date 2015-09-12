package jacz.store.old2.test;

import jacz.store.old2.Database;
import jacz.store.old2.Libraries;
import jacz.store.old2.ResultSet;
import jacz.store.old2.common.Person;
import jacz.store.old2.db_mediator.CSVDBMediator;
import jacz.store.old2.db_mediator.DBMediator;

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
