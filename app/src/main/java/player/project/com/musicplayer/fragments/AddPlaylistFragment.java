package player.project.com.musicplayer.fragments;

import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.controllers.PlayListController;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.adapters.SelectableSongAdapter;
import player.project.com.musicplayer.models.Playlist;
import player.project.com.musicplayer.models.Song;

public class AddPlaylistFragment extends Fragment {
    EditText edtTitle;
    Button btnCancel, btnOk;
    RecyclerView lstSongs;
    SelectableSongAdapter adapter;

    public AddPlaylistFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtTitle = view.findViewById(R.id.edt_title);
        lstSongs = view.findViewById(R.id.lv_songs);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnOk = view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cancel add playlist!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();


            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null && adapter.countSelectedSongs() > 0) {
                    createPlaylist();
                    Toast.makeText(getActivity(), "Added playlist!", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), "Select at least 1 song to make the playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Song> songs = new SongController(getActivity()).getAllSongs();
                adapter = new SelectableSongAdapter(songs, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                lstSongs.setLayoutManager(mLayoutManager);
                lstSongs.setAdapter(adapter);
            }
        });
    }

    private void createPlaylist() {
        String playListName = edtTitle.getText().toString();
        if (playListName == null || playListName.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter playlist name first!", Toast.LENGTH_SHORT).show();
            return;
        }
        PlayListController playListController = new PlayListController(getContext());
        ArrayList<Playlist> playlists = playListController.getPlayList();
        for (int i = 0; i < playlists.size(); i++) {
            if (playListName.equals(playlists.get(i).getName())) {
                Toast.makeText(getActivity(), "This paylist name is available, try to enter a different one!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        ArrayList<Song> songs = adapter.getSeclectedSongs();
        for (int i = 0; i < songs.size(); i++) {
            playListController.addPlaylist(playListName, songs.get(i).getPath());
        }
    }


}
