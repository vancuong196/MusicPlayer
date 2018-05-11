package player.project.com.musicplayer.ultilities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;

/**
 * Created by Cuong on 5/11/2018.
 */

public class StartServiceHelper {
    public static void sendShuffleAllCommand(Context context, ArrayList<Song> data) {
        SettingManager.getInstance(context).setsMode(Constant.SETTING_SHUFFLE_MODE_ON);
        ((MainActivity) context).setBtnShuffle();
        Random r = new Random();
        int postion;
        if (data.size() == 1) {
            postion = 0;
        } else if (data.size() > 1) {
            postion = r.nextInt(data.size() - 1);
        } else {
            return;
        }
        Intent myIntent = new Intent(context, PlayerService.class);
        myIntent.setAction(Constant.ACTION_SONG_CHANGE);
        myIntent.putExtra(Constant.SONG_LIST_EX, data);
        myIntent.putExtra(Constant.SONG_POSTON_EX, postion);
        context.startService(myIntent);
        Toast.makeText(context, "Shuffle all", Toast.LENGTH_SHORT).show();
    }
}
