package player.project.com.musicplayer.ultilities;

public final class Constant {
    public static String BROADCAST_SONG_CHANGED = "com.cuong.musicplayer.broadcast.songchanged";
    public static String BROADCAST_CURRENT_PLAY_TIME = "com.cuong.musicplayer.broadcast.currentplaytime";
    public static String BROADCAST_TIMER = "com.cuong.musicplayer.broadcast.currenttimer";
    public static String BROADCAST_MEDIA_PLAYER_STATE_CHANGED = "com.cuong.musicplayer.broadcast.mediaplayerchange";
    public static String BROADCAST_PLAYLIST_CHANGED = "com.cuong.musicplayer.broadcast.playlistchanged";
    public static String BROADCAST_ERROR = "com.cuong.musicplayer.broadcast.error_when_play_song";

    public static String ACTION_PLAY = "1";
    public static String ACTION_PREV = "3";
    public static String ACTION_NEXT = "4";
    public static String ACTION_SEEK = "5";
    public static String ACTION_DISABLE_TIMER = "disabletimer";
    public static String ACTION_UPDATE_TIMER = "udtm";
    public static String ACTION_CHANGE_PLAYLIST = "actionsongchange";
    public static String ACTION_CHANGE_SONG_POSTION = "changepostion";
    public static String REQUEST_UPDATE_SHUFFLE_MODE = "update_shuffle_mode";
    public static String REQUEST_UPDATE_UI = "updatesongrequest";

    public static String CURRENT_EX = "current_ex";
    public static String TIMER_STATE_EX = "tm_ex";
    public static String TIMER_EX = "timer_ex";
    public static String DURATION_EX = "duration_ex";
    public static String MEDIA_STATE_EX = "mediastateex";
    public static String SONG_POSTON_EX = "songpostiontoplaex";
    public static String SONG_LIST_EX = "songlistex";
    public static String SONG_EX = "songex";
    public static String SEEK_TO_POSTION_EX = "stp";

    public static int MEDIA_PLAYER_STATE_PAUSED = 1;
    public static int MEDIA_PLAYER_STATE_PLAYING = 2;
    public static int MEDIA_PLAYER_STATE_NULL = 3;

    public static int SETTING_SHUFFLE_MODE_ON = 1;
    public static int SETTING_SHUFFLE_MODE_OFF = 2;
    public static int SETTING_REPEAT_MODE_ALL = 3;
    public static int SETTING_REPEAT_MODE_ONE = 4;
    public static int SETTING_REPEAT_MODE_OFF = 5;


}
