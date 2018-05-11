package player.project.com.musicplayer.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.customadapter.SongListViewAdapter;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.ultilities.StartServiceHelper;
import player.project.com.musicplayer.ultilities.Ultility;

/**
 * Created by Cuong on 5/3/2018.
 */

public class DetailAlbumFragment extends Fragment {
    RecyclerView mLvSongs;
    SongListViewAdapter mLvAdapter;
    ArrayList<Song> data;
    TextView tvAlbumName;
    TextView tvNumberOfSong;
    ImageView imgCover;
    String albumName;

    public DetailAlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail_album, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAlbumName = view.findViewById(R.id.tv_album_name);
        tvNumberOfSong = view.findViewById(R.id.tv_number_songs);
        imgCover = view.findViewById(R.id.img_cover);
        mLvSongs = view.findViewById(R.id.lv_songs);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

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
        data = (ArrayList<Song>) args.getSerializable("songList");
        albumName = args.getString("name");
        tvAlbumName.setText(albumName);
        tvNumberOfSong.setText(String.valueOf(data.size()));

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        initCollapsingToolbar();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setCoverBackgroundImage();

        mLvAdapter = new SongListViewAdapter(data, getView().getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mLvSongs.setLayoutManager(mLayoutManager);
        mLvSongs.setAdapter(mLvAdapter);

    }

    private void setCoverBackgroundImage() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isHaveCoverImage()) {
                Bitmap bm = Ultility.getCoverImageofSong(data.get(i).getPath(), true, null);
                if (bm != null) {
                    imgCover.setImageBitmap(bm);
                    break;
                }
            }

        }

    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = getView().findViewById(R.id.appbar);
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
                    collapsingToolbar.setTitle("Album: " + albumName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

}
