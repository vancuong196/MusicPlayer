package player.project.com.musicplayer.Controllers;

import android.content.Context;
import android.content.SharedPreferences;

import player.project.com.musicplayer.Ultilities.Constant;

/**
 * Created by Cuong on 2/4/2018.
 */

public class SettingManager {
    int sMode;
    int rMode;
    Context context;
    private static class SettingMangagerHolder {
        static SettingManager instance = new SettingManager();
    }

    public static SettingManager getInstance(Context context) {
        SettingMangagerHolder.instance.context = context;
        return SettingMangagerHolder.instance;
    }

    public int getsMode() {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);

        return settings.getInt("shuffle", Constant.SETTING_SHUFFLE_MODE_OFF);

    }

    public int getrMode() {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);
        return settings.getInt("repeat", Constant.SETTING_REPEAT_MODE_OFF);
    }

    public void setsMode(int mode) {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("shuffle", mode);
        editor.commit();
    }

    public void setrMode(int mode) {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("repeat", mode);
        editor.commit();
    }
}
