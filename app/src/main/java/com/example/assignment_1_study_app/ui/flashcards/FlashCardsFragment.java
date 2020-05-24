package com.example.assignment_1_study_app.ui.flashcards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.assignment_1_study_app.R;

public class FlashCardsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_flashcards, container, false);

        return root;
    }
}
