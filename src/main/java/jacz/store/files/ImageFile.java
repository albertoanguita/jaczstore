package jacz.store.files;

import jacz.store.common.CustomElementLoaderFactory;
import jacz.util.io.object_serialization.Serializer;

import java.text.ParseException;
import java.util.List;

/**
 * An image file
 *
 * todo not needed. For images we only send the hash, since they are automatically transferred and its metadata is never shown
 */
public final class ImageFile extends File {

    public ImageFile(String hash, String comment, Long fileLength, String fileFormat, String fileName) {
        super(hash, comment, fileLength, fileFormat, fileName);
    }

    public static ImageFile loadFromString(String string) throws IllegalArgumentException {
        try {
            List<String> stringList = Serializer.deserializeListFromReadableString(string, CustomElementLoaderFactory.CUSTOM_PRE, CustomElementLoaderFactory.CUSTOM_POST);
            if (stringList.size() != 5) {
                throw new IllegalArgumentException("Invalid number of fields: " + stringList.size());
            }
            try {
                return new ImageFile(stringList.get(0), stringList.get(1), Long.parseLong(stringList.get(2)), stringList.get(3), stringList.get(4));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid parse strings: " + stringList);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid string: " + string);
        }
    }
}
