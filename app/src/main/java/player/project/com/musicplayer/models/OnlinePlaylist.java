package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 5/7/2018.
 */

public class OnlinePlaylist {
    String tittle;
    String decription;
    String numberOfSong;
    String type;

    public OnlinePlaylist() {
    }

    public String getTittle() {

        return tittle;
    }

    public OnlinePlaylist(String tittle, String decription, String numberOfSong, String type) {
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
        return "OnlinePlaylist{" +
                "tittle='" + tittle + '\'' +
                ", decription='" + decription + '\'' +
                ", numberOfSong='" + numberOfSong + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
