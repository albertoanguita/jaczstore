package jacz.store.files;

import jacz.store.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.text.ParseException;
import java.util.List;

/**
 * An ordered list of audio files
 */
public class AudioList extends PlayList {

    /**
     * The type of recording of a song (studio, live, radio edit...)
     */
    public final String audioRecording;

    public AudioList(String hash, String comment, Long totalLength, String fileFormat, List<String> fileHashes, List<String> fileNames, Long reproductionTime, ShortQuality shortQuality, String audioRecording) {
        super(hash, comment, totalLength, fileFormat, fileHashes, fileNames, reproductionTime, shortQuality);
        this.audioRecording = audioRecording;
    }

    public static AudioList loadFromString(String string) throws IllegalArgumentException {
        try {
            List<String> stringList = Serializer.deserializeListFromReadableString(string, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
            if (stringList.size() != 9) {
                throw new IllegalArgumentException("Invalid number of fields: " + stringList.size());
            }
            try {
                List<String> fileHashes = Serializer.deserializeListFromReadableString(stringList.get(4), CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
                List<String> fileNames = Serializer.deserializeListFromReadableString(stringList.get(5), CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
                return new AudioList(stringList.get(0), stringList.get(1), Long.parseLong(stringList.get(2)), stringList.get(3), fileHashes, fileNames, Long.parseLong(stringList.get(6)), ShortQuality.parse(stringList.get(7)), stringList.get(8));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid parse strings: " + stringList);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid string: " + string);
        }
    }

    protected List<String> toStringList() {
        List<String> stringList = super.toStringList();
        stringList.add(audioRecording);
        return stringList;
    }
}
