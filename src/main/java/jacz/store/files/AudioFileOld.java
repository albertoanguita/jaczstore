package jacz.store.files;

import jacz.store.ItemInterface;

import java.text.ParseException;
import java.util.Map;

/**
 * Audio file item
 */
public class AudioFileOld extends OldPlayFile {

    /**
     * Encoding of the audio file (http://en.wikipedia.org/wiki/Audio_file_format)
     */
    public enum AudioFormat {
        MP3,
        OGG,
        WAV,
        OTHER
    }

    public enum Recording {
        STUDIO,
        LIVE,
        RADIO_EDIT,
        OTHER
    }

    //private Integer bitRate;

//    public AudioFileOld(Database database) throws DBException, IOException {
//        super("AudioFileLibrary", database);
//    }
//
//    public AudioFileOld(Database database, String identifier) throws DBException, IOException {
//        super("AudioFileLibrary", database, identifier);
//    }

    public AudioFileOld(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
//        super("AudioFileLibrary", itemInterface, values, itemIsNew);
    }
}
