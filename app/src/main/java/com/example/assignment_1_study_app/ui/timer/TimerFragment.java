package com.example.assignment_1_study_app.ui.timer;

import android.graphics.drawable.Icon;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.assignment_1_study_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimerFragment extends Fragment {

    private Boolean timerRunning = false;
    private TextView text;
    private FloatingActionButton fab;
    private TextView time;

    private CountDownTimer countdown;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timer, container, false);
        text = root.findViewById(R.id.timer_text);
        fab = root.findViewById(R.id.timer_fab);
        time = root.findViewById(R.id.timer_time);

        text.setText("Time to Study");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        return root;
    }

    private void startTimer() {
        timerRunning = true;
        fab.setImageResource(android.R.drawable.stat_notify_sync_noanim);
        startTimer25();
    }

    private void stopTimer() {
        timerRunning = false;
        fab.setImageResource(android.R.drawable.ic_media_play);
        countdown.cancel();
        time.setText("25:00");
        text.setText("Time to Study");
    }

    private void startTimer25() {
        // starts countdown for 25 minutes (25 * 60 * 1000 = 1500000)
        text.setText("Time to Study");
        countdown = new CountDownTimer(1500000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                countdown.cancel();
                System.out.println("Finished 25");
                ding();
                startTimer5();
            }
        }.start();
    }

    private void startTimer5() {
        // starts countdown for 5 minutes (5 * 60 * 1000 = 300000)
        text.setText("Time for a Break");
        countdown = new CountDownTimer(300000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                countdown.cancel();
                System.out.println("Finished 5");
                ding();
                startTimer25();
            }
        }.start();
    }

    private void updateTimerText(long millisecondsRemaining) {
        System.out.println(millisecondsRemaining);
        String strMinutes = String.valueOf(millisecondsRemaining / 60000);
        String strSeconds;
        int seconds = (int) millisecondsRemaining % 60000 / 1000;
        if (seconds < 10) {
            strSeconds = "0" + String.valueOf(seconds);
        } else {
            strSeconds = String.valueOf(seconds);
        }
        time.setText(strMinutes + ":" + strSeconds);
    }

    private void ding() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
