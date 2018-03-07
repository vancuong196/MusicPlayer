package player.project.com.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.controllers.SongController;


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLvSongs = view.findViewById(R.id.lv_songs);
        data = SongController.getInstance().getSongs();
        mLvAdapter = new SongListViewAdapter(data, view.getContext());
        mLvSongs.setAdapter(mLvAdapter);
        mLvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = data.get(position);
                Intent myIntent = new Intent(getActivity(), PlayerActivity.class);
                myIntent.putExtra("path", song.getPath());
                myIntent.putExtra("name", song.getSongName());
                myIntent.putExtra("duration", song.getDuration());
                startActivity(myIntent);
            }
        });
        // Inflate the layout for this fragment
    }

    public class SongListViewAdapter extends ArrayAdapter<Song> {


        ArrayList<Song> dataSet;
        Context mContext;

        public SongListViewAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public SongListViewAdapter(ArrayList<Song> data, Context context) {
            super(context, R.layout.songlist_item, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Song song = dataSet.get(position);
            // Check if an existing view is being reused, otherwise inflate the view
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                v = inflater.inflate(R.layout.songlist_item, null);

            }
            TextView tt1 = (TextView) v.findViewById(R.id.tittle);
            TextView tt2 = (TextView) v.findViewById(R.id.artist);
            TextView tt3 = (TextView) v.findViewById(R.id.duration);

            if (tt1 != null) {
                tt1.setText(song.getSongName());
            }

            if (tt2 != null) {
                tt2.setText(song.getSingerName());
            }

            if (tt3 != null) {
                tt3.setText(song.getDuration());
            }
            // Return the completed view to render on screen
            return v;
        }
    }
}