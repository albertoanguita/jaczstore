package jacz.store.files;

import jacz.store.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.text.ParseException;
import java.util.List;

/**
 * An audio file
 */
public class AudioFile extends PlayFile {

    /**
     * The type of recording of a song (studio, live, radio edit...)
     */
    public final String audioRecording;

    public AudioFile(String hash, String comment, Long fileLength, String fileFormat, String fileName, Long reproductionTime, ShortQuality shortQuality, String audioRecording) {
        super(hash, comment, fileLength, fileFormat, fileName, reproductionTime, shortQuality);
        this.audioRecording = audioRecording;
    }

    public static AudioFile loadFromString(String string) throws IllegalArgumentException {
        try {
            List<String> stringList = Serializer.deserializeListFromReadableString(string, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
            if (stringList.size() != 8) {
                throw new IllegalArgumentException("Invalid number of fields: " + stringList.size());
            }
            try {
                return new AudioFile(stringList.get(0), stringList.get(1), Long.parseLong(stringList.get(2)), stringList.get(3), stringList.get(4), Long.parseLong(stringList.get(5)), ShortQuality.parse(stringList.get(6)), stringList.get(7));
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
