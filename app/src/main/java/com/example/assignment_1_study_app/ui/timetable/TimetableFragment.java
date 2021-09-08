package com.example.assignment_1_study_app.ui.timetable;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.timetable.TimetableContract;
import com.example.assignment_1_study_app.database.timetable.TimetableDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TimetableFragment extends Fragment {
    TimetablePagerAdapter timetablePagerAdapter;
    ViewPager viewPager;
    FloatingActionButton fabAddTimetable;
    TimetableDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timetable_tabbed, container, false);

        dbHelper = new TimetableDbHelper(getContext());

        fabAddTimetable = root.findViewById(R.id.fabAddTimetable);
        fabAddTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_TITLE, "New Class");
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_START_HOUR, 10);
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_START_MIN, 30);
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_END_HOUR, 13);
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_END_MIN, 0);
                values.put(TimetableContract.TimetableEntry.COLUMN_NAME_DAY, viewPager.getCurrentItem());

                long newRowId = db.insert(TimetableContract.TimetableEntry.TABLE_NAME, null, values);

                NavController controller = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("id", newRowId);
                controller.navigate(R.id.nav_edit_timetable, args);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        timetablePagerAdapter = new TimetablePagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(timetablePagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}