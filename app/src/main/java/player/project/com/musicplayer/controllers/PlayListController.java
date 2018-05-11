package player.project.com.musicplayer.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Playlist;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.Ultility;

/**
 * Created by Nam on 5/9/2018.
 */

public class PlayListController extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "application_db";
    private static final String TABLE_NAME = "playlist";
    private static final String ID = "id";
    private static final String PLAYLISTNAME = "name";
    private static final String SONGPATH = "songPath";
    private Context mContext;

    public PlayListController(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                PLAYLISTNAME + " TEXT, " +
                SONGPATH + " TEXT )";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public ArrayList<Playlist> getPlayList() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        String selectQuery = "SELECT " + PLAYLISTNAME + ",count(*) as numberOfSongs FROM " + TABLE_NAME + " group by " + PLAYLISTNAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Playlist playlist = new Playlist();
                playlist.setName(cursor.getString(0));
                playlist.setNumberOfSongs(cursor.getInt(1));
                playlists.add(playlist);
            } while (cursor.moveToNext());
        }
        db.close();
        return playlists;
    }

    public ArrayList<Song> getAllSongBeLongPlayList(String playlistName) {
        ArrayList<Song> songs = new ArrayList<>();
        Song song;
        String selectQuery = "SELECT *FROM " + TABLE_NAME + " where " + PLAYLISTNAME + "='" + playlistName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                SongController songController = new SongController(this.mContext);
                song = songController.getSongsByPath(cursor.getString(2));
                if (song != null) {
                    songs.add(song);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        Ultility.sortSongList(songs);
        return songs;
    }

    public void addPlaylist(String playlistName, String songPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYLISTNAME, playlistName);
        values.put(SONGPATH, songPath);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deletePlayList(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PLAYLISTNAME + "=?", new String[]{String.valueOf(playlist.getName())});
        db.close();
    }

    public void deleteSongOfPlayList() {

    }
}




