package jacz.store.common;

import jacz.store.ItemInterface;
import jacz.store.db_mediator.DBException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Library items with a list of associated tags and images. There is one main image, a reduced version of this image, and a list of additional
 * images. This list supports repetition
 * <p/>
 * Each image is represented with an identifier to an ImageFile item
 */
public class TaggedLibraryItemWithImages extends TaggedLibraryItem {

    public static final String MAIN_IMAGE = "mainImage";

    public static final String MAIN_IMAGE_REDUCED = "mainImageReduced";

    public static final String IMAGES = "images";

    /**
     * The main image, in its original size
     */
    private String mainImage;

    /**
     * The main image, reduced size
     */
    private String mainImageReduced;

    /**
     * Images for this item
     */
    private List<String> images;

    protected TaggedLibraryItemWithImages(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(container, itemInterface, values, itemIsNew);
    }

    public List<String> getFields() {
        List<String> fields = super.getFields();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = TaggedLibraryItem.getFieldsStatic();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(MAIN_IMAGE);
        fields.add(MAIN_IMAGE_REDUCED);
        fields.add(IMAGES);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        mainImage = null;
        mainImageReduced = null;
        images = new ArrayList<>();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        mainImage = initializeValueString(MAIN_IMAGE, values);
        mainImageReduced = initializeValueString(MAIN_IMAGE_REDUCED, values);
        images = initializeValueStringList(IMAGES, values);
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) throws DBException, IOException {
        this.mainImage = updateField(this.mainImage, mainImage, MAIN_IMAGE);
    }

    public String getMainImageReduced() {
        return mainImageReduced;
    }

    public void setMainImageReduced(String mainImageReduced) throws DBException, IOException {
        this.mainImageReduced = updateField(this.mainImageReduced, mainImageReduced, MAIN_IMAGE_REDUCED);
    }

    public List<String> getImages() {
        return images;
    }

    public void addImage(int index, String element) throws DBException, IndexOutOfBoundsException, IOException {
        images = addElementWithRepetition(images, index, element, IMAGES);
    }

    public void removeImage(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(images, index, IMAGES);
    }

    public void setImages(List<String> images) throws DBException, IOException {
        this.images = updateList(this.images, images, IMAGES);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case MAIN_IMAGE:
                return getMainImage();

            case MAIN_IMAGE_REDUCED:
                return getMainImageReduced();

            case IMAGES:
                return getImages();

            default:
                return super.get(field);
        }
    }

    @Override
    public Float match(LibraryItem anotherItem) {
        return super.match(anotherItem);
    }

    @Override
    protected void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof TaggedLibraryItemWithImages)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        TaggedLibraryItemWithImages o = (TaggedLibraryItemWithImages) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setMainImage(mergeElement(mainImage, o.mainImage));
        setMainImageReduced(mergeElement(mainImageReduced, o.mainImageReduced));
        setImages(mergeList(images, o.images));
    }
}
