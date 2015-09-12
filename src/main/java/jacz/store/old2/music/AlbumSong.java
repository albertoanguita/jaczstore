package jacz.store.old2.music;

/**
 * A song referenced in an audio album
 * todo remove
 */
public class AlbumSong {

    public final String songFile;

    public final String songTitle;

    public final String songItem;

    public AlbumSong(String songFile, String songTitle, String songItem) {
        this.songFile = songFile;
        this.songTitle = songTitle;
        this.songItem = songItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumSong albumSong = (AlbumSong) o;

        if (songFile != null ? !songFile.equals(albumSong.songFile) : albumSong.songFile != null) return false;
        if (songItem != null ? !songItem.equals(albumSong.songItem) : albumSong.songItem != null) return false;
        if (songTitle != null ? !songTitle.equals(albumSong.songTitle) : albumSong.songTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = songFile != null ? songFile.hashCode() : 0;
        result = 31 * result + (songTitle != null ? songTitle.hashCode() : 0);
        result = 31 * result + (songItem != null ? songItem.hashCode() : 0);
        return result;
    }
}
