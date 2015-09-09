package jacz.store.old;

import jacz.store.Database;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;
import jacz.store.music.Song;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Songs library
 */
public class SongLibrary extends ItemContainer<Song> {

    SongLibrary(Database database, DBMediator dbMediator) {
        super("SongLibrary", database, dbMediator);
    }

    @Override
    protected List<String> defineFields() {
        List<String> songFields = new ArrayList<>();
        songFields.add("identifier");
        songFields.add("creationDate");
        songFields.add("modificationDate");
        songFields.add("title");
        songFields.add("originalTitle");
        songFields.add("year");
        songFields.add("creators");
        songFields.add("audioFiles");
        songFields.add("lyrics");
        songFields.add("audioAlbums");
        songFields.add("labels");
        songFields.add("writers");
        songFields.add("producers");
        return songFields;
    }

    @Override
    protected Song loadItem(Map<String, String> record) throws ParseException {
//        return new Song(database, record);
        return null;
    }

    @Override
    public Song buildNewItem() throws ParseException, DBException, IOException {
//        return new Song(database);
        return null;
    }
}
