package player.project.com.musicplayer.ultilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LyricsHelper {
    private static final String BASE_API_URL = "http://lyric-api.herokuapp.com/api/find/";
    private String mOutputLyric;
    private Boolean mIsSuccess = false;

    public LyricsHelper(String singerName, String songName) {
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
                mIsSuccess = false;
            } else {
                mOutputLyric = sb.toString();
                mIsSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mIsSuccess = false;
        }
        try {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


    }

    private String makeUrl(String singerName, String songName) {
        String tmp1;
        String tmp2;
        String tmp3;
        tmp1 = singerName.replaceAll(" ", "%20");
        tmp2 = songName.replaceAll(" ", "%20");
        tmp3 = BASE_API_URL + tmp1 + "/" + tmp2;
        return tmp3;
    }

    public boolean isFound() {
        return mIsSuccess;
    }

    public String getLyics() {
        return makeLyric(mOutputLyric);
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