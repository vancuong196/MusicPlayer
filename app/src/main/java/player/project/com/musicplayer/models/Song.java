package player.project.com.musicplayer.models;

import java.io.Serializable;


public class Song implements Serializable {
    private String songName;
    private String duration;
    private String artist;
    private String album;
    private String path;
    private boolean isHaveCoverImage;
    private int songId;

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", duration='" + duration + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", path='" + path + '\'' +
                ", isHaveCoverImage=" + isHaveCoverImage +
                ", songId=" + songId +
                '}';
    }

    public boolean isHaveCoverImage() {
        return isHaveCoverImage;
    }

    public void setHaveCoverImage(boolean isHaveCoverImage) {
        this.isHaveCoverImage = isHaveCoverImage;
    }

    public Song(String songName, String singerName, String album, String duration, String path) {
        this.songName = songName;
        this.artist = singerName;
        this.duration = duration;
        this.album = album;
        this.path = path;
    }

    public Song(int songId, String songName, String singerName, String album, String duration, String path) {
        this.songName = songName;
        this.artist = singerName;
        this.duration = duration;
        this.album = album;
        this.path = path;
        this.songId = songId;
    }

    public Song() {

    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSongId(int songId) {
        this.songId = songId;
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
