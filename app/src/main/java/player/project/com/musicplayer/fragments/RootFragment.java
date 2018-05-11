package player.project.com.musicplayer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.controllers.SongScanner;
import player.project.com.musicplayer.customadapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends Fragment {

    Toolbar toolbar;

    public RootFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Music");
        /*
        drawer = ((MainActivity)getActivity()).drawer;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        */
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        //  getActivity().setSupportActionBar(toolbar);
        //((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        /*
        tabLayout.getTabAt(0).setIcon(R.drawable.music_note);
        tabLayout.getTabAt(1).setIcon(R.drawable.music_note);
        tabLayout.getTabAt(2).setIcon(R.drawable.playlist_play);
        tabLayout.getTabAt(3).setIcon(R.drawable.artist);
        tabLayout.getTabAt(4).setIcon(R.drawable.album);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        toolbar.setTitle("Home");
                        break;
                    case 1:
                        toolbar.setTitle("Song");
                        break;
                    case 2:
                        toolbar.setTitle("Playlist");
                        break;
                    case 3:
                        toolbar.setTitle("Artist");
                        break;
                    case 4:
                        toolbar.setTitle("Album");
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
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OnlineFragment(), "Online");
        adapter.addFragment(new SongFragment(), "Songs");
        adapter.addFragment(new PlaylistFragment(), "Playlist");
        adapter.addFragment(new ArtistFragment(), "Artist");
        adapter.addFragment(new AlbumFragment(), "Album");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, searchFragment, "TAG").commit();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_recan) {
            new SongController(getActivity()).deleteAllSong();
            new SongScanner(getActivity()).scan();

            //mLvAdapter.addAll(new SongController(this).getAllSongs());
            //mLvAdapter.notifyDataSetChanged();
        }
        if (id == R.id.action_exit) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
