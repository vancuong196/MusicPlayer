package player.project.com.musicplayer.fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.activities.MainActivity;
import player.project.com.musicplayer.adapters.SongListViewAdapter;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.StartServiceHelper;
import player.project.com.musicplayer.ultilities.Ultility;
import player.project.com.musicplayer.ultilities.XmlParser;

/**
 * Created by nqminh on 09/05/2018.
 */

public class DetailArtistFragment extends Fragment {
    private RecyclerView mLvSongs;
    private SongListViewAdapter mLvAdapter;
    private ArrayList<Song> data;
    private TextView tvNumberOfSong;
    private TextView tvArtistName;
    private Button btnShowInfo;
    ImageView imgCover;
    private String artistName;

    public DetailArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail_artist, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tvArtistName = view.findViewById(R.id.tv_artist_name);
        tvNumberOfSong = view.findViewById(R.id.tv_number_songs);
        imgCover = view.findViewById(R.id.img_cover);
        mLvSongs = view.findViewById(R.id.lv_songs);
        btnShowInfo = view.findViewById(R.id.btn_show_artist_info);

        FloatingActionButton btnShuffleAll = view.findViewById(R.id.btn_shuffle_all);
        btnShuffleAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && data.size() > 0) {
                    StartServiceHelper.sendShuffleAllCommand(getActivity(), data);
                }
            }
        });


        Bundle args = getArguments();
        data = (ArrayList<Song>) args.getSerializable("songList");
        artistName = args.getString("name");
        tvNumberOfSong.setText(String.valueOf(data.size()) + " songs");
        tvArtistName.setText(artistName);
        btnShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment artistInfoFragment = new ArtistInfoFragment();
                Bundle args = new Bundle();
                args.putString("name", artistName);
                artistInfoFragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).addToBackStack("frag").replace(R.id.root_fragment, artistInfoFragment, "TAG").commit();

            }
        });
        btnShowInfo.setEnabled(false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();


        new FetchArtistInfo().execute(artistName);

        mLvAdapter = new SongListViewAdapter(data, getView().getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mLvSongs.setLayoutManager(mLayoutManager);
        mLvSongs.setAdapter(mLvAdapter);
        super.onViewCreated(view, savedInstanceState);

        // Inflate the layout for this fragment
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = getView().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when mToolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Artist: " + artistName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private class FetchArtistInfo extends AsyncTask<String, Void, Boolean> {


        ArrayList<String> info;
        String BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=-artistname-&api_key=0995e324520c21274d839a0e593126cd";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String artistName = strings[0];
            artistName = artistName.split(",")[0];
            artistName = artistName.split("&")[0];
            artistName = artistName.trim();
            artistName = artistName.replaceAll(" ", "%20");
            BASE_URL = BASE_URL.replaceAll("-artistname-", artistName);
            URL url = null;
            try {

                url = new URL(BASE_URL);
                InputStream inputStream = url.openConnection().getInputStream();
                info = new XmlParser().parseArtist(inputStream);
                if (info != null) {
                    return true;
                } else {
                    return false;
                }
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

            if (success) {
                try {
                    Glide.with(getContext()).load(info.get(0)).into(imgCover);
                    btnShowInfo.setEnabled(true);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            } else {
                try {
                    Glide.with(getContext()).load(R.drawable.artist_parallax).into(imgCover);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }
}
