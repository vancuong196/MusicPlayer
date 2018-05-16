package player.project.com.musicplayer.ultilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import player.project.com.musicplayer.models.Song;

public class Ultility {
    public static String milisecondToDuration(String str) {
        long milliseconds = Long.valueOf(str);
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static String milisecondToDuration(long mili) {
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (mili / (1000 * 60 * 60));
        int minutes = (int) (mili % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((mili % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }


    public static int progressToTimer(int progress, long totalDuration) {
        int currentDuration;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static Bitmap getCoverImageofSong(String path) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            byte[] b = mmr.getEmbeddedPicture();
            if (b == null) {
                return null;
            } else return BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e) {
                return null;
            }
        }


    public static ArrayList<Song> randomSongListMaker(ArrayList<Song> songs, int postion) {
        ArrayList<Song> cSongs = (ArrayList<Song>) songs.clone();
        if (postion != -1) {
            Song currentItem = cSongs.get(postion);
            cSongs.remove(postion);
            Collections.shuffle(cSongs);
            cSongs.add(0, currentItem);
            return cSongs;
        } else {
            Collections.shuffle(cSongs);
            return cSongs;
        }

    }

    public static void sortSongList(ArrayList<Song> songs) {
        // Sorting
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {

                return song1.getSongName().toLowerCase().compareTo(song2.getSongName().toLowerCase());
            }
        });
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }
}
