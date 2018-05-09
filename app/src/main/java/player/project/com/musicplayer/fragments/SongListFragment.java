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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.customadapter.SongListViewAdapter;
import player.project.com.musicplayer.models.Song;


public class SongListFragment extends Fragment {
    RecyclerView mLvSongs;
    SongListViewAdapter mLvAdapter;
    ArrayList<Song> data;
    FloatingActionButton fabShuffleAll;
    public SongListFragment() {
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
                Toast.makeText(getActivity(), "Shuffle all", Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);


        // Inflate the layout for this fragment
    }


}