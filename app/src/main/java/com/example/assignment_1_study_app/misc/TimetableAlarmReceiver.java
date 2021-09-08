package com.example.assignment_1_study_app.misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimetableAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper.createNotification(context, "YOU HAVE A CLASS", "GET GOING!!");
        DeviceBootReceiver.updateNextSessionAlarm(context);
    }
}
