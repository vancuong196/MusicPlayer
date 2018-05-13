package player.project.com.musicplayer.fragments;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.EOFException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.adapters.SongListViewAdapter;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.StartServiceHelper;
import player.project.com.musicplayer.ultilities.Ultility;
import player.project.com.musicplayer.ultilities.XmlParser;

/**
 * Created by Cuong on 5/3/2018.
 */

public class DetailOnlineAlbumFragment extends Fragment {
    private RecyclerView mLvSongs;
    private SongListViewAdapter mLvAdapter;
    private ArrayList<Song> data;
    private TextView tvAlbumName;
    private TextView tvNumberOfSong;
    private ImageView imgCover;
    private OnlineAlbum onlineAlbum;
    public DetailOnlineAlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail_online_album, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvAlbumName = view.findViewById(R.id.tv_album_name);
        tvNumberOfSong = view.findViewById(R.id.tv_number_songs);
        imgCover = view.findViewById(R.id.img_cover);
        mLvSongs = view.findViewById(R.id.lv_songs);


        FloatingActionButton btnShuffleAll = view.findViewById(R.id.btn_shuffle_all);
        btnShuffleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && data.size() > 0) {
                    StartServiceHelper.sendShuffleAllCommand(getActivity(), data);
                }
            }
        });


        Bundle args = getArguments();
        onlineAlbum = (OnlineAlbum) args.getSerializable("album");

        if (onlineAlbum == null) {
            Toast.makeText(getActivity(), "Error when loading details of album", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
        try {
            Glide.with(getContext()).load(onlineAlbum.getImageLink()).into(imgCover);
        } catch (Exception e) {
            System.out.println("error when load cover image of album");
        }
        tvAlbumName.setText(onlineAlbum.getTittle().toUpperCase());
        tvNumberOfSong.setText(onlineAlbum.getNumberOfSong() + " songs");
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();

        data = new ArrayList<>();
        mLvAdapter = new SongListViewAdapter(data, getView().getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mLvSongs.setLayoutManager(mLayoutManager);
        mLvSongs.setAdapter(mLvAdapter);
        prepareAlbums();
        super.onViewCreated(view, savedInstanceState);


        // Inflate the layout for this fragment
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = getView().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when mToolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Album: " + onlineAlbum.getTittle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void prepareAlbums() {
        new FetchOnlineAlbumTask().execute(onlineAlbum.getLink());
    }

    public class FetchOnlineAlbumTask extends AsyncTask<String, Void, Boolean> {


        ArrayList<OnlineAlbum> list;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... path) {
            //prgStatus.setVisibility(View.VISIBLE);
            URL url = null;
            try {
                url = new URL(path[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                data = new XmlParser().parseSongList(inputStream);
                Ultility.sortSongList(data);
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
            //prgStatus.setVisibility(View.GONE);
            if (success) {

                mLvAdapter = new SongListViewAdapter(data, getContext());
                mLvSongs.setAdapter(mLvAdapter);
            } else {
                Toast.makeText(getActivity(), "Cannot load online content, please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

}
