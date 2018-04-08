package player.project.com.musicplayer.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.Ultilities.Utilitys;
import player.project.com.musicplayer.models.Song;

public class PendingSongListAdapter extends ArrayAdapter<Song> {


    ArrayList<Song> dataSet;
    Context mContext;

    public PendingSongListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PendingSongListAdapter(ArrayList<Song> data, Context context) {
        super(context, R.layout.lv_song_item_simple, data);
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
            v = inflater.inflate(R.layout.lv_song_item_simple, null);

        }
        TextView tt1 = v.findViewById(R.id.song_name);
        TextView tt3 = (TextView) v.findViewById(R.id.singer_name);
        TextView tt2 = (TextView) v.findViewById(R.id.time);
        if (tt1 != null) {
            tt1.setText(song.getSongName());
        }

        if (tt3 != null) {
            tt3.setText(song.getSingerName());
        }

        if (tt2 != null) {
            tt2.setText(Utilitys.milisecondToDuration(song.getDuration()));
        }
        // Return the completed view to render on screen
        return v;
    }
}