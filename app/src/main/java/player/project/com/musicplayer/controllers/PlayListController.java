package player.project.com.musicplayer.controllers;

import java.util.ArrayList;

import player.project.com.musicplayer.models.PlayList;
import player.project.com.musicplayer.models.Song;

/**
 * Created by Cuong on 2/1/2018.
 */

class PlayListController {
    static private ArrayList<Song> songList;
    private static final PlayListController ourInstance = new PlayListController();

    static PlayListController getInstance() {
        return ourInstance;
    }

    private PlayListController() {
    }

    public ArrayList<Song> getSongList(PlayList playList) {
        if (songList.isEmpty()) load();
        return songList;
    }

    public void load() {

    }
}
