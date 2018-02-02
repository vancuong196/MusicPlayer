package player.project.com.musicplayer.models;

/**
 * Created by Cuong on 2/1/2018.
 */

public class PlayList {
    private int id;
    private String name;

    public PlayList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
