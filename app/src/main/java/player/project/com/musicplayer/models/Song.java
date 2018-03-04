package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 2/1/2018.
 */

public class Song {
    private String songName;
    private String singerName;
    private String duration;
    private String path;

    public Song(String songName, String singerName, String duration, String path) {
        this.songName = songName;
        this.singerName = singerName;
        this.duration = duration;
        this.path = path;
    }

    public String getSongName() {
        return songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }
}
