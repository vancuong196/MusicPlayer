package player.project.com.musicplayer.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.models.Artist;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.ultilities.XmlParser;

public class ListViewArtistAdapter extends ArrayAdapter<Artist> {


    private ArrayList<Artist> dataSet;
    private Context mContext;

    public ListViewArtistAdapter(ArrayList<Artist> data, Context context) {
        super(context, R.layout.lv_artist_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Artist artist = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.lv_artist_item, null);

        }
        CircleImageView tt1 = v.findViewById(R.id.img_atist_logo);
        TextView tt2 = v.findViewById(R.id.tv_artist_name);
        TextView tt3 = v.findViewById(R.id.tv_artist_detail);


        if (tt2 != null) {
            tt2.setText(artist.getName());
        }

        if (tt3 != null) {
            tt3.setText(artist.getNumberOfSong() + " songs| " + artist.getNumberOfAlbum() + " albums");
        }
        // Return the completed view to render on screen
        return v;
    }

}