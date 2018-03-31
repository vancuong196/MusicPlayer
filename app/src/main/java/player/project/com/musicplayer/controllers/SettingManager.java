package player.project.com.musicplayer.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import player.project.com.musicplayer.Constant;

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

    public String getsMode() {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);

        return settings.getString("shuffle", Constant.SETTING_SMODE_OFF).toString();

    }

    public String getrMode() {
        SharedPreferences settings = context.getSharedPreferences("PlayMode", 0);
        return settings.getString("repeat", Constant.SETTING_RMODE_ONE).toString();
    }

    public void setsMode(String mode) {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("shuffle", mode);
        editor.commit();
    }

    public void setrMode(String mode) {
        SharedPreferences settings = context.getSharedPreferences("PlayerMode", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("repeat", mode);
        editor.commit();
    }
}
