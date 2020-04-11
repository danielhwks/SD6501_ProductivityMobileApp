package com.example.assignment_1_study_app.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment_1_study_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NotesEditFragment extends Fragment {

    EditText editTitle;
    EditText editContent;
    FloatingActionButton btnSave;

    File note;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_notes, container, false);

        editTitle = root.findViewById(R.id.noteTitle);
        editContent = root.findViewById(R.id.noteContent);
        btnSave = root.findViewById(R.id.noteSave);

        Bundle args = getArguments();
        String title = args.getString("title");

        File path = getContext().getFilesDir();
        this.note = new File(path, title);

        editTitle.setText(title);
        editContent.setText(getContents());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(editTitle.getText().toString(), editContent.getText().toString());
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        return root;
    }

    private void saveToFile(String filename, String content) {
        this.note.delete();
        File path = getContext().getFilesDir();
        this.note = new File(path, filename);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(this.note);
            stream.write(content.getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContents() {
        int length = (int) this.note.length();
        byte[] bytes = new byte[length];
        try {
            FileInputStream stream = new FileInputStream(this.note);
            stream.read(bytes);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(bytes);
    }
}
