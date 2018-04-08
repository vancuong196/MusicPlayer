package player.project.com.musicplayer.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.models.Album;

public class AlbumListViewAdapter extends ArrayAdapter<Album> {


    ArrayList<Album> dataSet;
    Context mContext;
    Album album;

    public AlbumListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AlbumListViewAdapter(ArrayList<Album> data, Context context) {
        super(context, R.layout.lv_albums_item, data);
        this.dataSet = data;
        this.mContext = context;
        album = new Album();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        album = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.lv_albums_item, null);
        }

        TextView tvAlbumName = v.findViewById(R.id.album_title);
        TextView tvArtist = v.findViewById(R.id.album_detail);
        //   ImageView imgAlbum = v.findViewById(R.id.a);

        //hinh anh cua album
        //  imgAlbum.setImageResource(album.getImgResoure());

        tvAlbumName.setText(album.getAlbumName());
        tvArtist.setText(album.getArtist() + " | " + String.valueOf(album.getNumberOfSong()) + " Songs");
        // tvNumberOfSongs.setText();

        // Return the completed view to render on screen
        return v;
    }
}