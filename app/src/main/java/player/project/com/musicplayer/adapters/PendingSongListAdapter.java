package player.project.com.musicplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;
import player.project.com.musicplayer.ultilities.Ultility;
import player.project.com.musicplayer.models.Song;

public class PendingSongListAdapter extends RecyclerView.Adapter<PendingSongListAdapter.MyViewHolder> {


    private ArrayList<Song> dataSet;
    private Context mContext;
    private int playPostion;
    private PendingSongListAdapter.MyViewHolder lastSelectHolder;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSongName;
        TextView tvSingerName;
        TextView tvDuration;
        LinearLayout line;

        MyViewHolder(View v) {
            super(v);
            tvDuration = v.findViewById(R.id.time);
            tvSingerName = v.findViewById(R.id.singer_name);
            tvSongName = v.findViewById(R.id.song_name);
            line = v.findViewById(R.id.song_line);
        }
    }

    @Override
    public PendingSongListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_song_item_simple, parent, false);

        return new PendingSongListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PendingSongListAdapter.MyViewHolder holder, final int position) {
        Song song = dataSet.get(position);
        if (position == playPostion) {
            lastSelectHolder = holder;
            holder.line.setSelected(true);
        } else {
            holder.line.setSelected(false);
        }
        holder.tvSingerName.setText(song.getSingerName());
        holder.tvSongName.setText(song.getSongName());
        holder.tvDuration.setText(Ultility.milisecondToDuration(song.getDuration()));
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, PlayerService.class);
                myIntent.putExtra(Constant.SONG_POSTON_EX, position);
                myIntent.setAction(Constant.ACTION_CHANGE_SONG_POSTION);
                playPostion = position;
                if (lastSelectHolder != null) {
                    lastSelectHolder.line.setSelected(false);
                }
                holder.line.setSelected(true);
                lastSelectHolder = holder;
                mContext.startService(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public PendingSongListAdapter(ArrayList<Song> data, Context context, int postion) {
        this.dataSet = data;
        this.mContext = context;
        this.playPostion = postion;
    }

    public void setItemSelected(int postion) {
        playPostion = postion;
        notifyDataSetChanged();

    }
}