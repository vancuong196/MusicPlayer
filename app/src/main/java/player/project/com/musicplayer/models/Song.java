package player.project.com.musicplayer.models;

import java.io.Serializable;

/**
 * Created by Cuong on 2/1/2018.
 */

public class Song implements Serializable {
    private String songName;
    private String duration;
    private String artist;
    private String album;
    private String path;
    private int songId;
    private static int id = 0;

    public Song(String songName, String singerName, String album, String duration, String path) {
        this.songName = songName;
        this.artist = singerName;
        this.duration = duration;
        this.album = album;
        this.path = path;
        songId = id++;
    }

    public String getSongName() {
        return songName;
    }

    public String getSingerName() {
        return artist;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public static int getId() {
        return id;
    }

    public String getDuration() {
        return duration;

    }

    public String getPath() {
        return path;
    }

    public int getSongId() {
        return songId;
    }
}
