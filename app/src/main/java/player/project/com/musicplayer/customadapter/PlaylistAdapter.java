package player.project.com.musicplayer.customadapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.controllers.PlayListController;
import player.project.com.musicplayer.fragments.DetailAlbumFragment;
import player.project.com.musicplayer.fragments.DetailPlaylistFragment;
import player.project.com.musicplayer.models.Playlist;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.StartServiceHelper;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {


    ArrayList<Playlist> dataSet;
    Context mContext;
    int selectedItemPostion = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumberOfSong;
        TextView tvPlaylistName;
        ImageView imgThreeDot;
        LinearLayout line;

        public MyViewHolder(View v) {
            super(v);

            tvPlaylistName = v.findViewById(R.id.tv_playlist_name);
            tvNumberOfSong = v.findViewById(R.id.tv_number_songs);
            imgThreeDot = v.findViewById(R.id.image_three_menu);
            line = v.findViewById(R.id.playlist_line);
        }
    }

    @Override
    public PlaylistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_playlist_item, parent, false);

        return new PlaylistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistAdapter.MyViewHolder holder, final int position) {
        Playlist playlist = dataSet.get(position);
        holder.tvPlaylistName.setText(playlist.getName());
        holder.tvNumberOfSong.setText(String.valueOf(playlist.getNumberOfSongs()) + " Songs");
        holder.imgThreeDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.imgThreeDot, position);
            }
        });
        final int pos = position;
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment playlistDetailFragment = new DetailPlaylistFragment();
                Bundle args = new Bundle();
                Playlist playlist = dataSet.get(position);
                args.putSerializable("playlist", playlist);
                playlistDetailFragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, playlistDetailFragment, "TAG").commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public PlaylistAdapter(ArrayList<Playlist> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }

    /**
     * Click listener for popup menu items
     */
    private void showPopupMenu(View view, int postion) {
        // inflate menu
        selectedItemPostion = postion;
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.playlist_item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();

    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_remove:
                    new PlayListController(mContext).deletePlayList(dataSet.get(selectedItemPostion));
                    dataSet.remove(selectedItemPostion);
                    notifyItemChanged(selectedItemPostion);
                    return true;
                case R.id.action_shuffe:
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Song> songs = new PlayListController(mContext).getAllSongBeLongPlayList(dataSet.get(selectedItemPostion).getName());
                            StartServiceHelper.sendShuffleAllCommand(mContext, songs);
                        }
                    });
                    return true;
                default:
            }

            return false;

        }

    }

}