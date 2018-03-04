package player.project.com.musicplayer.controllers;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Song;

/**
 * Created by Cuong on 2/1/2018.
 */

public class SongController {
    static private ArrayList<Song> songs = new ArrayList<>();
    private static final SongController ourInstance = new SongController();

    public static SongController getInstance() {
        return ourInstance;
    }

    private SongController() {
    }

    public ArrayList<Song> getSongs() {
        if (songs.isEmpty()) load();
        return songs;
    }

    public void load() {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
        SongScaner scaner = new SongScaner();
        songs = scaner.getSongsList();
    }

    public void loadFakeData() {
        songs.add(new Song("Let her go", "Passenger", "04:35", "null"));
        songs.add(new Song("When we was young", "Passenger", "05:03", "null"));
        songs.add(new Song("Somebody's Love", "Passenger", "03:30", "null"));
        songs.add(new Song("Home", "Passenger", "03:14", "null"));
        songs.add(new Song("Heart's on fire", "Passenger", "13:03", "null"));
        songs.add(new Song("Boy who cried the wolf", "Passenger", "03:03", "null"));
    }
}
