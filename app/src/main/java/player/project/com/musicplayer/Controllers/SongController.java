package player.project.com.musicplayer.Controllers;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Song;


/**
 * Created by USER on 19/10/2017.
 */

public class SongController extends SQLiteOpenHelper {


    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "music_application_db";
    private static final String TABLE_NAME = "songs";
    private static final String ID = "Song_Id";
    private static final String NAME = "Song_name";
    private static final String ARTIST = "artist";
    private static final String DURATION = "duration";
    private static final String ALBUM = "album";
    private static final String PATH = "path";
    private static final String Lyric = "lyric";
    private Context mContext;

    /*
        public ArrayList<Song> getSongListFromMediaStore() {
            ContentResolver contentResolver = mContext.getContentResolver();
            Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ArrayList<Song> songs = new ArrayList<>();
            Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

            if (songCursor != null && songCursor.moveToFirst()) {
                int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                do {
                    long currentId = songCursor.getLong(songId);
                    String currentTitle = songCursor.getString(songTitle);
                    String currentAtist = songCursor.getString(songArtist);
                    String currentAlbum = songCursor.getString(songAlbum);
                    String currentPath = songCursor.getString(songArtist);
                    // String duration = UltilisongCursor.getLong(songDuration);
                    //  songs.add(new Song((int)currentId,currentTitle,currentAtist,currentAlbum,);
                } while (songCursor.moveToNext());
            }
        }
    */
    public SongController(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String script = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                ARTIST + " TEXT, " +
                ALBUM + " TEXT, " +
                DURATION + " TEXT, " +
                PATH + " TEXT)";
        db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public int addSong(Song song) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, song.getSongName());
        values.put(ARTIST, song.getArtist());
        values.put(ALBUM, song.getAlbum());
        values.put(PATH, song.getPath());
        values.put(DURATION, song.getDuration());
        db.insert(TABLE_NAME, null, values);
        db = this.getReadableDatabase();
        String statement = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(statement, null);
        if (cursor != null)
            cursor.moveToFirst();
        db.close();
        return Integer.parseInt(cursor.getString(0));
    }

    public Song getSong(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, NAME, ARTIST, ALBUM, DURATION, PATH},
                ID + " =?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Song song = new Song(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                , cursor.getString(4), cursor.getString(5));
        db.close();
        return song;
    }

    public ArrayList<Song> getAllSongs() {

        ArrayList<Song> songs = new ArrayList<Song>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setSongId(Integer.parseInt(cursor.getString(0)));
                song.setSongName(cursor.getString(1));
                song.setArtist(cursor.getString(2));
                song.setAlbum(cursor.getString(3));
                song.setDuration(cursor.getString(4));
                song.setPath(cursor.getString(5));
                songs.add(song);
            } while (cursor.moveToNext());
        }
        db.close();
        return songs;
    }

    public int count() {

        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }

    public int updateSong(Song song) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, song.getSongName());
        values.put(ARTIST, song.getArtist());
        values.put(ALBUM, song.getAlbum());
        values.put(DURATION, song.getDuration());
        values.put(PATH, song.getPath());


        int id = db.update(TABLE_NAME, values, ID + " =?", new String[]{String.valueOf(song.getSongId())});
        db.close();
        return id;
    }

    public void deleteSong(Song song) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " =?", new String[]{String.valueOf(song.getSongId())});
        db.close();
    }

    public void deleteAllSong() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


}