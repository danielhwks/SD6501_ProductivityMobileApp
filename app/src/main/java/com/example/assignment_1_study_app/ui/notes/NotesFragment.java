package com.example.assignment_1_study_app.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_1_study_app.R;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private ArrayList<String> mTitles = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        initTitles();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_notes);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), mTitles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return root;
    }

    private void initTitles() {
        mTitles.add("1");
        mTitles.add("2");
        mTitles.add("3");
        mTitles.add("4");
        mTitles.add("5");
        mTitles.add("6");
        mTitles.add("7");
        mTitles.add("8");
        mTitles.add("9");
        mTitles.add("10");
        mTitles.add("11");
        mTitles.add("12");
        mTitles.add("13");
        mTitles.add("14");
    }
}
