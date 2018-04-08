package player.project.com.musicplayer.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.models.Song;

public class SongListViewAdapter extends ArrayAdapter<Song> {


    ArrayList<Song> dataSet;
    Context mContext;
    Song song;
    public SongListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SongListViewAdapter(ArrayList<Song> data, Context context) {
        super(context, R.layout.lv_songs_item, data);
        this.dataSet = data;
        this.mContext = context;
        song = new Song();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        song = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.lv_songs_item, null);

        }
        CircleImageView tt1 = v.findViewById(R.id.logo);
        TextView tt3 = (TextView) v.findViewById(R.id.singer_name);
        TextView tt2 = (TextView) v.findViewById(R.id.song_name);
        byte[] data = song.getCoverPicture();
        Bitmap bitmap;
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        } else {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_music);

            //any default cover resourse folder
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_music);
        }
        if (tt1 != null) {
            tt1.setImageBitmap(bitmap);
        }

        if (tt2 != null) {
            tt2.setText(song.getSongName());
        }

        if (tt3 != null) {
            tt3.setText(song.getSingerName());
        }
        // Return the completed view to render on screen
        return v;
    }
}