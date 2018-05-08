package player.project.com.musicplayer.customadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;

public class SongListViewAdapter extends RecyclerView.Adapter<SongListViewAdapter.MyViewHolder> {


    ArrayList<Song> dataSet;
    Context mContext;
    Song song;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgIcon;
        TextView tvSingerName;
        TextView tvSongName;
        LinearLayout line;

        public MyViewHolder(View v) {
            super(v);
            imgIcon = v.findViewById(R.id.logo);
            tvSingerName = (TextView) v.findViewById(R.id.singer_name);
            tvSongName = (TextView) v.findViewById(R.id.song_name);
            line = v.findViewById(R.id.song_line);
        }
    }


    public SongListViewAdapter(ArrayList<Song> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        song = new Song();
    }


    @Override
    public SongListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_songs_item, parent, false);

        return new SongListViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongListViewAdapter.MyViewHolder holder, int position) {
        Song song = dataSet.get(position);
        holder.tvSingerName.setText(song.getSingerName());
        holder.tvSongName.setText(song.getSongName());
        final int pos = position;
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("debug-");
                Intent myIntent = new Intent(mContext, PlayerService.class);
                System.out.println("debug--");
                myIntent.setAction(Constant.ACTION_SONG_CHANGE);
                System.out.println("debug---");
                myIntent.putExtra(Constant.SONG_LIST_EX, dataSet);
                System.out.println("debug----");
                myIntent.putExtra(Constant.SONG_POSTON_EX, pos);
                ((MainActivity) mContext).setMiniWidgetVisible(true);
                ((MainActivity) mContext).pendSongListInit(dataSet);
                System.out.println("debug-----");
                mContext.startService(myIntent);
                System.out.println("debug------");
            }
        });
        String path = song.getPath();
        // loading album cover using Glide library
        Glide.with(mContext).load(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_music)).into(holder.imgIcon);

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
        return dataSet.size();
    }
}