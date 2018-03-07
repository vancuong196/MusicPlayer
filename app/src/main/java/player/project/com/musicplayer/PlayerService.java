package player.project.com.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;

import player.project.com.musicplayer.models.PlayList;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private ArrayList<String> mPlayList;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String actionCode = intent.getAction();
            if (actionCode == Constant.ACTION_PLAY) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        sendBroadcast(new Intent("android.action.update"));
                    } else {
                        mMediaPlayer.start();
                    }
                } else {
                    mPlayList = intent.getStringArrayListExtra(Constant.PLAYLIST_EX);
                    int postion = intent.getIntExtra(Constant.PLAY_POSTION_EX, 0);
                    play(postion);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public PlayerService() {
        super();
    }

    public void pausePlayer() {

    }

    public void play(int postion) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mPlayList.get(postion));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    public void next() {

    }

    public void previous() {

    }

    public void seekTo(long mili) {

    }

    public void setPlayTime(long mili) {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}
