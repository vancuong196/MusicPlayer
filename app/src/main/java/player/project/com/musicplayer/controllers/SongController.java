package player.project.com.musicplayer.controllers;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Song;

/**
 * Created by Cuong on 2/1/2018.
 */

class SongController {
    static private ArrayList<Song> songs;
    private static final SongController ourInstance = new SongController();

    static SongController getInstance() {
        return ourInstance;
    }

    private SongController() {
    }

    public ArrayList<Song> getSongs() {
        if (songs.isEmpty()) load();
        return songs;
    }

    public void load() {

    }
}
