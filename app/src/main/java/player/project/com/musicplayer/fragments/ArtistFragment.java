package player.project.com.musicplayer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import player.project.com.musicplayer.controllers.SongController;
import player.project.com.musicplayer.adapters.ListViewArtistAdapter;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.models.Artist;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {

    ListView mLvArtist;
    ListViewArtistAdapter mLvAdapter;
    ArrayList<Artist> data;
    SongController songController;

    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songController = new SongController(getContext());
        mLvArtist = view.findViewById(R.id.lv_artist);
        data = songController.getAllArtist();

        mLvAdapter = new ListViewArtistAdapter(data, view.getContext());
        mLvArtist.setAdapter(mLvAdapter);
        mLvArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String artistName = data.get(position).getName();
//                ArrayList<Song> songs = songController.getAllSongBelongArtist(artistName);
//                Intent myIntent = new Intent(getActivity(), PlayerService.class);
//                myIntent.setAction(Constant.ACTION_CHANGE_PLAYLIST);
//                myIntent.putExtra(Constant.SONG_LIST_EX, songs);
//                myIntent.putExtra(Constant.SONG_POSTON_EX, 0);
//                ((MainActivity) getActivity()).setMiniWidgetVisible(true);
//                ((MainActivity) getActivity()).pendSongListInit(songs);
//                getActivity().startService(myIntent);
                Fragment artistDetailFragment = new DetailArtistFragment();
                Bundle args = new Bundle();
                String artistName = data.get(position).getName();
                args.putString("name", artistName);
                args.putSerializable("songList", songController.getAllSongBelongArtist(artistName));
                artistDetailFragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().
                        getSupportFragmentManager();

                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment,
                        artistDetailFragment, "TAG").commit();


            }
        });
    }
}
