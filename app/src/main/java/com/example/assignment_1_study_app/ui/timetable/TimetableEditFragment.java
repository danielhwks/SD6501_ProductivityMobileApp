package com.example.assignment_1_study_app.ui.timetable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.timetable.TimetableContract;
import com.example.assignment_1_study_app.database.timetable.TimetableDbHelper;
import com.example.assignment_1_study_app.misc.DeviceBootReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimetableEditFragment extends Fragment {

    EditText timetableTitle;
    EditText timetableStartHour;
    EditText timetableStartMin;
    EditText timetableEndHour;
    EditText timetableEndMin;
    Button btnDeleteTimetable;
    FloatingActionButton timetableSave;
    Spinner daysSpinner;

    Long tId;
    TimetableSession currentSession;

    SQLiteOpenHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_timetable, container, false);

        timetableTitle = root.findViewById(R.id.timetable_edit_title);
        timetableStartHour = root.findViewById(R.id.timetable_edit_start_hour);
        timetableStartMin = root.findViewById(R.id.timetable_edit_start_min);
        timetableEndHour = root.findViewById(R.id.timetable_edit_end_hour);
        timetableEndMin = root.findViewById(R.id.timetable_edit_end_min);
        timetableSave = root.findViewById(R.id.timetable_edit_save);
        btnDeleteTimetable = root.findViewById(R.id.btnDeleteTimetable);

        daysSpinner = (Spinner) root.findViewById(R.id.timetable_spinner_day);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(adapter);

        dbHelper = new TimetableDbHelper(getContext());

        Bundle args = getArguments();
        tId = args.getLong("id");

        fetchSession();

        timetableTitle.setText(currentSession.getTitle());
        timetableStartHour.setText(currentSession.getStartHour().toString());
        timetableStartMin.setText(currentSession.getStartMin().toString());
        timetableEndHour.setText(currentSession.getEndHour().toString());
        timetableEndMin.setText(currentSession.getEndMin().toString());
        daysSpinner.setSelection(currentSession.getDay());

        btnDeleteTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String selection = TimetableContract.TimetableEntry._ID + " = ?";
                String[] selectionArgs = { tId.toString() };
                int deletedRows = db.delete(TimetableContract.TimetableEntry.TABLE_NAME, selection, selectionArgs);

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        timetableSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_TITLE, timetableTitle.getText().toString());
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_START_HOUR, Integer.parseInt(timetableStartHour.getText().toString()));
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_START_MIN, Integer.parseInt(timetableStartMin.getText().toString()));
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_END_HOUR, Integer.parseInt(timetableEndHour.getText().toString()));
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_END_MIN, Integer.parseInt(timetableEndMin.getText().toString()));
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_DAY, daysSpinner.getSelectedItemPosition());

                String selection = TimetableContract.TimetableEntry._ID + " = ?";
                String[] selectionArgs = { tId.toString() };

                int count = db.update(
                        TimetableContract.TimetableEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                db.close();

                DeviceBootReceiver.updateNextSessionAlarm(getContext());

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        return root;
    }

    private void fetchSession() {
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

        String selection = TimetableContract.TimetableEntry._ID + " = ?";
        String[] selectionArgs = { tId.toString() };

        Cursor cursor = db.query(
                TimetableContract.TimetableEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
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
            currentSession = session;
        }
        cursor.close();
    }
}
