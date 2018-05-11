package player.project.com.musicplayer.ultilities;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.models.Song;

/**
 * Created by Cuong on 5/7/2018.
 */

public class XmlParser {

    public ArrayList<OnlineAlbum> parsePlayList(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String numberOfSong = null;
        String type = null;
        String description = null;
        String imageLink = null;
        boolean isItem = false;
        ArrayList<OnlineAlbum> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

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
                } else if (name.equalsIgnoreCase("image")) {
                    imageLink = result;
                }
                if (title != null && link != null && description != null && numberOfSong != null && type != null && imageLink != null) {
                    System.out.println("debug" + isItem);
                    if (isItem) {
                        OnlineAlbum item = new OnlineAlbum(title, description, numberOfSong, type, link, imageLink);
                        items.add(item);
                    }

                    title = null;
                    link = null;
                    numberOfSong = null;
                    type = null;
                    description = null;
                    isItem = false;
                    imageLink = null;
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
                if (info != null && link != null) {
                    String[] a = new String[2];
                    a[0] = link;
                    a[1] = info;
                    return a;
                }
            }


            return null;

        } finally {
            inputStream.close();
        }
    }

    public ArrayList<Song> parseSongList(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String songName = null;
        String path = null;
        String albumName = null;
        String singerName = null;
        String duration = null;
        String imageLink = null;
        boolean isItem = false;
        ArrayList<Song> items = new ArrayList<>();
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
                        System.out.println("debug" + isItem);
                        if (isItem) {
                            Song item = new Song(songName, singerName, albumName, duration, path);
                            //todo setcoverpath
                            //item.setCoverPath(imageLink);
                            items.add(item);
                        }

                        songName = null;
                        path = null;
                        duration = null;
                        albumName = null;
                        singerName = null;
                        isItem = false;
                        imageLink = null;

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
                    songName = result;
                } else if (name.equalsIgnoreCase("duration")) {
                    duration = result;
                } else if (name.equalsIgnoreCase("artist")) {
                    singerName = result;
                } else if (name.equalsIgnoreCase("album")) {
                    albumName = "fake";

                } else if (name.equalsIgnoreCase("path")) {
                    path = result;

                } else if (name.equalsIgnoreCase("image")) {
                    imageLink = "fake";
                }
                //songName != null && path != null && duration != null && albumName != null && singerName != null&&imageLink!=null

            }


            return items;
        } finally {
            inputStream.close();
        }
    }


}
