package player.project.com.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.Ultility;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {
    Notification mNotification;
    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mPlayList;
    private SettingManager mSettingManager;
    private Handler mHandler = new Handler();
    private int mCurrentPostion;
    int mTimerTick;
    boolean isShufflePlayList = false;
    boolean isTimerSet = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String actionCode = intent.getAction();
            //request update mTimerTick
            if (Objects.equals(actionCode, Constant.ACTION_UPDATE_TIMER)) {
                isTimerSet = true;
                mTimerTick = intent.getIntExtra(Constant.TIMER_EX, 0) * 60;
                mHandler.postDelayed(mTimerTask, 1000);
            }
            // request disable mTimerTick
            else if (Objects.equals(actionCode, Constant.ACTION_DISABLE_TIMER)) {

                try {
                    mHandler.removeCallbacks(mTimerTask);
                } catch (Exception e) {
                    System.out.println("Failed disable mTimerTick");
                }
                isTimerSet = false;
                Intent myIntent = new Intent(Constant.BROADCAST_TIMER);
                myIntent.putExtra(Constant.TIMER_STATE_EX, isTimerSet);
                sendBroadcast(myIntent);
            }
            // request change media player state
            if (Objects.equals(actionCode, Constant.ACTION_PLAY)) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        buildNotification(mPlayList.get(mCurrentPostion), false);
                        broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PAUSED);
                        stopBroadcastCurrentPlayTime();
                    } else {
                        mMediaPlayer.start();
                        buildNotification(mPlayList.get(mCurrentPostion), true);
                        broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PLAYING);
                        broadcastCurrentPlayTime();
                    }

                }
            }
            // request move to a postion in current playlist
            else if (Objects.equals(actionCode, Constant.ACTION_CHANGE_SONG_POSTION)) {
                int postion = intent.getIntExtra(Constant.SONG_POSTON_EX, 0);
                play(postion);

            }
            // Request change current playlist and play song at postion given.
            else if (Objects.equals(actionCode, Constant.ACTION_CHANGE_PLAYLIST)) {
                ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra(Constant.SONG_LIST_EX);
                int postion = intent.getIntExtra(Constant.SONG_POSTON_EX, 0);
                int shuffleMode = mSettingManager.getsMode();
                if (shuffleMode == Constant.SETTING_SHUFFLE_MODE_ON) {
                    mPlayList = Ultility.randomSongListMaker(songs, postion);
                    broadcastPlayListChanged(0);
                    play(0);
                    isShufflePlayList = true;
                } else {
                    mPlayList = songs;
                    broadcastPlayListChanged(postion);
                    play(postion);
                    isShufflePlayList = false;
                }

            } else if (Objects.equals(actionCode, Constant.REQUEST_UPDATE_SHUFFLE_MODE)) {
                int shuffleMode = mSettingManager.getsMode();
                if (shuffleMode == Constant.SETTING_SHUFFLE_MODE_ON) {
                    if (!isShufflePlayList) {
                        mPlayList = Ultility.randomSongListMaker(mPlayList, mCurrentPostion);
                        isShufflePlayList = true;
                        mCurrentPostion = 0;
                    }
                } else {
                    if (isShufflePlayList) {
                        Song song = mPlayList.get(mCurrentPostion);
                        Ultility.sortSongList(mPlayList);
                        mCurrentPostion = mPlayList.indexOf(song);
                        isShufflePlayList = false;
                    }
                }
                broadcastPlayListChanged(mCurrentPostion);
            }
            // Request play next song
            else if (Objects.equals(actionCode, Constant.ACTION_NEXT)) {
                next();
            }
            // Request play previous song
            else if (Objects.equals(actionCode, Constant.ACTION_PREV)) {
                previous();
            }
            // Request seek to a postion of current playing song
            else if (Objects.equals(actionCode, Constant.ACTION_SEEK)) {
                int milis = intent.getIntExtra(Constant.SEEK_TO_POSTION_EX, 0);
                seekTo(milis);
            }
            // Request information to update UI.
            if (Objects.equals(actionCode, Constant.REQUEST_UPDATE_UI)) {
                broadcastPlayListChanged(mCurrentPostion);
                if (mPlayList != null) {
                    broadcastSongChange(mCurrentPostion);

                }
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PLAYING);

                    } else {

                        broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PAUSED);

                    }

                }
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

        mSettingManager = SettingManager.getInstance(getApplicationContext());
        super.onCreate();
    }

    public PlayerService() {
        super();
    }


    public void play(int postion) {
        stopBroadcastCurrentPlayTime();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
        } else {
            mMediaPlayer.reset();
            mMediaPlayer.setOnCompletionListener(this);
        }

        try {
            System.out.println("preparing to play----------------");
            System.out.println(mPlayList.get(postion).getPath());
            mMediaPlayer.setDataSource(mPlayList.get(postion).getPath());
        } catch (IOException e) {
            broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PAUSED);
            broadcastError();
            // Todo broadcast to activity that service cant not play this song.
            mCurrentPostion = postion;
            mMediaPlayer.reset();
            next();
            e.printStackTrace();
            return;
        }
        try {
            mMediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
            mCurrentPostion = postion;
            broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PAUSED);
            broadcastError();
            // Todo broadcast to activity that service cant not play this song.

            mMediaPlayer.reset();
            next();
            e.printStackTrace();
            return;
            // Todo broadcast to activity that service cant not play this song.
        }
        mMediaPlayer.start();
        broadcastSongChange(postion);
        broadcastCurrentPlayTime();
        mCurrentPostion = postion;
        buildNotification(mPlayList.get(postion), true);
    }

    public void broadcastError() {
        Intent myIntent = new Intent(Constant.BROADCAST_ERROR);
        sendBroadcast(myIntent);
    }
    public void broadcastSongChange(int postion) {
        Intent myIntent = new Intent(Constant.BROADCAST_SONG_CHANGED);
        myIntent.putExtra(Constant.SONG_POSTON_EX, postion);
        myIntent.putExtra(Constant.SONG_EX, mPlayList.get(postion));
        sendBroadcast(myIntent);
    }

    public void broadcastPlayListChanged(int currentPostion) {
        if (mPlayList != null) {
            Intent myIntent = new Intent(Constant.BROADCAST_PLAYLIST_CHANGED);
            myIntent.putExtra(Constant.SONG_LIST_EX, mPlayList);
            myIntent.putExtra(Constant.SONG_POSTON_EX, currentPostion);
            sendBroadcast(myIntent);
        }
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


    public void next() {

        if (mCurrentPostion < mPlayList.size() - 1) {
            play(mCurrentPostion + 1);
        } else play(0);
    }


    public void previous() {
        if (mCurrentPostion > 0) {
            play(mCurrentPostion - 1);
        } else play(mPlayList.size() - 1);
    }

    public void seekTo(int mili) {

        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mili);
        }
    }
    private Runnable mTimerTask = new Runnable() {
        @Override
        public void run() {

            if (mTimerTick == 0 && mMediaPlayer != null) {
                isTimerSet = false;
                mMediaPlayer.pause();
                broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PAUSED);
                Intent myIntent = new Intent(Constant.BROADCAST_TIMER);
                myIntent.putExtra(Constant.TIMER_STATE_EX, isTimerSet);
                sendBroadcast(myIntent);
                mHandler.removeCallbacks(mTimerTask);

            }
            if (mTimerTick == 0 && mMediaPlayer == null) {
                isTimerSet = false;
                broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_NULL);
                mHandler.removeCallbacks(mTimerTask);
            }
            if (mTimerTick > 0) {
                mTimerTick = mTimerTick - 1;
                Intent myIntent = new Intent(Constant.BROADCAST_TIMER);
                myIntent.putExtra(Constant.TIMER_STATE_EX, isTimerSet);
                if (isTimerSet) {
                    myIntent.putExtra(Constant.TIMER_EX, mTimerTick);
                }
                sendBroadcast(myIntent);
                mHandler.postDelayed(mTimerTask, 1000);
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        int rmode = mSettingManager.getrMode();
        if (rmode == Constant.SETTING_REPEAT_MODE_ONE) {
            play(mCurrentPostion);
            return;
        }
        if (rmode == Constant.SETTING_REPEAT_MODE_OFF) {
            broadcastMediaStateChange(Constant.MEDIA_PLAYER_STATE_PAUSED);
            return;
        }
        if (rmode == Constant.SETTING_REPEAT_MODE_ALL) {
            next();
        }

    }


    public void buildNotification(Song song, boolean isOnGoing) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constant.REQUEST_UPDATE_UI);
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
        Bitmap icon = Ultility.getCoverImageofSong(song.getPath());
        if (icon == null) {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.artist_parallax);
        }
        icon = Bitmap.createScaledBitmap(icon, 128, 128, false);
        if (isOnGoing) {

            mNotification = new NotificationCompat.Builder(this)
                    .setContentTitle(song.getSongName())
                    .setTicker(song.getSongName())
                    .setContentText(song.getSingerName())
                    .setSmallIcon(R.drawable.ic_music)
                    .setLargeIcon(icon)
                    .setColorized(true)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.previous,
                            "", ppreviousIntent)
                    .addAction(R.drawable.pause, "",
                            pplayIntent)
                    .addAction(R.drawable.next, "",
                            pnextIntent).build();

            startForeground(12, mNotification);
        } else {
            stopForeground(false);
            mNotification = new NotificationCompat.Builder(this)
                    .setContentTitle(song.getSongName())
                    .setTicker(song.getSongName())
                    .setContentText(song.getSingerName())
                    .setSmallIcon(R.drawable.ic_music)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setColorized(true)
                    .setAutoCancel(true)
                    .setLargeIcon(icon)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .addAction(R.drawable.previous,
                            "", ppreviousIntent)
                    .addAction(R.drawable.play, "",
                            pplayIntent)
                    .addAction(R.drawable.next, "",
                            pnextIntent).build();
            mNotification.flags = NotificationCompat.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            notificationManager.notify(12, mNotification);

        }

    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        PlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
