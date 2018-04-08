package player.project.com.musicplayer.models;

import java.lang.reflect.Array;

/**
 * Created by TrangHT on 31/1/2018.
 */
public class Album {
    private String albumName;
    private int numberOfSong;
    private String artist;
    private int imgResoure;

    public Album() {

    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getImgResoure() {
        return imgResoure;
    }

    public void setImgResoure(int imgResoure) {
        this.imgResoure = imgResoure;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public void setNumberOfSong(int numberOfSong) {
        this.numberOfSong = numberOfSong;
    }
}
