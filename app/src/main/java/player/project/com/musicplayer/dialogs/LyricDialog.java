package player.project.com.musicplayer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.ultilities.LyricsHelper;
import player.project.com.musicplayer.models.Song;

public class LyricDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private Song song;
    private TextView tvTitle;
    private TextView tvLyric;
    private ProgressBar prgWaiting;

    public LyricDialog(@NonNull Context context, Song song) {
        super(context);
        mContext = context;
        this.song = song;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_lyric);
        tvLyric = findViewById(R.id.tv_lyrics);
        tvTitle = findViewById(R.id.tv_title);
        prgWaiting = findViewById(R.id.prg_waiting);
        tvTitle.setText(song.getSongName() + " - " + song.getArtist());
        new BackgroundTask().execute();
    }

    @Override
    public void onClick(View v) {

    }

    public class BackgroundTask extends AsyncTask<Void, Void, Boolean> {
        String lr;

        @Override
        protected Boolean doInBackground(Void... voids) {
            LyricsHelper f = new LyricsHelper(song.getArtist(), song.getSongName());
            if (f.isFound()) {
                lr = f.getLyics();
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            prgWaiting.setVisibility(View.GONE);
            if (success) {
                tvLyric.setText(Html.fromHtml(lr));
            } else {
                tvLyric.setText("Not found!");
            }
        }
    }
}
