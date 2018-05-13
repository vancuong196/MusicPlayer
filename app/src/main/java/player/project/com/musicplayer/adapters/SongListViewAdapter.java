package player.project.com.musicplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import player.project.com.musicplayer.R;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;

public class SongListViewAdapter extends RecyclerView.Adapter<SongListViewAdapter.MyViewHolder> {


    ArrayList<Song> dataSet;
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgIcon;
        TextView tvSingerName;
        TextView tvSongName;
        LinearLayout line;

        MyViewHolder(View v) {
            super(v);
            imgIcon = v.findViewById(R.id.logo);
            tvSingerName = v.findViewById(R.id.singer_name);
            tvSongName = v.findViewById(R.id.song_name);
            line = v.findViewById(R.id.song_line);
        }
    }


    public SongListViewAdapter(ArrayList<Song> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public SongListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_song_item, parent, false);

        return new SongListViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongListViewAdapter.MyViewHolder holder, int position) {
        final Song song = dataSet.get(position);
        holder.tvSingerName.setText(song.getSingerName());
        holder.tvSongName.setText(song.getSongName());
        final int pos = position;
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(mContext, PlayerService.class);
                myIntent.setAction(Constant.ACTION_CHANGE_PLAYLIST);
                myIntent.putExtra(Constant.SONG_LIST_EX, dataSet);
                myIntent.putExtra(Constant.SONG_POSTON_EX, pos);
                mContext.startService(myIntent);
                Toast.makeText(mContext, "Playing: " + song.getSongName(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}