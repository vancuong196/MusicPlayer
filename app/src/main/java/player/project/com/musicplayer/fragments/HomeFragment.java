package player.project.com.musicplayer.fragments;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.customadapter.OnlineAlbumListAdapter;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.ultilities.XmlParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String INDEX_URL = "https://www.dropbox.com/s/r54qk8f0lqu66nt/index.xml?dl=1";

    private RecyclerView recyclerView;
    private OnlineAlbumListAdapter adapter;
    private ArrayList<OnlineAlbum> albumList;
    private ProgressBar prgStatus;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prgStatus = view.findViewById(R.id.prg_status);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.background).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        MediaPlayer mMediaPlayer= new MediaPlayer();
        try {
            mMediaPlayer.setDataSource("https://www.dropbox.com/s/yvwz29xii0uxws8/Cung-Anh-Ngoc-Dolil-Hagi-STee.mp3?dl=1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("duration"+mMediaPlayer.getDuration());
        mMediaPlayer.start();
        mMediaPlayer.seekTo(10000);
        */
    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        new FetchOnlinePlaylistTask().execute((Void) null);
    }

    private class FetchOnlinePlaylistTask extends AsyncTask<Void, Void, Boolean> {


        ArrayList<OnlineAlbum> list;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            prgStatus.setVisibility(View.VISIBLE);
            URL url = null;
            try {
                url = new URL(INDEX_URL);
                InputStream inputStream = url.openConnection().getInputStream();
                list = new XmlParser().parsePlayList(inputStream);
                url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=my%20tam&api_key=0995e324520c21274d839a0e593126cd");
                inputStream = url.openConnection().getInputStream();
                String[] tmp = new XmlParser().parseArtist(inputStream);
                if (tmp != null) {
                    System.out.println("000000000000000000000" + tmp[0]);
                    System.out.println(tmp[1]);
                }
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            prgStatus.setVisibility(View.GONE);
            if (success) {

                albumList = list;
                System.out.println(list);
                adapter = new OnlineAlbumListAdapter(getContext(), albumList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Cannot load online content, please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
/*
package player.project.com.musicplayer.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.customadapter.AlbumListViewAdapter;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.customadapter.OnlineAlbumListAdapter;
import player.project.com.musicplayer.models.Albumt;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.models.Album;
import player.project.com.musicplayer.ultilities.XmlParser;



public class HomeFragment extends Fragment {
    private static final String INDEX_URL="https://www.dropbox.com/s/r54qk8f0lqu66nt/index.xml?dl=1";

    private RecyclerView recyclerView;
    private OnlineAlbumListAdapter adapter;
    private ArrayList<OnlineAlbum> albumList;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.background).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaPlayer mMediaPlayer= new MediaPlayer();
        try {
            mMediaPlayer.setDataSource("https://www.dropbox.com/s/yvwz29xii0uxws8/Cung-Anh-Ngoc-Dolil-Hagi-STee.mp3?dl=1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("duration"+mMediaPlayer.getDuration());
        mMediaPlayer.start();
        mMediaPlayer.seekTo(10000);

    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) getView().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

 */
/*
    private void prepareAlbums() {
        new FetchFeedTask().execute((Void)null);
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {


        ArrayList<OnlineAlbum> list;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(INDEX_URL);
                InputStream inputStream = url.openConnection().getInputStream();
                list= new XmlParser().parsePlayList(inputStream);
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success){
                albumList = list;
                System.out.println(list);
                adapter = new OnlineAlbumListAdapter(getContext(), albumList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(),"Cannot load online content, please try again later",Toast.LENGTH_LONG).show();
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

 */