package player.project.com.musicplayer;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cuong on 2/1/2018.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<String> data = new ArrayList<>();

    public RecyclerViewAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.songName.setText(data.get(position));
        holder.singerName.setText("Passenger");
        // bắt sự kiện khi kích vào LinearLayout
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(holder.songName.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView singerName;
        LinearLayout line;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            singerName = (TextView) itemView.findViewById(R.id.singer);
            line = (LinearLayout) itemView.findViewById(R.id.line);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}