package com.example.assignment_1_study_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment_1_study_app.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final Button btnShortcutTimer = root.findViewById(R.id.btnShortcutTimer);
        final Button btnShortcutNotes = root.findViewById(R.id.btnShortcutNotes);
        final Button btnShortcutFlashcards = root.findViewById(R.id.btnShortcutFlashcards);
        final Button btnShortcutTodo = root.findViewById(R.id.btnShortcutTodo);

        textView.setText("What would you like to do?");

        btnShortcutTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.nav_timer);
            }
        });
        btnShortcutNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.nav_notes);
            }
        });
        btnShortcutFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.nav_decks);
            }
        });
        btnShortcutTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.nav_todo);
            }
        });

        return root;
    }
}
