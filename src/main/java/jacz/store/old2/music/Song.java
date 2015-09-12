package jacz.store.old2.music;

import jacz.store.old2.ItemInterface;
import jacz.store.old2.Libraries;
import jacz.store.old2.common.Creation;
import jacz.store.old2.common.CustomElementLoaderFactory;
import jacz.store.old2.common.IdentifierMap;
import jacz.store.old2.common.LibraryItem;
import jacz.store.old2.db_mediator.DBException;
import jacz.store.old2.files.AudioFile;
import jacz.store.old2.files.VideoFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A song
 * todo include comments for files
 */
public class Song extends Creation {

    public static final String AUDIO_FILES = "audioFiles";

    public static final String VIDEO_FILES = "videoFiles";

    public static final String LYRICS = "lyrics";

    /**
     * List of audio files for this song
     */
    private List<AudioFile> audioFiles;

    /**
     * Music videos of this song
     */
    private List<VideoFile> videoFiles;

    /**
     * Lyrics of this song
     */
    private String lyrics;

    public Song(ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
        super(Libraries.SONG_LIBRARY, itemInterface, values, itemIsNew);
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

    public static List<String> getOwnFields() {
        List<String> fields = new ArrayList<>();
        fields.add(AUDIO_FILES);
        fields.add(VIDEO_FILES);
        fields.add(LYRICS);
        return fields;
    }

    @Override
    protected void initValues() {
        super.initValues();
        audioFiles = new ArrayList<>();
        videoFiles = new ArrayList<>();
        lyrics = null;
    }

    @Override
    protected void loadValues(Map<String, String> values) throws ParseException {
        super.loadValues(values);
        audioFiles = initializeValueCustomElementList(AUDIO_FILES, values, CustomElementLoaderFactory.audioFileLoader);
        videoFiles = initializeValueCustomElementList(VIDEO_FILES, values, CustomElementLoaderFactory.videoFileLoader);
        lyrics = initializeValueString(LYRICS, values);
    }

    public List<AudioFile> getAudioFiles() {
        return audioFiles;
    }

    public void addAudioFile(int index, AudioFile element) throws DBException, IndexOutOfBoundsException, IOException {
        audioFiles = addElementWithoutRepetition(audioFiles, index, element, AUDIO_FILES, CustomElementLoaderFactory.audioFileLoader);
    }

    public void removeAudioFile(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(audioFiles, index, AUDIO_FILES, CustomElementLoaderFactory.audioFileLoader);
    }

    public void setAudioFiles(List<AudioFile> audioFiles) throws DBException, IOException {
        this.audioFiles = updateList(this.audioFiles, audioFiles, AUDIO_FILES, CustomElementLoaderFactory.audioFileLoader);
    }

    public List<VideoFile> getVideoFiles() {
        return videoFiles;
    }

    public void addVideoFile(int index, VideoFile element) throws DBException, IndexOutOfBoundsException, IOException {
        videoFiles = addElementWithoutRepetition(videoFiles, index, element, VIDEO_FILES, CustomElementLoaderFactory.videoFileLoader);
    }

    public void removeVideoFile(int index) throws DBException, IndexOutOfBoundsException, IOException {
        removeElement(videoFiles, index, VIDEO_FILES, CustomElementLoaderFactory.videoFileLoader);
    }

    public void setVideoFiles(List<VideoFile> videoFiles) throws DBException, IOException {
        this.videoFiles = updateList(this.videoFiles, videoFiles, VIDEO_FILES, CustomElementLoaderFactory.videoFileLoader);
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) throws DBException, IOException {
        this.lyrics = updateField(this.lyrics, lyrics, LYRICS);
    }

    @Override
    public Object get(String field) {
        switch (field) {
            case AUDIO_FILES:
                return getAudioFiles();

            case VIDEO_FILES:
                return getVideoFiles();

            case LYRICS:
                return getLyrics();

            default:
                return super.get(field);
        }
    }

    @Override
    public void mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalArgumentException, DBException, IOException {
        if (!(anotherItem instanceof Song)) {
            throw new IllegalArgumentException("Invalid argument: " + anotherItem.getClass().getName());
        }
        Song o = (Song) anotherItem;
        super.mergeData(anotherItem, identifierMap);
        setAudioFiles(mergeList(audioFiles, o.audioFiles));
        setVideoFiles(mergeList(videoFiles, o.videoFiles));
        setLyrics(mergeElement(lyrics, o.lyrics));
    }
}
