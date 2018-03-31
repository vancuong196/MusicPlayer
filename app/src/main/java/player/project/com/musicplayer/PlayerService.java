package player.project.com.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.player.Player;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {
    Notification notification;
    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mPlayList;
    private SettingManager mSettingManager;
    private Handler mHandler = new Handler();
    private int currentPostion;
    private ArrayList<Integer> playedList;
    int timer;
    boolean isTimerSet = false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String actionCode = intent.getAction();
            if (actionCode == Constant.ACTION_UPDATE_TIMER) {
                timer = intent.getIntExtra(Constant.TIMER_EX, 0);
                mHandler.postDelayed(mTimerTask, 60 * timer * 1000);
            }
            if (actionCode == Constant.ACTION_PLAY) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        buildNotification(mPlayList.get(currentPostion), false);
                        broadcastMediaStateChange(0);
                        stopBroadcastCurrentPlayTime();
                    } else {
                        mMediaPlayer.start();
                        buildNotification(mPlayList.get(currentPostion), true);
                        broadcastMediaStateChange(1);
                        broadcastCurrentPlayTime();
                    }

                }
            }
            if (actionCode == Constant.ACTION_CHANGE_POSTION) {
                int postion = intent.getIntExtra(Constant.SONG_POSTON_EX, 0);
                play(postion);
            } else if (actionCode == Constant.ACTION_SONG_CHANGE) {
                playedList = new ArrayList<>();
                mPlayList = (ArrayList<Song>) intent.getSerializableExtra(Constant.SONG_LIST_EX);
                int postion = intent.getIntExtra(Constant.SONG_POSTON_EX, 0);
                play(postion);
            } else if (actionCode == Constant.ACTION_NEXT) {
                next();
            } else if (actionCode == Constant.ACTION_PREV) {
                previous();
            } else if (actionCode.equals(Constant.ACTION_PLAY_YOUTUBE)) {

                WebView webview = new WebView(this);
                final WebSettings settings = webview.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setUseWideViewPort(true);
                webview.setWebChromeClient(new WebChromeClient());
                webview.setPadding(0, 0, 0, 0);
                String html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" src=\"http://www.youtube.com/embed/K0-ucWKiTps?autoplay=1"
                        + "?fs=0\" frameborder=\"0\">\n"
                        + "</iframe>";
                webview.loadDataWithBaseURL("http://www.youtube.com", html, "text/html", "UTF-8", "");


            } else if (actionCode.equals(Constant.ACTION_SEEK)) {
                int milis = intent.getIntExtra(Constant.SEEK_TO_POSTION_EX, 0);
                seekTo(milis);
            }
            if (actionCode.equals(Constant.ACTION_UPDATE_UI_REQUEST)) {
                broadcastSongChange(currentPostion);
            }

        }
        return super.onStartCommand(intent, flags, startId);
        //return Service.START_STICKY;

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        playedList = new ArrayList<>();
        mSettingManager = SettingManager.getInstance(this);

        super.onCreate();
    }

    public PlayerService() {
        super();
    }

    public void pausePlayer() {

    }

    public void play(int postion) {
        stopBroadcastCurrentPlayTime();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
        }
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mPlayList.get(postion).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        playedList.add(postion);
        broadcastSongChange(postion);
        broadcastCurrentPlayTime();
        currentPostion = postion;
        buildNotification(mPlayList.get(postion), true);
    }

    public void broadcastSongChange(int postion) {
        Intent myIntent = new Intent(Constant.BROADCAST_SONG_CHANGED);
        myIntent.putExtra(Constant.SONG_EX, mPlayList.get(postion));
        sendBroadcast(myIntent);
    }
    public void next() {
        if (SettingManager.getInstance(this).getsMode() == Constant.SETTING_SMODE_OFF) {
            if (currentPostion < mPlayList.size() - 1) {
                play(currentPostion + 1);
            } else play(0);
        } else {
            int postion = new Random().nextInt(mPlayList.size());
            play(postion);
        }
    }

    public void previous() {
        if (currentPostion > 0) {
            play(currentPostion - 1);
        } else play(mPlayList.size() - 1);
    }

    public void seekTo(int mili) {
        stopBroadcastCurrentPlayTime();
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mili);
        }
        broadcastCurrentPlayTime();
    }

    public void broadcastMediaStateChange(int state) {
        Intent myIntent = new Intent(Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED);
        myIntent.putExtra(Constant.MEDIA_STATE_EX, state);
        sendBroadcast(myIntent);
    }

    public void broadcastCurrentPlayTime() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public void stopBroadcastCurrentPlayTime() {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    private Runnable mTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();
            Intent myIntent = new Intent(Constant.BROADCAST_CURRENT_PLAY_TIME);
            myIntent.putExtra(Constant.DURATION_EX, totalDuration);
            myIntent.putExtra(Constant.CURRENT_EX, currentDuration);
            sendBroadcast(myIntent);
            mHandler.postDelayed(this, 100);
        }
    };
    public void setPlayTime(long mili) {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("Complete");
        SettingManager setting = SettingManager.getInstance(this);
        if (setting.getrMode() == Constant.SETTING_RMODE_ONE) {
            play(currentPostion);
        } else {
            next();
        }

    }


    public void buildNotification(Song song, boolean isOnGoing) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constant.ACTION_UPDATE_UI_REQUEST);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, PlayerService.class);
        previousIntent.setAction(Constant.ACTION_PREV);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(Constant.ACTION_PLAY);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayerService.class);
        nextIntent.setAction(Constant.ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
//Todo fix error here
        //     Bitmap icon = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.ic_music);
        if (isOnGoing) {
            notification = new NotificationCompat.Builder(this)
                    .setUsesChronometer(true)
                    .setContentTitle(song.getSongName())
                    .setTicker(song.getSongName())
                    .setContentText(song.getSingerName())
                    .setSmallIcon(R.drawable.ic_music)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_back,
                            "", ppreviousIntent)
                    .addAction(R.drawable.ic_pause, "",
                            pplayIntent)
                    .addAction(R.drawable.ic_forward, "",
                            pnextIntent).build();
            startForeground(12, notification);
        } else {

            notification = new NotificationCompat.Builder(this)
                    .setUsesChronometer(true)
                    .setContentTitle(song.getSongName())
                    .setTicker(song.getSongName())
                    .setContentText(song.getSingerName())
                    .setSmallIcon(R.drawable.ic_music)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .addAction(R.drawable.ic_back,
                            "", ppreviousIntent)
                    .addAction(R.drawable.ic_play, "",
                            pplayIntent)
                    .addAction(R.drawable.ic_forward, "",
                            pnextIntent).build();
            stopForeground(true);

        }

    }
}
