package player.project.com.musicplayer.controllers;

/**
 * Created by Cuong on 2/4/2018.
 */

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import player.project.com.musicplayer.Utilitys;
import player.project.com.musicplayer.models.Song;

public class SongScaner {


    // SDCard Path
    final String MEDIA_PATH_EX = new String("/sdcard/");
    final String MEDIA_PATH_IN = Environment.getExternalStorageDirectory().getPath() + "/Music/";
    private ArrayList<Song> songsList = new ArrayList<Song>();

    // Constructor
    public SongScaner() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public void scan() {
        System.out.println("--------------------------------------------------------------------------------------");
        File home = new File(MEDIA_PATH_EX);

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {

                System.out.println(file.getPath().toString());
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(file.getPath());

                String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                String singerName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);


                Song song = new Song(file.getName().substring(0, (file.getName().length() - 4)), singerName, duration, file.getPath());
                // Adding each song to SongList
                songsList.add(song);
            }
        }
        home = new File(MEDIA_PATH_IN);
        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                System.out.println(file.getPath().toString());
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(file.getPath());

                String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                String singerName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);


                Song song = new Song(file.getName().substring(0, (file.getName().length() - 4)), singerName, Utilitys.milisecondToDuration(duration), file.getPath());
                // Adding each song to SongList
                songsList.add(song);
            }
        }

        // return songs list array
    }

    public ArrayList<Song> getSongsList() {
        if (songsList.isEmpty()) scan();
        return songsList;

    }

    public void store() {


    }

    /**
     * Class to filter files which are having .mp3 extension
     */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3") || name.endsWith(".mp4") || name.endsWith(".MP4"));
        }
    }
}