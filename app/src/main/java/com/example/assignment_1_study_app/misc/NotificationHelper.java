package com.example.assignment_1_study_app.misc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.assignment_1_study_app.MainActivity;
import com.example.assignment_1_study_app.R;

public class NotificationHelper {
    public static void createNotification(Context context, String title, String text) {
        Intent nIntent = new Intent(context, MainActivity.class);
        nIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "weltec_studyapp")
                .setSmallIcon(R.drawable.ic_keyboard_arrow_right_24px)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(1, builder.build());
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "StudyApp";
            String description = "Notifications from the StudyApp";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("weltec_studyapp", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
