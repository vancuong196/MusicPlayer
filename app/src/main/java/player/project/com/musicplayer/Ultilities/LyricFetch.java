package player.project.com.musicplayer.Ultilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Cuong on 4/1/2018.
 */

public class LyricFetch {
    static String baseAdress = "http://lyric-api.herokuapp.com/api/find/";
    String output;
    Boolean success = false;

    public LyricFetch(String singerName, String songName) {
        String urlString = makeUrl(singerName, songName);
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String tmp;
            while ((tmp = in.readLine()) != null) {
                sb.append(tmp);
            }
            if (sb.toString().contains("err\":\"not found\"")) {
                success = false;
            } else {
                output = sb.toString();
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        try {

        } finally {
            urlConnection.disconnect();
        }


    }

    private String makeUrl(String singerName, String songName) {
        String tmp1;
        String tmp2;
        String tmp3;
        tmp1 = singerName.replaceAll(" ", "%20");
        tmp2 = songName.replaceAll(" ", "%20");
        tmp3 = baseAdress + tmp1 + "/" + tmp2;
        return tmp3;
    }

    public boolean isFound() {
        return success;
    }

    public String getLyics() {
        return makeLyric(output);
    }

    private String makeLyric(String str) {
        str = str.replaceAll("\"lyric\":\"", "");
        str = str.replaceAll("\",\"err\":\"none\"", "");
        str = str.replaceAll("\\\\n", "<br>");
        str = str.replace('{', '\n');
        str = str.replace('}', '\n');
        return str;
    }
}