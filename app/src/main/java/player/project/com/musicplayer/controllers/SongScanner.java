package player.project.com.musicplayer.controllers;

/**
 * Created by Cuong on 2/4/2018.
 */

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.models.Song;

public class SongScanner {


    // SDCard Path
    final String MEDIA_PATH_IN = Environment.getExternalStorageDirectory().getPath();
    File[] list;
    private ArrayList<Song> songsList = new ArrayList<Song>();
    Context mContext;
    SongController songController;
    // Constructor

    public SongScanner(@NonNull Context context) {
        mContext = context;
        list = mContext.getExternalFilesDirs(null);
        songController = new SongController(mContext);
    }

    public void scan() {
        for (int i = 0; i < list.length; i++) {
            System.out.println("Path----------------" + list[i].getParentFile().getParentFile().getParentFile().getParentFile().getPath());
            scanPath(list[i].getParentFile().getParentFile().getParentFile().getParentFile().getPath());
        }
    }
    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    private void scanPath(@NonNull String path) {
        System.out.println("-----Sanning directory " + path + "--------");
        File home = new File(path);
        if (home.exists()) {

            File[] files = home.listFiles();

            if (files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory() && !file.isHidden() && file.canRead()) {
                        scanPath(file.getPath());
                    } else if (file.isFile() && !file.isHidden() && file.canRead())

                    {
                        String tmp = file.getName();
                        if (tmp.toLowerCase().endsWith(".mp3") || tmp.toLowerCase().endsWith(".mp4")) {
                            System.out.println(file.getPath().toString());
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            try {


                                mmr.setDataSource(file.getPath());

                                String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                String singerName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                                String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                                //coverart is an Imageview object
                                boolean isHaveCoverImage = true;

                                if (mmr.getEmbeddedPicture() == null) {
                                    isHaveCoverImage = false;
                                }
                                if (duration == null) {
                                    continue;
                                }

                                if (songName == null) {
                                    songName = file.getName().substring(0, file.getName().length() - ".mp3".length());
                                }
                                if (singerName == null) {
                                    singerName = "unknown";
                                }
                                if (album == null) {
                                    album = "unknown";
                                }
                                Song song = new Song(songName, singerName, album, duration, file.getPath());
                                song.setHaveCoverImage(isHaveCoverImage);
                                // Adding each song to SongList
                                songController.addSong(song);
                            } catch (Exception e) {
                                System.out.println("Loi");
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * Class to filter files which are having .mp3,mp4 extension
     */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3") || name.endsWith(".mp4") || name.endsWith(".MP4"));
        }

    }
}
