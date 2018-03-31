package player.project.com.musicplayer;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.models.Song;


/**
 * Created by Cuong on 2/1/2018.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SlidingUpPanelLayout mLayout;
    ImageView btnNext;
    ImageView btnPlay;
    ImageView btnPrev;
    ImageView btnMenu;
    ImageView btnAlarm;
    ImageView btnRepeat;
    ImageView btnShuffle;
    ImageButton btnWidgetPlay;
    ImageButton btnWidgetNext;
    CircleImageView coverArt;
    TextView tvSongNameWidget;
    TextView tvSingerNameWidget;
    SeekBar progressBar;
    TextView tvDuration;
    TextView tvCurrentTime;
    TextView tvSongName;
    TextView tvSingerName;
    ListView mLvSongs;
    PendingSongListAdapter mLvAdapter;

    long duration;
    ArrayList<Song> songList;
    private Handler mHandler = new Handler();

    // ONCREATE
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);


        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //   Log.i(TAG, "onPanelStateChanged " + newState);
                // mLayout.setDragView(findViewById(R.layout.song_list1));
                LinearLayout m = findViewById(R.id.miniLayout);
                if (previousState.equals(SlidingUpPanelLayout.PanelState.DRAGGING) && newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    m.setVisibility(LinearLayout.GONE);
                } else {
                    m.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        playerUiInit();
        setMiniWidgetVisible(false);

    }

    public void setMiniWidgetVisible(boolean b) {
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (60 * scale + 0.5f);
        if (b) {
            mLayout.setPanelHeight(pixels);
        } else {
            mLayout.setPanelHeight(0);
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
                btnPlay.setImageResource(R.drawable.ic_pause);

            }
            if (intent.getAction().equals(Constant.BROADCAST_SONG_CHANGED)) {
                Song song = (Song) intent.getSerializableExtra(Constant.SONG_EX);
                tvSongName.setText(song.getSongName());
                tvSongNameWidget.setText(song.getSongName());
                tvSingerName.setText(song.getSingerName());
                tvSingerNameWidget.setText(song.getSingerName());

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
                if (status == 0) {
                    btnPlay.setImageResource(R.drawable.ic_play);
                    btnWidgetPlay.setImageResource(R.drawable.ic_play);
                }
                if (status == 1) {
                    btnPlay.setImageResource(R.drawable.ic_pause);
                    btnWidgetPlay.setImageResource(R.drawable.ic_pause);
                }
            }
        }
    };


    public void playerUiInit() {
        tvSingerNameWidget = findViewById(R.id.singer_name_widget);
        tvSongNameWidget = findViewById(R.id.song_name_widget);
        coverArt = findViewById(R.id.logo_widget);
        btnMenu = findViewById(R.id.btn_menu);
        btnAlarm = findViewById(R.id.btn_alarm);
        btnNext = findViewById(R.id.btn_next);
        btnPlay = findViewById(R.id.btn_play);
        btnPrev = findViewById(R.id.btn_prev);
        btnRepeat = findViewById(R.id.btn_repeat);
        btnShuffle = findViewById(R.id.btn_shuffle);
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
        registerReceiver(receiver, filter);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongListFragment(), "Song");
        adapter.addFragment(new PlaylistFragment(), "PlayList");
        adapter.addFragment(new ArtistFragment(), "Artist");
        adapter.addFragment(new AlbumFragment(), "Album");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search");

        searchView.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // use this method when query submitted
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // use this method for auto complete search process
                return false;
            }


        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_about) {

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

        } else if (id == R.id.btn_alarm) {
            showDialog(12);

        } else if (id == R.id.btn_repeat) {
            if (SettingManager.getInstance(this).getrMode() == Constant.SETTING_RMODE_ONE) {
                SettingManager.getInstance(this).setrMode(Constant.SETTING_RMODE_ALL);
            } else {
                SettingManager.getInstance(this).setrMode(Constant.SETTING_RMODE_ONE);
            }

        } else if (id == R.id.btn_shuffle) {
            if (SettingManager.getInstance(this).getsMode() == Constant.SETTING_SMODE_ON) {
                SettingManager.getInstance(this).setsMode(Constant.SETTING_SMODE_OFF);
            } else {
                SettingManager.getInstance(this).setsMode(Constant.SETTING_RMODE_ONE);
            }
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 12) {
            new TimerDialog(this).show();
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
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
/*
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import player.project.com.recyclerview.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);



    }
}
*/
/*
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import player.project.com.musicplayer.player.Player;

public class MainActivity extends AppCompatActivity {


    Button start,pause,stop;
    Boolean t=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        start=(Button)findViewById(R.id.button1);
        pause=(Button)findViewById(R.id.button2);
        stop=(Button)findViewById(R.id.button3);
        //creating media player

        final Player mp=Player.getInstance(MainActivity.this);
        try{
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/Music/mainee.mp3");
            mp.prepare();
        }catch(Exception e){
            System.out.println(e.toString());
        }
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player) {
                t=true;
            }
        });

        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t){
                mp.start();}
            }
        });

        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });
        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });

    }
}


*/