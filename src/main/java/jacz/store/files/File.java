package jacz.store.files;

import jacz.store.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.util.List;

/**
 * A file located in the disk
 */
public abstract class File extends Data {

    public final String fileName;

    public File(String hash, String comment, Long fileLength, String fileFormat, String fileName) {
        super(hash, comment, fileLength, fileFormat);
        this.fileName = fileName;
    }

    protected List<String> toStringList() {
        List<String> stringList = super.toStringList();
        stringList.add(fileName);
        return stringList;
    }
}
