package player.project.com.musicplayer.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.adapters.SongListViewAdapter;
import player.project.com.musicplayer.models.Song;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    ArrayList<Song> songs;
    ArrayList<Song> resultSongs;
    EditText mEdtSearch;
    RecyclerView mRecyclerViewResultList;
    SongListViewAdapter mLvAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEdtSearch = view.findViewById(R.id.inputSearch);
        mEdtSearch.requestFocus();
        mRecyclerViewResultList = view.findViewById(R.id.lv_songs_search);
        resultSongs = new ArrayList<>();
        mLvAdapter = new SongListViewAdapter(resultSongs, getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerViewResultList.setLayoutManager(mLayoutManager);
        mRecyclerViewResultList.setAdapter(mLvAdapter);
        Runnable getSongsTask = new Runnable() {
            @Override
            public void run() {
                songs = new SongController(getContext()).getAllSongs();
            }
        };
        Handler handler = new Handler();
        handler.post(getSongsTask);
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void performSearch() {
        if (songs == null || songs.isEmpty() || mEdtSearch.getText().toString().isEmpty()) {

            mLvAdapter = new SongListViewAdapter(new ArrayList<Song>(), getContext());
            mRecyclerViewResultList.setAdapter(mLvAdapter);
        } else {
            resultSongs.clear();
            for (int i = 0; i < songs.size(); i++) {
                if (songs.get(i).getSongName().toLowerCase().contains(mEdtSearch.getText().toString().toLowerCase())) {
                    resultSongs.add(songs.get(i));
                    System.out.println("-----------------------------------------");
                }
            }
            mLvAdapter = new SongListViewAdapter(resultSongs, getContext());
            mRecyclerViewResultList.setAdapter(mLvAdapter);
        }
    }
}
