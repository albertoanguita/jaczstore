package jacz.store.common;

import jacz.store.files.AlbumSongFile;
import jacz.store.files.AudioFile;
import jacz.store.files.AudioList;
import jacz.store.files.VideoFile;

/**
 * Custom element loaders
 */
public class CustomElementLoaderFactory {

    public static final String CUSTOM_PRE = "{{";

    public static final String CUSTOM_POST = "}}";

    private static class AudioListLoader implements CustomElementLoader<AudioList> {

        @Override
        public AudioList loadElement(String string) {
            return AudioList.loadFromString(string);
        }

        @Override
        public String saveElement(AudioList element) {
            return element.saveElement();
        }
    }

    private static class AudioAlbumFileLoader implements CustomElementLoader<AlbumSongFile> {

        @Override
        public AlbumSongFile loadElement(String string) {
            return AlbumSongFile.loadFromString(string);
        }

        @Override
        public String saveElement(AlbumSongFile element) {
            return element.saveElement();
        }
    }

    private static class AudioFileLoader implements CustomElementLoader<AudioFile> {

        @Override
        public AudioFile loadElement(String string) {
            return AudioFile.loadFromString(string);
        }

        @Override
        public String saveElement(AudioFile element) {
            return element.saveElement();
        }
    }

    private static class VideoFileLoader implements CustomElementLoader<VideoFile> {

        @Override
        public VideoFile loadElement(String string) {
            return VideoFile.loadFromString(string);
        }

        @Override
        public String saveElement(VideoFile element) {
            return element.saveElement();
        }
    }

    public static final AudioListLoader audioListLoader = new AudioListLoader();

    public static final AudioAlbumFileLoader audioAlbumFileLoader = new AudioAlbumFileLoader();

    public static final AudioFileLoader audioFileLoader = new AudioFileLoader();

    public static final VideoFileLoader videoFileLoader = new VideoFileLoader();
}
