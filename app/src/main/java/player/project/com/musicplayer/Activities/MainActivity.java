package player.project.com.musicplayer.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.Controllers.SettingManager;
import player.project.com.musicplayer.Controllers.SongController;
import player.project.com.musicplayer.Controllers.SongScaner;
import player.project.com.musicplayer.CustomAdapter.PendingSongListAdapter;
import player.project.com.musicplayer.CustomAdapter.ViewPagerAdapter;
import player.project.com.musicplayer.Dialog.LyricDialog;
import player.project.com.musicplayer.Dialog.TimerDialog;
import player.project.com.musicplayer.Fragments.AlbumFragment;
import player.project.com.musicplayer.Fragments.ArtistFragment;
import player.project.com.musicplayer.Fragments.PlaylistFragment;
import player.project.com.musicplayer.Fragments.RootFragment;
import player.project.com.musicplayer.Fragments.SearchFragment;
import player.project.com.musicplayer.Fragments.SongListFragment;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.Service.PlayerService;
import player.project.com.musicplayer.Ultilities.Constant;
import player.project.com.musicplayer.Ultilities.Utilitys;
import player.project.com.musicplayer.models.Song;


/**
 * Created by Cuong on 2/1/2018.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    ImageView btnNext;
    ImageView btnPlay;
    ImageView btnPrev;
    ImageView btnMenu;
    ImageView btnAlarm;
    ImageView btnRepeat;
    ImageView btnShuffle;
    ImageView btnWidgetPlay;
    ImageView btnWidgetNext;
    CircleImageView coverArt;
    TextView tvSongNameWidget;
    TextView tvSingerNameWidget;
    SeekBar progressBar;
    TextView tvDuration;
    TextView tvTimer;
    TextView tvCurrentTime;
    TextView tvSongName;
    TextView tvSingerName;
    DrawerLayout drawer;
    ListView mLvSongs;
    PendingSongListAdapter mLvAdapter;
    SlidingUpPanelLayout.PanelSlideListener mLideUpListener;
    Toolbar toolbar;
    long duration;
    Song currentSong;
    //  private TabLayout tabLayout;
    // private ViewPager viewPager;
    private SlidingUpPanelLayout mLayout;
    // ONCREATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int version = Build.VERSION.SDK_INT;
        if (version > Build.VERSION_CODES.LOLLIPOP) {
            if (!checkIfHavePermisson()) {
                requestForPermission();
            }
        }
        setContentView(R.layout.activity_main);

        //      toolbar = findViewById(R.id.toolbar);
        //       setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment rootFragment = new RootFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        //tabLayout.setVisibility(View.GONE);
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.root_fragment, rootFragment, "TAG").commit();

        /*
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.music_note);
        tabLayout.getTabAt(1).setIcon(R.drawable.playlist_play);
        tabLayout.getTabAt(2).setIcon(R.drawable.artist);
        tabLayout.getTabAt(3).setIcon(R.drawable.album);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        getSupportActionBar().setTitle("Song");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Playlist");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Artist");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Album");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        mLideUpListener = new SlidingUpPanelLayout.PanelSlideListener() {
            float previous = 0;
            boolean isup = false;
            boolean pr = false;
            int state = 0;

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                final LinearLayout m = findViewById(R.id.miniLayout);
                Log.i("Panel:------------", "onPanelSlide, offset " + slideOffset);
                if (slideOffset > previous && (slideOffset - previous > 0.2)) {
                    isup = true;
                    previous = slideOffset;
                    if (pr != true) {
                        state = 1;
                        pr = true;
                    } else {
                        state = 0;
                        pr = true;
                    }

                } else if (slideOffset <= previous && (-slideOffset + previous > 0.2)) {
                    isup = false;
                    previous = slideOffset;
                    if (pr != false) {
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
                    btnWidgetNext.setVisibility(View.VISIBLE);
                    btnWidgetPlay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //   Log.i(TAG, "onPanelStateChanged " + newState);
                // mLayout.setDragView(findViewById(R.layout.song_list1));

            }
        };
        playerUiInit();
        setMiniWidgetVisible(false);
        if (new SongController(getApplicationContext()).count() == 0) {
            new SongScaner(this).scan();

        }

    }

    public void pendSongListInit(ArrayList<Song> data) {
        mLvSongs = findViewById(R.id.lv_pending_songs);
        mLvAdapter = new PendingSongListAdapter(data, this);
        mLvSongs.setAdapter(mLvAdapter);
        mLvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MainActivity.this, PlayerService.class);
                myIntent.setAction(Constant.ACTION_CHANGE_POSTION);
                myIntent.putExtra(Constant.SONG_POSTON_EX, position);
                startService(myIntent);
            }
        });
    }

    private boolean checkIfHavePermisson() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED ? true : false;
    }

    private void requestForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1010);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1010) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "This application need READ_EXTERNAL_STORAGE permission to work correctly!", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {


            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

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
                tvCurrentTime.setText("" + Utilitys.milisecondToDuration(currentDuration));
                // Updating progress bar
                int progress = (int) (Utilitys.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                progressBar.setProgress(progress);


            }
            if (intent.getAction().equals(Constant.BROADCAST_SONG_CHANGED)) {
                Song song = (Song) intent.getSerializableExtra(Constant.SONG_EX);
                currentSong = song;
                tvSongName.setText(song.getSongName());
                tvSongNameWidget.setText(song.getSongName());
                tvSingerName.setText(song.getSingerName());
                tvSingerNameWidget.setText(song.getSingerName());
                btnPlay.setImageResource(R.drawable.pause);
                btnWidgetPlay.setImageResource(R.drawable.pause);
                //     byte [] data= song.getCoverPicture();
                //    Bitmap bitmap;
                //    if(data != null)
                //    {
                //      bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //     }
                //     else
                //     {
                //        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music); //any default cover resourse folder
                //   }
                //     coverArt.setImageBitmap(bitmap);


            }
            if (intent.getAction().equals(Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED)) {
                int status = intent.getIntExtra(Constant.MEDIA_STATE_EX, 0);
                if (status == Constant.MEDIA_PLAYER_PAUSED) {
                    btnPlay.setImageResource(R.drawable.play);
                    btnWidgetPlay.setImageResource(R.drawable.play);
                }
                if (status == Constant.MEDIA_PLAYER_PLAYING) {
                    btnPlay.setImageResource(R.drawable.pause);
                    btnWidgetPlay.setImageResource(R.drawable.pause);
                }
            }
            if (intent.getAction().equals(Constant.BROADCAST_TIMER)) {
                boolean state = intent.getBooleanExtra(Constant.TIMER_STATE_EX, false);
                if (state == true) {
                    int timer = intent.getIntExtra(Constant.TIMER_EX, 0);
                    tvTimer.setText(Utilitys.milisecondToDuration(timer * 1000));
                } else {
                    tvTimer.setText("");
                }
            }
        }
    };

    public void setMiniWidgetVisible(boolean b) {
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (60 * scale + 0.5f);
        if (b) {
            mLayout.setPanelHeight(pixels);
            mLayout.addPanelSlideListener(mLideUpListener);
        } else {
            mLayout.setPanelHeight(0);
            mLayout.removePanelSlideListener(mLideUpListener);
        }
    }

    public void playerUiInit() {
        tvSingerNameWidget = findViewById(R.id.singer_name_widget);
        tvSongNameWidget = findViewById(R.id.song_name_widget);
        coverArt = findViewById(R.id.logo_widget);
        btnMenu = findViewById(R.id.btn_menu);
        btnAlarm = findViewById(R.id.btn_alarm);
        tvTimer = findViewById(R.id.tv_timer);
        btnNext = findViewById(R.id.btn_next);
        btnPlay = findViewById(R.id.btn_play);
        btnPrev = findViewById(R.id.btn_prev);
        btnRepeat = findViewById(R.id.btn_repeat);
        int mode = SettingManager.getInstance(this).getrMode();
        if (mode == Constant.SETTING_REPEAT_MODE_ONE) {
            btnRepeat.setImageResource(R.drawable.repeat_once);
        } else if (mode == Constant.SETTING_REPEAT_MODE_ALL) {

            btnRepeat.setImageResource(R.drawable.repeat);
        } else if (mode == Constant.SETTING_REPEAT_MODE_OFF) {

            btnRepeat.setImageResource(R.drawable.repeat_off);
        }
        btnShuffle = findViewById(R.id.btn_shuffle);
        if (SettingManager.getInstance(this).getsMode() == Constant.SETTING_SHUFFLE_MODE_OFF) {
            btnShuffle.setImageResource(R.drawable.shuffle_disabled);
        } else {
            btnShuffle.setImageResource(R.drawable.shuffle);
        }
        progressBar = findViewById(R.id.skb_progress);
        tvDuration = findViewById(R.id.tv_duration);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvSongName = findViewById(R.id.tv_song_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        btnWidgetPlay = findViewById(R.id.btn_play_widget);
        btnWidgetNext = findViewById(R.id.btn_next_widget);
        btnWidgetPlay.setOnClickListener(this);
        btnWidgetNext.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnAlarm.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        // register recieve
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED);
        filter.addAction(Constant.BROADCAST_SONG_CHANGED);
        filter.addAction(Constant.BROADCAST_CURRENT_PLAY_TIME);
        filter.addAction(Constant.BROADCAST_TIMER);
        registerReceiver(receiver, filter);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongListFragment(), "");
        adapter.addFragment(new PlaylistFragment(), "");
        adapter.addFragment(new ArtistFragment(), "");
        adapter.addFragment(new AlbumFragment(), "");


        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

        } else if (id == R.id.nav_changlog) {

        } else if (id == R.id.nav_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            IntentFilter filter = new IntentFilter();
            startService(myIntent);
        } else if (id == R.id.btn_play || id == R.id.btn_play_widget) {
            // check for already playing
            Intent myIntent = new Intent(MainActivity.this, PlayerService.class);
            myIntent.setAction(Constant.ACTION_PLAY);
            startService(myIntent);
        } else if (id == R.id.btn_menu) {
            new LyricDialog(this, currentSong).show();
            return;
        } else if (id == R.id.btn_alarm) {
            showDialog(12);

        } else if (id == R.id.btn_repeat) {
            int mode = SettingManager.getInstance(this).getrMode();
            if (mode == Constant.SETTING_REPEAT_MODE_ONE) {
                SettingManager.getInstance(this).setrMode(Constant.SETTING_REPEAT_MODE_ALL);
                btnRepeat.setImageResource(R.drawable.repeat);
            } else if (mode == Constant.SETTING_REPEAT_MODE_ALL) {
                SettingManager.getInstance(this).setrMode(Constant.SETTING_REPEAT_MODE_OFF);
                btnRepeat.setImageResource(R.drawable.repeat_off);
            } else if (mode == Constant.SETTING_REPEAT_MODE_OFF) {
                SettingManager.getInstance(this).setrMode(Constant.SETTING_REPEAT_MODE_ONE);
                btnRepeat.setImageResource(R.drawable.repeat_once);
            }

        } else if (id == R.id.btn_shuffle) {
            if (SettingManager.getInstance(this).getsMode() == Constant.SETTING_SHUFFLE_MODE_ON) {
                SettingManager.getInstance(this).setsMode(Constant.SETTING_SHUFFLE_MODE_OFF);
                btnShuffle.setImageResource(R.drawable.shuffle_disabled);
            } else {
                SettingManager.getInstance(this).setsMode(Constant.SETTING_SHUFFLE_MODE_ON);
                btnShuffle.setImageResource(R.drawable.shuffle);
            }
        }

    }

    boolean isExit = false;
    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.root_fragment);
        if (!(f instanceof RootFragment)) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (isExit == true) {
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
    protected Dialog onCreateDialog(int id) {
        if (id == 12) {
            new TimerDialog(this).show();
        }
        if (id == 13) {

        }
        return super.onCreateDialog(id);
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
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_MEDIA_PLAYER_STATE_CHANGED);
        filter.addAction(Constant.BROADCAST_SONG_CHANGED);
        filter.addAction(Constant.BROADCAST_CURRENT_PLAY_TIME);
        filter.addAction(Constant.BROADCAST_TIMER);
        registerReceiver(receiver, filter);
        int currentPosition = Utilitys.progressToTimer(seekBar.getProgress(), duration);
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
}