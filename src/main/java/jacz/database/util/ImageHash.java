package jacz.database.util;

import java.io.Serializable;

/**
 * Stores the info about an image file: hash and file extension
 */
public class ImageHash implements Serializable {

    private static final String SEPARATOR = ".";

    private final String hash;

    private final String extension;

    public ImageHash(String hash, String extension) {
        this.hash = hash;
        this.extension = extension;
    }

    public String getHash() {
        return hash;
    }

    public String getExtension() {
        return extension;
    }

    public String serialize() {
        return hash + SEPARATOR + extension;
    }

    public static ImageHash deserialize(String str) throws IllegalArgumentException {
        int indexOfSeparator = str.indexOf(SEPARATOR);
        if (indexOfSeparator >= 0) {
            return new ImageHash(str.substring(0, indexOfSeparator), str.substring(indexOfSeparator + 1));
        } else {
            throw new IllegalArgumentException("Invalid image hash value: " + str);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageHash imageHash = (ImageHash) o;

        if (!hash.equals(imageHash.hash)) return false;
        return extension.equals(imageHash.extension);

    }

    @Override
    public int hashCode() {
        int result = hash.hashCode();
        result = 31 * result + extension.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ImageHash{" +
                "hash='" + hash + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
