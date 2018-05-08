package player.project.com.musicplayer.ultilities;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import player.project.com.musicplayer.models.OnlinePlaylist;

/**
 * Created by Cuong on 5/7/2018.
 */

public class XmlParser {

    public ArrayList<OnlinePlaylist> parsePlayList(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String numberOfSong = null;
        String type = null;
        String description = null;
        boolean isItem = false;
        ArrayList<OnlinePlaylist> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    System.out.println(result);
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("noi")) {
                    numberOfSong = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("decription")) {
                    description = result;

                } else if (name.equalsIgnoreCase("type")) {
                    type = result;
                }

                if (title != null && link != null && description != null && numberOfSong != null && type != null) {
                    System.out.println("debug" + isItem);
                    if (isItem) {
                        OnlinePlaylist item = new OnlinePlaylist(title, description, numberOfSong, type);
                        items.add(item);
                    }

                    title = null;
                    link = null;
                    numberOfSong = null;
                    type = null;
                    description = null;
                    isItem = false;
                }
            }


            return items;
        } finally {
            inputStream.close();
        }
    }

    public String[] parseArtist(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String info = null;
        String link = null;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null) {
                    continue;
                }
                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("image")) {
                    link = result;
                } else if (name.equalsIgnoreCase("summary")) {
                    info = result;
                }

            }

            if (info == null || link == null) {
                return null;
            } else {
                String[] a = new String[2];
                a[0] = link;
                a[1] = info;
                return a;
            }

        } finally {
            inputStream.close();
        }
    }

}
