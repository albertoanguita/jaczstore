package jacz.store.files;

import jacz.store.ItemInterface;

import java.text.ParseException;
import java.util.Map;

/**
 * A video file
 */
public class VideoFileOld extends OldPlayFile {

    public enum VideoFormat {
        MPEG,
        H264,
        ONLINE,
    }

    /**
     * Native x resolution of the video
     */
    private Integer xResolution;

    /**
     * Native y resolution of the video
     */
    private Integer yResolution;

//    public VideoFileOld(Database database) throws DBException, IOException {
//        super("VideoFileOld", database);
//    }
//
//    public VideoFileOld(Database database, String identifier) throws DBException, IOException {
//        super("VideoFileOld", database, identifier);
//    }

    public VideoFileOld(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
//        super(Libraries.VIDEO_FILE_LIBRARY, itemInterface, values, itemIsNew);
//        super("", itemInterface, values, itemIsNew);
        xResolution = Integer.parseInt(values.get("xResolution"));
        yResolution = Integer.parseInt(values.get("yResolution"));
    }

//    public static VideoFileOld buildItem(Database database, Map<String, String> values) throws ParseException {
//        return new VideoFileOld(database, values);
//    }
}
