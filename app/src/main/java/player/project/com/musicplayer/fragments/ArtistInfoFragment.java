package player.project.com.musicplayer.fragments;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import player.project.com.musicplayer.ultilities.XmlParser;

public class ArtistInfoFragment extends Fragment {

    TextView tvArtistName;
    TextView tvDecription;
    TextView tvFullInfo;
    ImageView imgCover;
    Button btnBack;

    public ArtistInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_artist_info, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tvArtistName = view.findViewById(R.id.tv_artist_info_name);
        tvDecription = view.findViewById(R.id.tv_artist_description);
        imgCover = view.findViewById(R.id.artist_cover);
        btnBack = view.findViewById(R.id.btn_return);
        tvFullInfo = view.findViewById(R.id.tv_artist_full_info);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Bundle args = getArguments();
        String artistName = args.getString("name");
        tvArtistName.setText(artistName);
        new FetchArtistInfo().execute(artistName);
        super.onViewCreated(view, savedInstanceState);

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
                    tvDecription.setText(info.get(1));
                    tvFullInfo.setText(info.get(2));
                    Glide.with(getContext()).load(info.get(0)).into(imgCover);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            } else {
                try {
                    tvDecription.setText("Cant not found artist info!");
                    Glide.with(getContext()).load(R.drawable.artist_parallax).into(imgCover);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

}
