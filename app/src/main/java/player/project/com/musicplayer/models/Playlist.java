package player.project.com.musicplayer.models;

import java.io.Serializable;

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

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public String getName() {
        return name;
    }
}
