package player.project.com.musicplayer;

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

/**
 * Created by Cuong on 3/25/2018.
 */

public class TimerDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    Button btnOk;
    Button btnCancel;
    SeekBar seekBar;
    TextView textView;
    Context context;

    public TimerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.timer_dialog_ui);
        btnOk = (Button) findViewById(R.id.btn_dialog_ok);
        btnCancel = (Button) findViewById(R.id.btn_dialog_cancel);
        seekBar = findViewById(R.id.skb_timer);
        textView = findViewById(R.id.tv_dialog_time);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_ok:
                Intent myIntent = new Intent(context, PlayerService.class);
                myIntent.putExtra(Constant.TIMER_EX, seekBar.getProgress());
                myIntent.setAction(Constant.ACTION_UPDATE_TIMER);
                context.startService(myIntent);
                break;
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
        textView.setText(String.valueOf(progress) + " minutes");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

