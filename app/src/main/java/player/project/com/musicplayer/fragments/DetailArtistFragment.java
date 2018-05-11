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
import android.widget.ImageView;
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
 * Created by nqminh on 09/05/2018.
 */

public class DetailArtistFragment extends Fragment {
    RecyclerView mLvSongs;
    SongListViewAdapter mLvAdapter;
    ArrayList<Song> data;
    TextView tvNumberOfSong;
    TextView tvArtistName;
    ImageView imgCover;
    String artistName;

    public DetailArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail_artist, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("ON CrEATED WIEW");

        tvArtistName = view.findViewById(R.id.tv_artist_name);
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
        data = (ArrayList<Song>) args.getSerializable("songList");
        artistName = args.getString("name");

        tvArtistName.setText(artistName);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        initCollapsingToolbar();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvNumberOfSong.setText(String.valueOf(data.size()) + " songs");

        for (int i = 0; i < data.size(); i++) {
            String path = data.get(i).getPath();
            Bitmap bm = Ultility.getCoverImageofSong(path, true, null);
            if (bm != null) {
                imgCover.setImageBitmap(bm);
                break;
            }
            if (bm == null && i == data.size() - 1) {
                imgCover.setImageResource(R.drawable.background);
            }
            ;
        }

        mLvAdapter = new SongListViewAdapter(data, getView().getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mLvSongs.setLayoutManager(mLayoutManager);
        /*
        mLvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), PlayerService.class);
                myIntent.setAction(Constant.ACTION_SONG_CHANGE);
                myIntent.putExtra(Constant.SONG_LIST_EX, data);
                myIntent.putExtra(Constant.SONG_POSTON_EX, position);
                ((MainActivity) getActivity()).setMiniWidgetVisible(true);
                ((MainActivity) getActivity()).pendSongListInit(data);
                getActivity().startService(myIntent);
            }
        });*/
        mLvSongs.setAdapter(mLvAdapter);
        super.onViewCreated(view, savedInstanceState);


        // Inflate the layout for this fragment
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
                    collapsingToolbar.setTitle("Artist: " + artistName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
