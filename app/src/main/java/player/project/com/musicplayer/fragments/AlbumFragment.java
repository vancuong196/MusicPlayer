package player.project.com.musicplayer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.customadapter.AlbumListViewAdapter;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.models.Album;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    GridView mLvAlbums;
    AlbumListViewAdapter mLvAdapter;
    ArrayList<Album> data;
    SongController songController;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songController = new SongController(getContext());
        mLvAlbums = view.findViewById(R.id.lv_albums);
        data = songController.getAllAlbums();

        mLvAdapter = new AlbumListViewAdapter(data, view.getContext());
        mLvAlbums.setAdapter(mLvAdapter);
        mLvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment albumDetailFragment = new DetailAlbumFragment();
                Bundle args = new Bundle();
                String albumName = data.get(position).getAlbumName();
                args.putString("name", albumName);
                args.putSerializable("songList", songController.getAllSongBelongToAlbums(albumName, data.get(position).getArtist()));
                albumDetailFragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, albumDetailFragment, "TAG").commit();


                /*
                String albumName = data.get(position).getAlbumName();
                Intent myIntent = new Intent(getActivity(), PlayerService.class);
                myIntent.setAction(Constant.ACTION_SONG_CHANGE);
                myIntent.putExtra(Constant.SONG_LIST_EX, songController.getAllSongBelongToAlbums(albumName, data.get(position).getArtist()));
                myIntent.putExtra(Constant.SONG_POSTON_EX, 0);
                ((MainActivity) getActivity()).setMiniWidgetVisible(true);
                ((MainActivity) getActivity()).pendSongListInit(songController.getAllSongBelongToAlbums(albumName, data.get(position).getArtist()));
                getActivity().startService(myIntent);
                */
            }
        });
    }
}
