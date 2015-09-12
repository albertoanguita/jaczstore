package jacz.store.old2.files;

import jacz.store.old2.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract data element
 */
public abstract class Data {

    /**
     * Hash of the data
     */
    public final String hash;

    /**
     * Natural language comment describing the data
     */
    public final String comment;

    /**
     * Length in bytes of this data element
     */
    public final Long length;

    /**
     * Encoding of the data (mp3, ogg, wav, avi...) (http://en.wikipedia.org/wiki/Audio_file_format)
     */
    public final String fileFormat;

    protected Data(String hash, String comment, Long length, String fileFormat) {
        this.hash = hash;
        this.comment = comment;
        this.length = length;
        this.fileFormat = fileFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;

        File file = (File) o;

        return hash.equals(file.hash);
    }

    @Override
    public int hashCode() {
        return hash.hashCode();
    }

    public final String saveElement() {
        return Serializer.serializeListToReadableString(toStringList(), CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
    }

    protected List<String> toStringList() {
        List<String> stringList = new ArrayList<>();
        stringList.add(hash);
        stringList.add(comment);
        stringList.add(length.toString());
        stringList.add(fileFormat);
        return stringList;
    }
}
