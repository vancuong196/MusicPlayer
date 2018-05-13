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
import player.project.com.musicplayer.adapters.PlaylistAdapter;
import player.project.com.musicplayer.models.Playlist;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    private ArrayList<Playlist> playlists;
    private Button mAddButton;
    private RecyclerView mRecyclerViewPlaylist;
    private PlaylistAdapter mLvAdapter;
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
        mLvAdapter = new PlaylistAdapter(playlists, getActivity());
        mRecyclerViewPlaylist = view.findViewById(R.id.lv_playlist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerViewPlaylist.setLayoutManager(mLayoutManager);
        mRecyclerViewPlaylist.setAdapter(mLvAdapter);
        mAddButton = view.findViewById(R.id.btn_add_playlist);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlaylistFragment addLyricFragment = new AddPlaylistFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, addLyricFragment, "TAG").commit();
            }
        });

    }
}
