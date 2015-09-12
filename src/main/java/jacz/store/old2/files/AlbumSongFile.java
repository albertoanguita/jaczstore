package jacz.store.old2.files;

import jacz.store.old2.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.text.ParseException;
import java.util.List;

/**
 * A song file in an audio album
 */
public class AlbumSongFile extends AudioFile {

    public final String songItem;

    public AlbumSongFile(String hash, String comment, Long fileLength, String fileFormat, String fileName, Long reproductionTime, ShortQuality shortQuality, String audioRecording, String songItem) {
        super(hash, comment, fileLength, fileFormat, fileName, reproductionTime, shortQuality, audioRecording);
        this.songItem = songItem;
    }

    public static AlbumSongFile loadFromString(String string) throws IllegalArgumentException {
        try {
            List<String> stringList = Serializer.deserializeListFromReadableString(string, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
            if (stringList.size() != 9) {
                throw new IllegalArgumentException("Invalid number of fields: " + stringList.size());
            }
            try {
                return new AlbumSongFile(stringList.get(0), stringList.get(1), Long.parseLong(stringList.get(2)), stringList.get(3), stringList.get(4), Long.parseLong(stringList.get(5)), ShortQuality.parse(stringList.get(6)), stringList.get(7), stringList.get(8));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid parse strings: " + stringList);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid string: " + string);
        }
    }

    protected List<String> toStringList() {
        List<String> stringList = super.toStringList();
        stringList.add(songItem);
        return stringList;
    }
}
