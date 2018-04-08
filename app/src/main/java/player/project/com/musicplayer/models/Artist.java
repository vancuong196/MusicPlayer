package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 4/2/2018.
 */

public class Artist {
    String name;
    int numberOfSong;
    int numberOfAlbum;

    public Artist() {

    }

    public Artist(String name, int numberOfSong, int numberOfAlbum) {
        this.name = name;
        this.numberOfSong = numberOfSong;
        this.numberOfAlbum = numberOfAlbum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public void setNumberOfSong(int numberOfSong) {
        this.numberOfSong = numberOfSong;
    }

    public int getNumberOfAlbum() {
        return numberOfAlbum;
    }

    public void setNumberOfAlbum(int numberOfAlbum) {
        this.numberOfAlbum = numberOfAlbum;
    }
}
