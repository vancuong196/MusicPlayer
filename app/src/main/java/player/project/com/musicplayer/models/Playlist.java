package player.project.com.musicplayer.models;

import java.io.Serializable;

/**
 * Created by Cuong on 2/1/2018.
 */

public class Playlist implements Serializable {

    private String name;
    private int numberOfSongs;

    public Playlist() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public Playlist(String name, int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
        this.name = name;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public String getName() {
        return name;
    }
}
