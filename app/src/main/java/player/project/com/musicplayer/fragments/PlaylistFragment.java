package player.project.com.musicplayer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.controllers.PlayListController;
import player.project.com.musicplayer.customadapter.PlaylistAdapter;
import player.project.com.musicplayer.models.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    ArrayList<Playlist> playlists;
    private Button adđButton;
    RecyclerView recyclerViewPlaylist;
    private PlaylistAdapter adapter;
    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlists = new PlayListController(getActivity()).getPlayList();
        adapter = new PlaylistAdapter(playlists, getActivity());
        recyclerViewPlaylist = view.findViewById(R.id.lv_playlist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewPlaylist.setLayoutManager(mLayoutManager);
        recyclerViewPlaylist.setAdapter(adapter);
        adđButton = view.findViewById(R.id.btn_add_playlist);
        adđButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlaylistFragment addLyricFragment = new AddPlaylistFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, addLyricFragment, "TAG").commit();
            }
        });

    }
}
