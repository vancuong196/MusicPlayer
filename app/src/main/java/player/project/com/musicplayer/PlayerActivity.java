package player.project.com.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.models.Song;

import static android.widget.Toast.LENGTH_SHORT;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    ImageButton nextButton;
    ImageButton playButton;
    ImageButton prevButton;
    SeekBar progressBar;
    MediaPlayer mediaPlayer;
    TextView tvDuration;
    TextView currentTime;
    TextView songTitle;
    long duration;
    ArrayList<Song> songList;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.BROADCAST_CURRENT_PLAY_TIME)) {
                long totalDuration = intent.getLongExtra(Constant.DURATION_EX, 0);
                long currentDuration = intent.getLongExtra(Constant.CURRENT_EX, 0);
                duration = totalDuration;
                // Displaying Total Duration time
                tvDuration.setText("" + Utilitys.milisecondToDuration(totalDuration));
                // Displaying time completed playing
                currentTime.setText("" + Utilitys.milisecondToDuration(currentDuration));
                // Updating progress bar
                int progress = (int) (Utilitys.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                progressBar.setProgress(progress);
            }
            if (intent.getAction().equals(Constant.BROADCAST_SONG_CHANGED)) {
                Song song = (Song) intent.getSerializableExtra(Constant.SONG_EX);
                songTitle.setText(song.getSongName());
            }
            if (intent.getAction().equals(Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED)) {
                int status = intent.getIntExtra(Constant.MEDIA_STATE_EX, 0);
                if (status == 0) {
                    playButton.setImageResource(R.drawable.ic_play);
                }
                if (status == 1) {
                    playButton.setImageResource(R.drawable.ic_pause);
                }
            }
        }
    };
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ui);
        uiInit();
        songList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getAction() == Constant.ACTION_PLAY_SONG_LIST) {
            final ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra(Constant.SONG_LIST_EX);
            songList = songs;
            songListInit();
            Intent myIntent = new Intent(PlayerActivity.this, PlayerService.class);
            myIntent.putExtra(Constant.SONG_LIST_EX, songs);
            myIntent.setAction(Constant.ACTION_SONG_CHANGE);
            startService(myIntent);

        }

        if (intent.getAction() != null && intent.getAction().equals(Constant.ACTION_PLAY)) {
            return;
        }

    }

    public void songListInit() {
        ListView listView = findViewById(R.id.lv_play_ui);
        ArrayList<Song> songs = this.songList;
        SongListViewAdapter mLvAdapter = new SongListViewAdapter(songs, this);
        listView.setAdapter(mLvAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void uiInit() {
        nextButton = findViewById(R.id.btn_next);
        playButton = findViewById(R.id.btn_play);
        prevButton = findViewById(R.id.btn_prev);
        progressBar = findViewById(R.id.progress_bar);
        tvDuration = findViewById(R.id.tv_songduration);
        currentTime = findViewById(R.id.tv_song_crtime);
        songTitle = findViewById(R.id.tv_song_name);
        nextButton.setOnClickListener(this);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for already playing
                Intent myIntent = new Intent(PlayerActivity.this, PlayerService.class);
                myIntent.setAction(Constant.ACTION_PLAY);
                startService(myIntent);

            }
        });
        prevButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        // register recieve
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_SONG_CHANGED);
        filter.addAction(Constant.BROADCAST_CURRENT_PLAY_TIME);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            Intent myIntent = new Intent(PlayerActivity.this, PlayerService.class);
            myIntent.setAction(Constant.ACTION_NEXT);
            startService(myIntent);

        } else if (v.getId() == R.id.btn_prev) {
            Intent myIntent = new Intent(PlayerActivity.this, PlayerService.class);
            myIntent.setAction(Constant.ACTION_PREV);
            IntentFilter filter = new IntentFilter();
            startService(myIntent);
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        unregisterReceiver(receiver);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // unregister
        int currentPosition = Utilitys.progressToTimer(seekBar.getProgress(), duration);
        Intent myIntent = new Intent(this, PlayerService.class);
        myIntent.putExtra(Constant.SEEK_TO_POSTION_EX, currentPosition);
        myIntent.setAction(Constant.ACTION_SEEK);
        startService(myIntent);
        // register receiver again
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_SONG_CHANGED);
        filter.addAction(Constant.BROADCAST_CURRENT_PLAY_TIME);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
