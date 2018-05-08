package player.project.com.musicplayer.models;

import java.io.Serializable;

/**
 * Created by Cuong on 5/7/2018.
 */

public class OnlineAlbum implements Serializable {
    String tittle;
    String decription;
    String numberOfSong;
    String type;

    public OnlineAlbum() {
    }

    public String getTittle() {

        return tittle;
    }

    public OnlineAlbum(String tittle, String decription, String numberOfSong, String type) {
        this.tittle = tittle;
        this.decription = decription;
        this.numberOfSong = numberOfSong;
        this.type = type;
    }

    public void setTittle(String tittle) {

        this.tittle = tittle;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getNumberOfSong() {
        return numberOfSong;
    }

    public void setNumberOfSong(String numberOfSong) {
        this.numberOfSong = numberOfSong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
