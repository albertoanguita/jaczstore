package jacz.store.files;

import java.util.List;

/**
 * An ordered list of files that can be played
 */
public abstract class PlayList extends FileList {

    public final Long reproductionTime;

    public final ShortQuality shortQuality;

    public PlayList(String hash, String comment, Long totalLength, String fileFormat, List<String> fileHashes, List<String> fileNames, Long reproductionTime, ShortQuality shortQuality) {
        super(hash, comment, totalLength, fileFormat, fileHashes, fileNames);
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
