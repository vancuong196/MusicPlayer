package player.project.com.musicplayer.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import player.project.com.musicplayer.Activities.MainActivity;
import player.project.com.musicplayer.Controllers.SongController;
import player.project.com.musicplayer.Controllers.SongScaner;
import player.project.com.musicplayer.CustomAdapter.AlbumListViewAdapter;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.Service.PlayerService;
import player.project.com.musicplayer.Ultilities.Constant;
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
                String albumName = data.get(position).getAlbumName();
                Intent myIntent = new Intent(getActivity(), PlayerService.class);
                myIntent.setAction(Constant.ACTION_SONG_CHANGE);
                myIntent.putExtra(Constant.SONG_LIST_EX, songController.getAllSongBelongToAlbums(albumName, data.get(position).getArtist()));
                myIntent.putExtra(Constant.SONG_POSTON_EX, 0);
                ((MainActivity) getActivity()).setMiniWidgetVisible(true);
                ((MainActivity) getActivity()).pendSongListInit(songController.getAllSongBelongToAlbums(albumName, data.get(position).getArtist()));
                getActivity().startService(myIntent);
            }
        });
    }
}
