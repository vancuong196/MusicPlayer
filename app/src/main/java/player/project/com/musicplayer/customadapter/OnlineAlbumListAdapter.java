package player.project.com.musicplayer.customadapter;

/**
 * Created by Cuong on 5/7/2018.
 */

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.models.OnlinePlaylist;
import player.project.com.musicplayer.models.Albumt;


public class OnlineAlbumListAdapter extends RecyclerView.Adapter<OnlineAlbumListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OnlinePlaylist> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, description;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            description = view.findViewById(R.id.tv_description);
            // overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public OnlineAlbumListAdapter(Context mContext, ArrayList<OnlinePlaylist> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_album_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        OnlinePlaylist album = albumList.get(position);
        holder.title.setText(album.getTittle());
        holder.count.setText(album.getNumberOfSong() + " songs");
        holder.description.setText(album.getDecription());
        // loading album cover using Glide library
        Glide.with(mContext).load("https://www.dropbox.com/s/m9ttkxuw9ltgq8o/userqltk.jpg?dl=1").into(holder.thumbnail);

        // holder.overflow.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         showPopupMenu(holder.overflow);
        //         Toast.makeText(mContext, "Thank you for trying this app, Find out more...",Toast.LENGTH_SHORT).show();
        //     }
        // });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        /*
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        */
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
          /*
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
        */
            return false;

        }

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}