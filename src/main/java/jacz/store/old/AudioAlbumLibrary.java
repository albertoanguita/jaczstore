package jacz.store.old;

import jacz.store.Database;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;
import jacz.store.music.AudioAlbum;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Audio album library
 */
public class AudioAlbumLibrary extends ItemContainer<AudioAlbum> {

    AudioAlbumLibrary(Database database, DBMediator dbMediator) {
        super("AudioAlbumLibrary", database, dbMediator);
    }

    @Override
    protected List<String> defineFields() {
        List<String> audioAlbumFields = new ArrayList<>();
        audioAlbumFields.add("identifier");
        audioAlbumFields.add("creationDate");
        audioAlbumFields.add("modificationDate");
        audioAlbumFields.add("title");
        audioAlbumFields.add("originalTitle");
        audioAlbumFields.add("year");
        audioAlbumFields.add("creators");
        audioAlbumFields.add("songs");
        audioAlbumFields.add("labels");
        audioAlbumFields.add("producers");
        return audioAlbumFields;
    }

    @Override
    protected AudioAlbum loadItem(Map<String, String> record) throws ParseException {
//        return new AudioAlbum(database, record);
        return null;
    }

    @Override
    public AudioAlbum buildNewItem() throws ParseException, DBException, IOException {
//        return new AudioAlbum(database);
        return null;
    }
}
