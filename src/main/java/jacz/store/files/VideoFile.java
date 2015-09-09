package jacz.store.files;

import jacz.store.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.text.ParseException;
import java.util.List;

/**
 * A video file
 */
public final class VideoFile extends PlayFile {

    /**
     * Type of rip of a video file (http://en.wikipedia.org/wiki/Pirated_movie_release_types)
     */
    public final String videoRip;

    /**
     * Native x resolution of the video
     */
    public final Integer xResolution;

    /**
     * Native y resolution of the video
     */
    public final Integer yResolution;


    public VideoFile(String hash, String comment, Long fileLength, String fileFormat, String fileName, Long reproductionTime, ShortQuality shortQuality, String videoRip, Integer xResolution, Integer yResolution) {
        super(hash, comment, fileLength, fileFormat, fileName, reproductionTime, shortQuality);
        this.videoRip = videoRip;
        this.xResolution = xResolution;
        this.yResolution = yResolution;
    }

    public static VideoFile loadFromString(String string) throws IllegalArgumentException {
        try {
            List<String> stringList = Serializer.deserializeListFromReadableString(string, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
            if (stringList.size() != 10) {
                throw new IllegalArgumentException("Invalid number of fields: " + stringList.size());
            }
            try {
                return new VideoFile(stringList.get(0), stringList.get(1), Long.parseLong(stringList.get(2)), stringList.get(3), stringList.get(4), Long.parseLong(stringList.get(5)), ShortQuality.parse(stringList.get(6)), stringList.get(7), Integer.parseInt(stringList.get(8)), Integer.parseInt(stringList.get(9)));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid parse strings: " + stringList);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid string: " + string);
        }
    }


    public List<String> toStringList() {
        List<String> stringList = super.toStringList();
        stringList.add(videoRip);
        stringList.add(xResolution.toString());
        stringList.add(yResolution.toString());
        return stringList;
    }
}
