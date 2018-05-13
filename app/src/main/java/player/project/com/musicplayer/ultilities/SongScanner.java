package player.project.com.musicplayer.ultilities;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;

import java.io.File;

import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.models.Song;

public class SongScanner {

    private File[] list;
    private SongController songController;
    // Constructor

    public SongScanner(@NonNull Context context) {
        list = context.getExternalFilesDirs(null);
        songController = new SongController(context);
    }

    public void scan() {
        for (File aList : list) {
            System.out.println("Path----------------" + aList.getParentFile().getParentFile().getParentFile().getParentFile().getPath());
            scanPath(aList.getParentFile().getParentFile().getParentFile().getParentFile().getPath());
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
                            System.out.println(file.getPath());
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            try {


                                mmr.setDataSource(file.getPath());

                                String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                String singerName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                                String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                                //coverart is an Imageview object
                                boolean isHaveCoverImage = false;

                                if (mmr.getEmbeddedPicture() != null) {
                                    isHaveCoverImage = true;
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

}

