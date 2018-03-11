package player.project.com.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Song;

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
            tt3.setText(Utilitys.milisecondToDuration(song.getDuration()));
        }
        // Return the completed view to render on screen
        return v;
    }
}