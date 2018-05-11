package player.project.com.musicplayer.customadapter;

/**
 * Created by Cuong on 5/7/2018.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.fragments.DetailOnlineAlbumFragment;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.StartServiceHelper;
import player.project.com.musicplayer.ultilities.Ultility;
import player.project.com.musicplayer.ultilities.XmlParser;


public class OnlineAlbumListAdapter extends RecyclerView.Adapter<OnlineAlbumListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OnlineAlbum> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, description;
        public ImageView thumbnail;
        public LinearLayout cardView;
        public Button btnExplore;
        public Button btnShuffleAll;
        public MyViewHolder(View view) {
            super(view);
            btnExplore = view.findViewById(R.id.btn_explore);
            btnShuffleAll = view.findViewById(R.id.btn_shuffle);
            title = view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            description = view.findViewById(R.id.tv_description);
            cardView = view.findViewById(R.id.item_view);
            // overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public OnlineAlbumListAdapter(Context mContext, ArrayList<OnlineAlbum> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_online_album_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OnlineAlbum album = albumList.get(position);
        holder.title.setText(album.getTittle());
        holder.count.setText(album.getNumberOfSong() + " songs");
        holder.description.setText(album.getDecription());
        // loading album cover using Glide library
        Glide.with(mContext).load("https://www.dropbox.com/s/m9ttkxuw9ltgq8o/userqltk.jpg?dl=1").into(holder.thumbnail);
        final int pos = position;
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment albumDetailFragment = new DetailOnlineAlbumFragment();
                Bundle args = new Bundle();
                args.putSerializable("album", albumList.get(pos));
                albumDetailFragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, albumDetailFragment, "TAG").commit();

            }
        });

        holder.btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment albumDetailFragment = new DetailOnlineAlbumFragment();
                Bundle args = new Bundle();
                args.putSerializable("album", albumList.get(pos));
                albumDetailFragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, albumDetailFragment, "TAG").commit();

            }
        });
        holder.btnShuffleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchOnlineAlbumTask().execute(album.getLink());
                Toast.makeText(mContext, "Shuffle all now!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class FetchOnlineAlbumTask extends AsyncTask<String, Void, Boolean> {


        ArrayList<Song> list;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... path) {
            //prgStatus.setVisibility(View.VISIBLE);
            URL url = null;
            try {
                url = new URL(path[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                list = new XmlParser().parseSongList(inputStream);
                Ultility.sortSongList(list);
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            //prgStatus.setVisibility(View.GONE);
            if (success) {
                StartServiceHelper.sendShuffleAllCommand(mContext, list);
            } else {
                Toast.makeText(mContext, "Cannot load online content, please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}