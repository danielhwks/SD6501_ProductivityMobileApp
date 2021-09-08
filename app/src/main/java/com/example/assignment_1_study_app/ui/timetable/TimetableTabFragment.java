package com.example.assignment_1_study_app.ui.timetable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.timetable.TimetableContract;
import com.example.assignment_1_study_app.database.timetable.TimetableDbHelper;
import com.example.assignment_1_study_app.misc.DeviceBootReceiver;

import java.util.ArrayList;

public class TimetableTabFragment extends Fragment implements RecyclerViewAdapter.OnTimetableListener {

    ArrayList<TimetableSession> mTimetables;

    TimetableDbHelper dbHelper;

    Integer day;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);

        Bundle args = getArguments();
        day = args.getInt("day");

        dbHelper = new TimetableDbHelper(getContext());

        getTimetableValues();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_timetable);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), mTimetables, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return root;
    }

    public void getTimetableValues() {

        mTimetables = new ArrayList<>();

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

        String selection = TimetableContract.TimetableEntry.COLUMN_NAME_DAY + " = ?";
        String[] selectionArgs = { day.toString() };

        String orderBy = TimetableContract.TimetableEntry.COLUMN_NAME_START_HOUR + " ASC, " + TimetableContract.TimetableEntry.COLUMN_NAME_START_MIN;

        Cursor cursor = db.query(
                TimetableContract.TimetableEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
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
    }

    @Override
    public void onTimetableClick(View view, int position) {
        NavController controller = Navigation.findNavController(view);
        Bundle args = new Bundle();
        args.putLong("id", mTimetables.get(position).getId());
        controller.navigate(R.id.nav_edit_timetable, args);
    }
}
