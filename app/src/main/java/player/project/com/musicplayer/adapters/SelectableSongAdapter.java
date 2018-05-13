package player.project.com.musicplayer.adapters;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import player.project.com.musicplayer.models.Song;


public class SelectableSongAdapter extends SongListViewAdapter {
    private ArrayList<Integer> selectedSongsIndex;
    private ArrayList<Song> selectedSongs;

    public SelectableSongAdapter(ArrayList<Song> data, Context context) {
        super(data, context);
        selectedSongsIndex = new ArrayList<>();
        selectedSongs = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Song song = dataSet.get(position);
        if (isItemSelected(position)) {
            holder.line.setSelected(true);
        } else {
            holder.line.setSelected(false);
        }
        holder.tvSingerName.setText(song.getSingerName());
        holder.tvSongName.setText(song.getSongName());
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemSelected(position)) {
                    holder.line.setSelected(false);
                    removeSelectedSong(position);
                } else {
                    holder.line.setSelected(true);
                    selectedSongsIndex.add(position);
                }
            }
        });
    }

    public ArrayList<Song> getSeclectedSongs() {
        for (int i = 0; i < selectedSongsIndex.size(); i++) {
            selectedSongs.add(dataSet.get(selectedSongsIndex.get(i)));
        }
        return selectedSongs;
    }

    public int countSelectedSongs() {
        return selectedSongsIndex.size();
    }

    private boolean isItemSelected(int postion) {
        for (int i = 0; i < selectedSongsIndex.size(); i++) {
            if (selectedSongsIndex.get(i) == postion) {
                return true;
            }
        }
        return false;
    }

    private void removeSelectedSong(int number) {
        for (int i = 0; i < selectedSongsIndex.size(); i++) {
            if (selectedSongsIndex.get(i) == number) {
                selectedSongsIndex.remove(i);
            }
        }
    }
}
