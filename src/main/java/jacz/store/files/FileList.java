package jacz.store.files;

import jacz.store.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.util.List;

/**
 * An ordered list of files
 */
public abstract class FileList extends Data {

    public final List<String> fileHashes;

    public final List<String> fileNames;

    public FileList(String hash, String comment, Long totalLength, String fileFormat, List<String> fileHashes, List<String> fileNames) {
        super(hash, comment, totalLength, fileFormat);
        this.fileHashes = fileHashes;
        this.fileNames = fileNames;
    }

    protected List<String> toStringList() {
        List<String> stringList = super.toStringList();
        stringList.add(Serializer.serializeListToReadableString(fileHashes, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST));
        stringList.add(Serializer.serializeListToReadableString(fileNames, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST));
        return stringList;
    }
}
