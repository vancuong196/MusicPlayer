package player.project.com.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SettingManager;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.customadapter.SongListViewAdapter;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;


public class SongFragment extends Fragment {
    RecyclerView mLvSongs;
    SongListViewAdapter mLvAdapter;
    ArrayList<Song> data;
    FloatingActionButton fabShuffleAll;

    public SongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        System.out.println("ON CrEATED WIEW");
        fabShuffleAll = view.findViewById(R.id.fab_button);
        mLvSongs = view.findViewById(R.id.lv_songs);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                data = new SongController(getContext()).getAllSongs();
                mLvAdapter = new SongListViewAdapter(data, getView().getContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
                mLvSongs.setLayoutManager(mLayoutManager);
                mLvSongs.setAdapter(mLvAdapter);
            }
        });

        fabShuffleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingManager.getInstance(getContext()).setsMode(Constant.SETTING_SHUFFLE_MODE_ON);
                ((MainActivity) getActivity()).setBtnShuffle();
                Random r = new Random();
                int postion = r.nextInt(data.size() - 1);
                Intent myIntent = new Intent(getContext(), PlayerService.class);
                myIntent.setAction(Constant.ACTION_SONG_CHANGE);
                myIntent.putExtra(Constant.SONG_LIST_EX, data);
                myIntent.putExtra(Constant.SONG_POSTON_EX, postion);
                getActivity().startService(myIntent);
                Toast.makeText(getActivity(), "Shuffle all", Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);


        // Inflate the layout for this fragment
    }


}