package player.project.com.musicplayer.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;
/**
 * Created by Cuong on 1/26/2018.
 */

public class Player extends MediaPlayer implements AudioManager.OnAudioFocusChangeListener {
    private static Player sPlayer = new Player();
    private static Context context;

    private Player() {
        super();

    }

    public static Player getInstance(Context c) {
        context = c;
        return sPlayer;
    }

    public void play() {
        try {
            this.prepare();
        } catch (IOException e) {
            Toast.makeText(context, "This is my Toast message!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        super.setOnCompletionListener(listener);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
