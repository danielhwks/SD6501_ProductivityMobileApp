package com.example.assignment_1_study_app.misc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.example.assignment_1_study_app.database.timetable.TimetableContract;
import com.example.assignment_1_study_app.database.timetable.TimetableDbHelper;
import com.example.assignment_1_study_app.ui.timetable.TimetableSession;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // notify the user
        updateNextSessionAlarm(context);
    }

    private static Optional<TimetableSession> getNextSession(Calendar now, ArrayList<TimetableSession> mTimetables) {
        int now_day = now.get(Calendar.DAY_OF_WEEK);
        int day = ((now_day - 2) < 0) ? (now_day - 2) + 7 : (now_day - 2);

        TimetableSession nextSession;

        for (int d = day; d < day+7; d++) {
            for (int i = 0; i < mTimetables.size(); i++) {
                TimetableSession session = mTimetables.get(i);
                if (session.getDay() == (d % 7)) {
                    if ((session.getDay() == day
                            && ((session.getStartHour() > now.get(Calendar.HOUR_OF_DAY))
                            || (session.getStartHour() == now.get(Calendar.HOUR_OF_DAY)
                            && session.getStartMin() > now.get(Calendar.MINUTE))))
                            || (session.getDay() != day)) {
                        nextSession = session;
                        return Optional.of(nextSession);
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static Optional<TimetableSession> getSession(Context context, Calendar now) {
        ArrayList<TimetableSession> mTimetables = new ArrayList<>();
        TimetableDbHelper dbHelper = new TimetableDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                TimetableContract.TimetableEntry.COLUMN_NAME_TITLE,
                TimetableContract.TimetableEntry.COLUMN_NAME_START_HOUR,
                TimetableContract.TimetableEntry.COLUMN_NAME_START_MIN,
                TimetableContract.TimetableEntry.COLUMN_NAME_END_HOUR,
                TimetableContract.TimetableEntry.COLUMN_NAME_END_MIN,
                TimetableContract.TimetableEntry.COLUMN_NAME_DAY
        };

        String orderBy = TimetableContract.TimetableEntry.COLUMN_NAME_START_HOUR + " ASC, " + TimetableContract.TimetableEntry.COLUMN_NAME_START_MIN;

        Cursor cursor = db.query(
                TimetableContract.TimetableEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                orderBy
        );

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry._ID)
            );
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry.COLUMN_NAME_TITLE)
            );
            Integer start_hour = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry.COLUMN_NAME_START_HOUR)
            );
            Integer start_min = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry.COLUMN_NAME_START_MIN)
            );
            Integer end_hour = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry.COLUMN_NAME_END_HOUR)
            );
            Integer end_min = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry.COLUMN_NAME_END_MIN)
            );
            Integer day = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TimetableContract.TimetableEntry.COLUMN_NAME_DAY)
            );
            TimetableSession session = new TimetableSession(id, title, start_hour, start_min, end_hour, end_min, day);
            mTimetables.add(session);
        }
        cursor.close();

        // order

        TimetableSession session;

        // check there is at least one session in list
        if (mTimetables.size() > 0) {
            return getNextSession(now, mTimetables);
        } else {
            return Optional.empty();
        }
    }

    public static void updateNextSessionAlarm(Context context) {
        Intent alarmIntent = new Intent(context, TimetableAlarmReceiver.class);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        TimetableSession nextSession;

        Optional<TimetableSession> session = getSession(context, calendar);

        if (session.isPresent()) {
            nextSession = session.get();
        } else {
            System.out.println("NO SESSION FOUND");
            return;
        }

        int hour = nextSession.getStartHour();
        int min = nextSession.getStartMin();

        if (nextSession.getDay() == calendar.get(Calendar.DAY_OF_WEEK)) {
            if (nextSession.getStartHour() > calendar.get(Calendar.HOUR_OF_DAY)) {
                // Right day just set the time
                setCalendarTime(calendar, hour, min);
            } else if (nextSession.getStartHour() < calendar.get(Calendar.HOUR_OF_DAY)) {
                // Right day but a week later, add 7 days and set the time
                increaseDateBy(calendar, 7);
                setCalendarTime(calendar, hour, min);
            } else {
                // within the same hour
                if (nextSession.getStartMin() > calendar.get(Calendar.MINUTE)) {
                    // Right day, set the time
                    setCalendarTime(calendar, hour, min);
                } else {
                    // Add 7 days and set time
                    increaseDateBy(calendar, 7);
                    setCalendarTime(calendar, hour, min);
                }
            }
        } else {
            // Another day of the week, increment until it matches and then set time
            int now_day = calendar.get(Calendar.DAY_OF_WEEK);
            int day = ((now_day - 2) < 0) ? (now_day - 2) + 7 : (now_day - 2);

            // This shouldn't loop forever...
            while (day != nextSession.getDay()) {
                increaseDateBy(calendar, 1);
                now_day = calendar.get(Calendar.DAY_OF_WEEK);
                day = ((now_day - 2) < 0) ? (now_day - 2) + 7 : (now_day - 2);
            }
            // Found the right day
            // Set the time
            setCalendarTime(calendar, hour, min);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private static void setCalendarTime(Calendar calendar, int hour, int min) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
    }

    private static void increaseDateBy(Calendar calendar, int inc) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + inc);
    }
}
