package jacz.store.old2.music;

import jacz.store.old2.ItemInterface;
import jacz.store.old2.Libraries;
import jacz.store.old2.common.Creation;
import jacz.store.old2.common.CustomElementLoaderFactory;
import jacz.store.old2.common.IdentifierMap;
import jacz.store.old2.common.LibraryItem;
import jacz.store.old2.db_mediator.DBException;
import jacz.store.old2.files.AudioList;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A music album composed of a list of audioLists
 */
public class AudioAlbum extends Creation {

    public static final String AUDIO_LISTS = "audioLists";

    private List<AudioList> audioLists;

    public AudioAlbum(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(Libraries.AUDIO_ALBUM_LIBRARY, itemInterface, values, itemIsNew);
    }

    public List<String> getFields() {
        List<String> fields = super.getFields();
        fields.addAll(getOwnFields());
        return fields;
    }

    public static List<String> getFieldsStatic() {
        List<String> fields = Creation.getFieldsStatic();
        fields.addAll(getOwnFields());
        return fields;
    }

    private static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(AUDIO_LISTS);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        audioLists = new ArrayList<>();
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        audioLists = initializeValueCustomElementList(AUDIO_LISTS, values, CustomElementLoaderFactory.audioListLoader);
    }

    public List<AudioList> getAudioLists() {
        return audioLists;
    }

    public void addAudioList(int index, AudioList element) throws DBException, IndexOutOfBoundsException, IOException {
        audioLists = addElementWithRepetition(audioLists, index, element, AUDIO_LISTS, CustomElementLoaderFactory.audioListLoader);
    }

    public void removeAudioList(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(audioLists, index, AUDIO_LISTS, CustomElementLoaderFactory.audioListLoader);
    }

    public void setAudioLists(List<AudioList> audioLists) throws DBException, IOException {
        this.audioLists = updateList(this.audioLists, audioLists, AUDIO_LISTS, CustomElementLoaderFactory.audioListLoader);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case AUDIO_LISTS:
                return getAudioLists();

            default:
                return super.get(field);
        }
    }

    @Override
    public void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof AudioAlbum)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        AudioAlbum o = (AudioAlbum) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setAudioLists(mergeList(audioLists, o.audioLists));
    }
}
