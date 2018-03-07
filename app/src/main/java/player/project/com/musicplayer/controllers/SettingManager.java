package player.project.com.musicplayer.controllers;

/**
 * Created by Cuong on 2/4/2018.
 */

public class SettingManager {
    int sMode;
    int rMode;

    private static class SettingMangagerHolder {
        static SettingManager instance = new SettingManager();
    }

    public static SettingManager getInstance() {
        return SettingMangagerHolder.instance;
    }

    public int getsMode() {
        return sMode;
    }

    public int getrMode() {
        return rMode;
    }

    public void setsMode(int sMode) {

        this.sMode = sMode;
    }

    public void setrMode(int rMode) {
        this.rMode = rMode;
    }
}
