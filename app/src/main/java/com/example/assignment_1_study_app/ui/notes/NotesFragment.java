package com.example.assignment_1_study_app.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.ui.timer.TimerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class NotesFragment extends Fragment implements RecyclerViewAdapter.OnNoteListener {

    private ArrayList<String> mTitles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        initTitles();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_notes);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), mTitles, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        FloatingActionButton btn = root.findViewById(R.id.noteAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                //Bundle args = new Bundle();
                //args.putString("title", "Test");
                //controller.navigate(R.id.nav_edit_notes, args);
                controller.navigate(R.id.nav_edit_notes);
            }
        });

        return root;
    }

    private void initTitles() {
        mTitles = new ArrayList<>();
        File path = getContext().getFilesDir();
        File[] files = path.listFiles();
        for (int i=0; i < files.length; i++) {
            System.out.println(files[i]);
            String filename = files[i].getName();
            mTitles.add(filename);
        }
    }

    @Override
    public void onNoteClick(View view, int position) {
        NavController controller = Navigation.findNavController(view);
        Bundle args = new Bundle();
        args.putString("title", mTitles.get(position));
        controller.navigate(R.id.nav_edit_notes, args);
    }
}
