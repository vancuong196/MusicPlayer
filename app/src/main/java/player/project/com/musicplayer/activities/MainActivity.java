package player.project.com.musicplayer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.adapters.PendingSongListAdapter;
import player.project.com.musicplayer.dialogs.LyricDialog;
import player.project.com.musicplayer.dialogs.TimerDialog;
import player.project.com.musicplayer.fragments.RootFragment;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.ultilities.Ultility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, PopupMenu.OnMenuItemClickListener {

    ImageView btnNext;
    ImageView btnPlay;
    ImageView btnPrev;

    ImageView btnBackArrow;
    ImageView btnMenuArrow;
    ImageView btnRepeat;
    ImageView btnShuffle;
    ImageView btnWidgetPlay;
    ImageView btnWidgetNext;
    CircleImageView imgWidgetArt;
    TextView tvWidgetSongName;
    TextView tvWidgetSingerName;
    SeekBar mainSeekBar;
    SeekBar widgetSeekBar;
    TextView tvDuration;
    TextView tvTimer;
    TextView tvCurrentTime;
    TextView tvSongName;
    TextView tvSingerName;
    RecyclerView mLvSongs;
    SettingManager mSettingManager;
    PendingSongListAdapter mLvAdapter;
    SlidingUpPanelLayout.PanelSlideListener mLideUpListener;
    ArrayList<Song> currentSonglist;
    long duration;
    Song currentSong;
    private boolean isSeekBarOnSliding = false;
    private SlidingUpPanelLayout mSildingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSettingManager = SettingManager.getInstance(getApplicationContext());
        Fragment rootFragment = new RootFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.root_fragment, rootFragment, "TAG").commit();
        mSildingUpPanelLayout = findViewById(R.id.sliding_layout);
        mSildingUpPanelLayout.getChildAt(1).setOnClickListener(null);
        mLideUpListener = new SlidingUpPanelLayout.PanelSlideListener() {
            float previous = 0;
            boolean isup = false;
            boolean pr = false;
            int state = 0;

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                final LinearLayout m = findViewById(R.id.miniLayout);
                if (slideOffset > previous && (slideOffset - previous > 0.2)) {
                    isup = true;
                    previous = slideOffset;
                    if (!pr) {
                        state = 1;
                        pr = true;
                    } else {
                        state = 0;
                        pr = true;
                    }

                } else if (slideOffset <= previous && (-slideOffset + previous > 0.2)) {
                    isup = false;
                    previous = slideOffset;
                    if (pr) {
                        state = 1;
                        pr = false;
                    } else {
                        pr = false;
                        state = 0;
                    }
                }

                if (isup && state == 1) {
                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            0,                 // fromYDelta
                            -m.getHeight()); // toYDelta
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    m.startAnimation(animate);
                    btnWidgetNext.setVisibility(View.GONE);
                    btnWidgetPlay.setVisibility(View.GONE);
                    btnBackArrow.setVisibility(View.VISIBLE);
                } else if (!isup && state == 1) {
                    m.setVisibility(View.VISIBLE);
                    m.setActivated(true);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            m.getHeight(),  // fromYDelta
                            0);                // toYDelta
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    m.startAnimation(animate);
                    btnBackArrow.setVisibility(View.GONE);
                    btnWidgetNext.setVisibility(View.VISIBLE);
                    btnWidgetPlay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        };

        playerUiInit();
        setMiniWidgetVisible(false);
        Intent myIntent = new Intent(this, PlayerService.class);
        myIntent.setAction(Constant.REQUEST_UPDATE_UI);
        startService(myIntent);
    }

    public void pendSongListInit(ArrayList<Song> data, int postion) {
        currentSonglist = data;
        mLvSongs = findViewById(R.id.lv_pending_songs);
        mLvAdapter = new PendingSongListAdapter(data, this, postion);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        mLvSongs.setLayoutManager(mLayoutManager);
        mLvSongs.setAdapter(mLvAdapter);

    }



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), Constant.BROADCAST_CURRENT_PLAY_TIME)) {

                long totalDuration = intent.getLongExtra(Constant.DURATION_EX, 0);
                long currentDuration = intent.getLongExtra(Constant.CURRENT_EX, 0);
                duration = totalDuration;
                // Displaying Total Duration time
                tvDuration.setText(Ultility.milisecondToDuration(totalDuration));
                // Displaying time completed playing
                tvCurrentTime.setText(Ultility.milisecondToDuration(currentDuration));
                // Updating progress bar
                int progress = (Ultility.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                if (!isSeekBarOnSliding) {
                    mainSeekBar.setProgress(progress);
                }
                widgetSeekBar.setProgress(progress);


            } else if (Constant.BROADCAST_SONG_CHANGED.equals(intent.getAction())) {
                Song song = (Song) intent.getSerializableExtra(Constant.SONG_EX);
                final int postion = intent.getIntExtra(Constant.SONG_POSTON_EX, 0);
                if (mLvAdapter != null) {
                    mLvAdapter.setItemSelected(postion);
                    mLvSongs.post(new Runnable() {
                        @Override
                        public void run() {
                            // Call smooth scroll
                            mLvSongs.smoothScrollToPosition(postion);
                        }
                    });
                }
                currentSong = song;
                tvSongName.setText(song.getSongName());
                tvWidgetSongName.setText(song.getSongName());
                tvSingerName.setText(song.getSingerName());
                tvWidgetSingerName.setText(song.getSingerName());
                btnPlay.setImageResource(R.drawable.pause);
                btnWidgetPlay.setImageResource(R.drawable.pause);


            } else if (Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED.equals(intent.getAction())) {
                int status = intent.getIntExtra(Constant.MEDIA_STATE_EX, 0);
                if (status == Constant.MEDIA_PLAYER_STATE_PAUSED) {
                    btnPlay.setImageResource(R.drawable.play);
                    btnWidgetPlay.setImageResource(R.drawable.play);
                }
                if (status == Constant.MEDIA_PLAYER_STATE_PLAYING) {
                    btnPlay.setImageResource(R.drawable.pause);
                    btnWidgetPlay.setImageResource(R.drawable.pause);
                }
            } else if (Constant.BROADCAST_TIMER.equals(intent.getAction())) {
                boolean state = intent.getBooleanExtra(Constant.TIMER_STATE_EX, false);
                if (state) {
                    int timer = intent.getIntExtra(Constant.TIMER_EX, 0);
                    tvTimer.setText("TM: " + Ultility.milisecondToDuration(timer * 1000));

                } else {
                    tvTimer.setText("");

                }
            } else if (Constant.BROADCAST_PLAYLIST_CHANGED.equals(intent.getAction())) {
                ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra(Constant.SONG_LIST_EX);
                final int postion = intent.getIntExtra(Constant.SONG_POSTON_EX, 0);
                setMiniWidgetVisible(true);
                pendSongListInit(songs, postion);
                mLvSongs.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        mLvSongs.smoothScrollToPosition(postion);
                    }
                });

            } else if (Constant.BROADCAST_ERROR.equals(intent.getAction())) {

                Toast.makeText(getApplication(), "Can't not play this song, playing next song...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void setBtnShuffle() {
        if (btnShuffle != null) {
            btnShuffle.setImageResource(R.drawable.shuffle);
        }
    }
    public void setMiniWidgetVisible(boolean b) {
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (60 * scale + 0.5f);
        if (b) {

            FrameLayout layout = findViewById(R.id.root_fragment);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.setMargins(0, 0, 0, pixels);
            layout.setLayoutParams(params);
            mSildingUpPanelLayout.setPanelHeight(pixels);
            mSildingUpPanelLayout.addPanelSlideListener(mLideUpListener);

        } else {
            mSildingUpPanelLayout.setPanelHeight(0);
            mSildingUpPanelLayout.removePanelSlideListener(mLideUpListener);
        }
    }

    public void playerUiInit() {

        tvWidgetSingerName = findViewById(R.id.singer_name_widget);
        tvWidgetSongName = findViewById(R.id.song_name_widget);
        btnWidgetPlay = findViewById(R.id.btn_play_widget);
        btnWidgetNext = findViewById(R.id.btn_next_widget);
        imgWidgetArt = findViewById(R.id.logo_widget);


        btnNext = findViewById(R.id.btn_next);
        btnPlay = findViewById(R.id.btn_play);
        btnPrev = findViewById(R.id.btn_prev);
        btnRepeat = findViewById(R.id.btn_repeat);
        mainSeekBar = findViewById(R.id.skb_progress);
        widgetSeekBar = findViewById(R.id.mini_player_widget_skb);
        tvDuration = findViewById(R.id.tv_duration);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvSongName = findViewById(R.id.tv_song_name);
        tvSingerName = findViewById(R.id.tv_singer_name);


        btnBackArrow = findViewById(R.id.btn_back_arrow);
        btnMenuArrow = findViewById(R.id.btn_down_menu);
        tvTimer = findViewById(R.id.tv_timer);

        int mode = mSettingManager.getrMode();
        if (mode == Constant.SETTING_REPEAT_MODE_ONE) {
            btnRepeat.setImageResource(R.drawable.repeat_once);
        } else if (mode == Constant.SETTING_REPEAT_MODE_ALL) {

            btnRepeat.setImageResource(R.drawable.repeat);
        } else if (mode == Constant.SETTING_REPEAT_MODE_OFF) {

            btnRepeat.setImageResource(R.drawable.repeat_off);
        }
        btnShuffle = findViewById(R.id.btn_shuffle);
        if (mSettingManager.getsMode() == Constant.SETTING_SHUFFLE_MODE_OFF) {
            btnShuffle.setImageResource(R.drawable.shuffle_disabled);
        } else {
            btnShuffle.setImageResource(R.drawable.shuffle);

        }

        btnMenuArrow.setOnClickListener(this);
        btnBackArrow.setOnClickListener(this);
        btnWidgetPlay.setOnClickListener(this);
        btnWidgetNext.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        mainSeekBar.setOnSeekBarChangeListener(this);

        // register reciever
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED);
        filter.addAction(Constant.BROADCAST_SONG_CHANGED);
        filter.addAction(Constant.BROADCAST_CURRENT_PLAY_TIME);
        filter.addAction(Constant.BROADCAST_TIMER);
        filter.addAction(Constant.BROADCAST_PLAYLIST_CHANGED);
        filter.addAction(Constant.BROADCAST_ERROR);
        registerReceiver(receiver, filter);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_next || id == R.id.btn_next_widget) {
            Intent myIntent = new Intent(MainActivity.this, PlayerService.class);
            myIntent.setAction(Constant.ACTION_NEXT);
            startService(myIntent);

        } else if (id == R.id.btn_prev) {
            Intent myIntent = new Intent(MainActivity.this, PlayerService.class);
            myIntent.setAction(Constant.ACTION_PREV);
            startService(myIntent);
        } else if (id == R.id.btn_play || id == R.id.btn_play_widget) {
            // check for already playing
            Intent myIntent = new Intent(MainActivity.this, PlayerService.class);
            myIntent.setAction(Constant.ACTION_PLAY);
            startService(myIntent);
        } else if (id == R.id.btn_back_arrow) {
            onBackPressed();
        } else if (id == R.id.btn_down_menu) {
            showMusicUiPopupMenu(btnMenuArrow);
        } else if (id == R.id.btn_repeat) {
            int mode = mSettingManager.getrMode();
            if (mode == Constant.SETTING_REPEAT_MODE_ONE) {
                mSettingManager.setrMode(Constant.SETTING_REPEAT_MODE_ALL);
                btnRepeat.setImageResource(R.drawable.repeat);
            } else if (mode == Constant.SETTING_REPEAT_MODE_ALL) {
                mSettingManager.setrMode(Constant.SETTING_REPEAT_MODE_OFF);
                btnRepeat.setImageResource(R.drawable.repeat_off);
            } else if (mode == Constant.SETTING_REPEAT_MODE_OFF) {
                mSettingManager.setrMode(Constant.SETTING_REPEAT_MODE_ONE);
                btnRepeat.setImageResource(R.drawable.repeat_once);
            }
        } else if (id == R.id.btn_shuffle) {
            if (mSettingManager.getsMode() == Constant.SETTING_SHUFFLE_MODE_ON) {
                mSettingManager.setsMode(Constant.SETTING_SHUFFLE_MODE_OFF);
                btnShuffle.setImageResource(R.drawable.shuffle_disabled);
            } else {
                mSettingManager.setsMode(Constant.SETTING_SHUFFLE_MODE_ON);
                btnShuffle.setImageResource(R.drawable.shuffle);
            }
            btnShuffle.setEnabled(false);
            btnShuffle.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnShuffle.setEnabled(true);
                    btnShuffle.setClickable(true);
                }
            }, 1000);
            Intent myIntent = new Intent(this, PlayerService.class);
            myIntent.setAction(Constant.REQUEST_UPDATE_SHUFFLE_MODE);
            startService(myIntent);
        }

    }

    boolean isExit = false;
    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.root_fragment);
        if (mSildingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSildingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (!(f instanceof RootFragment)) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (isExit) {
                finish();
            }
            isExit = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        isSeekBarOnSliding = true;

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeekBarOnSliding = false;
        int currentPosition = Ultility.progressToTimer(seekBar.getProgress(), duration);
        Intent myIntent = new Intent(this, PlayerService.class);
        myIntent.putExtra(Constant.SEEK_TO_POSTION_EX, currentPosition);
        myIntent.setAction(Constant.ACTION_SEEK);
        startService(myIntent);
        // register receiver again

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMusicUiPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.music_ui_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_timer_dialog) {
            new TimerDialog(this).show();
        } else if (id == R.id.action_show_lyric) {
            new LyricDialog(this, currentSong).show();
        }
        return false;
    }
}