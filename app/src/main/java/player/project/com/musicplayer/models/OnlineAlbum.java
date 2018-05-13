package player.project.com.musicplayer.models;

import java.io.Serializable;

public class OnlineAlbum implements Serializable {
    private String tittle;
    private String decription;
    private String numberOfSong;
    private String type;
    private String link;
    private String imageLink;

    public String getTittle() {

        return tittle;
    }

    public OnlineAlbum(String tittle, String decription, String numberOfSong, String type, String link, String imageLink) {
        this.tittle = tittle;
        this.decription = decription;
        this.numberOfSong = numberOfSong;
        this.type = type;
        this.link = link;
        this.imageLink = imageLink;
    }

    public String getLink() {

        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getDecription() {
        return decription;
    }

    public String getNumberOfSong() {
        return numberOfSong;
    }
    @Override
    public String toString() {
        return "OnlineAlbum{" +
                "tittle='" + tittle + '\'' +
                ", decription='" + decription + '\'' +
                ", numberOfSong='" + numberOfSong + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
