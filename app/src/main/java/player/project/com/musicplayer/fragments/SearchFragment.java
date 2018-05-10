package player.project.com.musicplayer.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.customadapter.SongListViewAdapter;
import player.project.com.musicplayer.models.Song;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    ArrayList<Song> songs;
    ArrayList<Song> resultSongs;
    EditText edtSearch;
    RecyclerView resultList;
    SongListViewAdapter adapter;
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
        edtSearch = view.findViewById(R.id.inputSearch);
        edtSearch.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        resultList = view.findViewById(R.id.lv_songs_search);
        resultSongs = new ArrayList<>();
        adapter = new SongListViewAdapter(resultSongs, getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        resultList.setLayoutManager(mLayoutManager);
        resultList.setAdapter(adapter);
        /*
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), PlayerService.class);
                myIntent.setAction(Constant.ACTION_SONG_CHANGE);
                myIntent.putExtra(Constant.SONG_LIST_EX, resultSongs);
                myIntent.putExtra(Constant.SONG_POSTON_EX, position);
                ((MainActivity) getActivity()).setMiniWidgetVisible(true);
                ((MainActivity) getActivity()).pendSongListInit(resultSongs);
                getActivity().startService(myIntent);
            }
        });
        */
        Runnable getSongsTask = new Runnable() {
            @Override
            public void run() {
                songs = new SongController(getContext()).getAllSongs();
            }
        };
        Handler handler = new Handler();
        handler.post(getSongsTask);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("_____________________________________________");
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

    }

    public void performSearch() {
        if (songs == null || songs.isEmpty() || edtSearch.getText().toString().isEmpty()) {

            adapter = new SongListViewAdapter(new ArrayList<Song>(), getContext());
            resultList.setAdapter(adapter);
        } else {
            resultSongs.clear();
            for (int i = 0; i < songs.size(); i++) {
                if (songs.get(i).getSongName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase())) {
                    resultSongs.add(songs.get(i));
                    System.out.println("-----------------------------------------");
                }
            }
            adapter = new SongListViewAdapter(resultSongs, getContext());
            resultList.setAdapter(adapter);
        }
    }
}
