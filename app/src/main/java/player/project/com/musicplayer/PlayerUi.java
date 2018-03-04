package player.project.com.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class PlayerUi extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    ImageButton nextButton;
    ImageButton playButton;
    ImageButton prevButton;
    SeekBar progressBar;
    MediaPlayer mediaPlayer;
    TextView tvDuration;
    TextView currentTime;
    TextView songTitle;
    String songName;
    String duration;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        songName = intent.getStringExtra("name");
        duration = intent.getStringExtra("duration");
        String path = intent.getStringExtra("path");
        setContentView(R.layout.activity_player_ui);
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
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        // Changing button image to play button
                        playButton.setImageResource(R.drawable.ic_play);
                    }
                } else {
                    // Resume song
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        // Changing button image to pause button
                        playButton.setImageResource(R.drawable.ic_pause);
                    }
                }

            }
        });
        prevButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        play(path);
    }

    public void play(String path) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        songTitle.setText(songName);
        tvDuration.setText("00:00");
        progressBar.setProgress(0);
        progressBar.setMax(100);
        updateProgressBar();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {

        } else if (v.getId() == R.id.btn_next) {


        } else if (v.getId() == R.id.btn_next) {

        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            tvDuration.setText("" + Utilitys.milisecondToDuration(totalDuration));
            // Displaying time completed playing
            currentTime.setText("" + Utilitys.milisecondToDuration(currentDuration));
            // Updating progress bar
            int progress = (int) (Utilitys.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            progressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = Utilitys.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mediaPlayer.release();
    }

}
