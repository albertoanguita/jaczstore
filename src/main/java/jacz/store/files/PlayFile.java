package jacz.store.files;

import java.util.List;

/**
 * A file that can be played/reproduced (e.g. an mp3 file, an avi file)
 */
public abstract class PlayFile extends File {

    public final Long reproductionTime;

    public final ShortQuality shortQuality;

    public PlayFile(String hash, String comment, Long fileLength, String fileFormat, String fileName, Long reproductionTime, ShortQuality shortQuality) {
        super(hash, comment, fileLength, fileFormat, fileName);
        this.reproductionTime = reproductionTime;
        this.shortQuality = shortQuality;
    }

    protected List<String> toStringList() {
        List<String> stringList = super.toStringList();
        stringList.add(reproductionTime.toString());
        stringList.add(shortQuality.toString());
        return stringList;
    }
}
