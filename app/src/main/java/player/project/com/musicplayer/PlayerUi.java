package player.project.com.musicplayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PlayerUi extends AppCompatActivity implements View.OnClickListener {
    ImageButton nextButton;
    ImageButton playButton;
    ImageButton prevButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ui);
        nextButton = findViewById(R.id.btn_next);
        playButton = findViewById(R.id.btn_play);
        prevButton = findViewById(R.id.btn_prev);
        nextButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {

        } else if (v.getId() == R.id.btn_next) {


        } else if (v.getId() == R.id.btn_next) {

        }
    }
}
