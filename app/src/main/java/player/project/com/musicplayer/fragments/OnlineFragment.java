package player.project.com.musicplayer.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.adapters.OnlineAlbumListAdapter;
import player.project.com.musicplayer.models.OnlineAlbum;
import player.project.com.musicplayer.models.Song;
import player.project.com.musicplayer.ultilities.XmlParser;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineFragment extends Fragment {
    private static final String INDEX_URL = "https://www.dropbox.com/s/r54qk8f0lqu66nt/index.xml?dl=1";
    private RecyclerView recyclerView;
    private OnlineAlbumListAdapter adapter;
    private ArrayList<OnlineAlbum> albumList;
    private ProgressBar prgStatus;
    public OnlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_online, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prgStatus = view.findViewById(R.id.prg_status);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        prepareAlbums();
    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        new FetchOnlinePlaylistTask().execute((Void) null);
    }

    private class FetchOnlinePlaylistTask extends AsyncTask<Void, Void, Boolean> {


        ArrayList<OnlineAlbum> list;

        @Override
        protected void onPreExecute() {
            prgStatus.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            URL url = null;
            try {
                url = new URL(INDEX_URL);
                InputStream inputStream = url.openConnection().getInputStream();
                list = new XmlParser().parsePlayList(inputStream);
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
            prgStatus.setVisibility(View.GONE);
            if (success) {

                albumList = list;
                System.out.println(list);
                adapter = new OnlineAlbumListAdapter(getContext(), albumList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Cannot load online content, please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

}