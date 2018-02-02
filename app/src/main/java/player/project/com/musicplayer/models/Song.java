package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 2/1/2018.
 */

public class Song {
    private String songName;
    private String path;
    private int playListID;

    public Song(String songName, String path, int playListID) {
        this.songName = songName;
        this.path = path;
        this.playListID = playListID;
    }

    public String getSongName() {
        return songName;
    }

    public String getPath() {
        return path;
    }

    public int getPlayListID() {
        return playListID;
    }
}
