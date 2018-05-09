package player.project.com.musicplayer.ultilities;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;

/**
 * Created by Cuong on 5/9/2018.
 */

public class StartServiceHelper {
    public static void send(Context context, ArrayList<Song> songs, int postion) {
        Intent myIntent = new Intent(context, PlayerService.class);
        myIntent.setAction(Constant.ACTION_SONG_CHANGE);

        int shuffleMode = SettingManager.getInstance(context).getsMode();
        if (shuffleMode == Constant.SETTING_SHUFFLE_MODE_ON) {
            ArrayList<Song> toSendSongList = Ultility.randomSongListMaker(songs, postion);
            myIntent.putExtra(Constant.SONG_LIST_EX, toSendSongList);
            myIntent.putExtra(Constant.SONG_POSTON_EX, 0);
        } else {
            myIntent.putExtra(Constant.SONG_LIST_EX, songs);
            myIntent.putExtra(Constant.SONG_POSTON_EX, postion);
        }

        //((MainActivity) context).setMiniWidgetVisible(true);
        //((MainActivity) context).pendSongListInit(songs);
        context.startService(myIntent);
    }

}
