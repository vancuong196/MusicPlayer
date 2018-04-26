package player.project.com.musicplayer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import player.project.com.musicplayer.Ultilities.Constant;
import player.project.com.musicplayer.Activities.MainActivity;
import player.project.com.musicplayer.Service.PlayerService;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.Controllers.SongController;
import player.project.com.musicplayer.CustomAdapter.SongListViewAdapter;
import player.project.com.musicplayer.models.Song;


public class SongListFragment extends Fragment {
    ListView mLvSongs;
    SongListViewAdapter mLvAdapter;
    ArrayList<Song> data;

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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                data = new SongController(getContext()).getAllSongs();
                mLvAdapter = new SongListViewAdapter(data, getView().getContext());
                mLvSongs.setAdapter(mLvAdapter);
            }
        });

        mLvSongs = view.findViewById(R.id.lv_songs);
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
        });
        super.onViewCreated(view, savedInstanceState);


        // Inflate the layout for this fragment
    }


}