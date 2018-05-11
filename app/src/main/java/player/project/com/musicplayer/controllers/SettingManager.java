package player.project.com.musicplayer.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import player.project.com.musicplayer.ultilities.Constant;

/**
 * Created by Cuong on 2/4/2018.
 */

public class SettingManager {
    private Context context;
    private SharedPreferences settings;
    private static class SettingMangagerHolder {
        static SettingManager instance = new SettingManager();
    }

    private SettingManager() {

    }

    public static SettingManager getInstance(Context context) {
        SettingMangagerHolder.instance.context = context;
        return SettingMangagerHolder.instance;
    }

    public int getsMode() {
        settings = context.getSharedPreferences("playersetting", Context.MODE_MULTI_PROCESS);
        int mode = settings.getInt("shuffle", Constant.SETTING_SHUFFLE_MODE_OFF);
        System.out.println("get smode " + mode);
        return mode;

    }

    public int getrMode() {
        settings = context.getSharedPreferences("playersetting", Context.MODE_MULTI_PROCESS);
        int mode = settings.getInt("repeat", Constant.SETTING_REPEAT_MODE_OFF);
        System.out.println("get rmode " + mode);
        return mode;
    }

    public void setsMode(int mode) {
        settings = context.getSharedPreferences("playersetting", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("shuffle", mode);
        System.out.println("set smode " + mode);
        editor.commit();
    }

    public void setrMode(int mode) {
        settings = context.getSharedPreferences("playersetting", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("repeat", mode);
        System.out.println("set rmode " + mode);
        editor.commit();
    }
}
