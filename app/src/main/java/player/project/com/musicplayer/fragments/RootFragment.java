package player.project.com.musicplayer.fragments;


import android.os.AsyncTask;
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
import android.widget.Toast;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.ultilities.SongScanner;
import player.project.com.musicplayer.adapters.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends Fragment {

    Toolbar mToolbar;

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
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitle("MUSIC");
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
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
            new ScanMusicInBackgroundTask().execute((Void) null);
        } else if (id == R.id.action_exit) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ScanMusicInBackgroundTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            new SongController(getActivity()).deleteAllSong();
            new SongScanner(getActivity()).scan();
            return true;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), "Your music library is updating", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            Toast.makeText(getActivity(), "Your music library is updated! Restart application to see changes", Toast.LENGTH_SHORT).show();

        }
    }
}
