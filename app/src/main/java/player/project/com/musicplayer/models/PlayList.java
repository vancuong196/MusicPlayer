package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 2/1/2018.
 */

public class PlayList {

    private String name;
    private int numberOfSongs;

    public PlayList(String name, int numberOfSongs) {
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
