package player.project.com.musicplayer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import player.project.com.musicplayer.R;
import player.project.com.musicplayer.service.PlayerService;
import player.project.com.musicplayer.ultilities.Constant;

/**
 * Created by Cuong on 3/25/2018.
 */

public class TimerDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private SeekBar seekBar;
    private TextView textView;
    private Context context;

    public TimerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_timer);
        Button btnOk = (Button) findViewById(R.id.btn_dialog_ok);
        Button btnCancel = (Button) findViewById(R.id.btn_dialog_cancel);
        seekBar = findViewById(R.id.skb_timer);
        textView = findViewById(R.id.tv_dialog_time);
        Button btnDisaleTimer = findViewById(R.id.btn_dialog_disable_timer);
        btnDisaleTimer.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(context, PlayerService.class);
        switch (v.getId()) {
            case R.id.btn_dialog_ok:
                myIntent.putExtra(Constant.TIMER_EX, seekBar.getProgress() + 1);
                myIntent.setAction(Constant.ACTION_UPDATE_TIMER);
                context.startService(myIntent);
                break;
            case R.id.btn_dialog_disable_timer:
                myIntent.setAction(Constant.ACTION_DISABLE_TIMER);
                context.startService(myIntent);
            case R.id.btn_dialog_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText(String.valueOf(progress + 1) + " minutes");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

