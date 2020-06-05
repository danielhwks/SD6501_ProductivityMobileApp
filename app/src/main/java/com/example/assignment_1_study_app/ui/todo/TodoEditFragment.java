package com.example.assignment_1_study_app.ui.todo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.todo.TodoContract;
import com.example.assignment_1_study_app.database.todo.TodoDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoEditFragment extends Fragment {

    EditText todoTitle;
    Button btnDeleteTodo;
    FloatingActionButton todoSave;

    Long tId;

    SQLiteOpenHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_todo, container, false);

        todoTitle = root.findViewById(R.id.todoTitle);
        btnDeleteTodo = root.findViewById(R.id.btnDeleteTodo);
        todoSave = root.findViewById(R.id.todoSave);

        dbHelper = new TodoDbHelper(getContext());

        Bundle args = getArguments();
        tId = args.getLong("id");

        getTitle();

        btnDeleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String selection = TodoContract.TodoEntry._ID + " = ?";
                String[] selectionArgs = { tId.toString() };

                int deletedRows = db.delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs);

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        todoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_NAME_TITLE, todoTitle.getText().toString());

                String selection = TodoContract.TodoEntry._ID + " = ?";
                String[] selectionArgs = { tId.toString() };

                int count = db.update(
                        TodoContract.TodoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        return root;
    }

    private void getTitle() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                TodoContract.TodoEntry.COLUMN_NAME_TITLE
        };

        String selection = TodoContract.TodoEntry._ID + " = ?";
        String[] selectionArgs = { tId.toString() };

        Cursor cursor = db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String title = "Todo Item";

        while (cursor.moveToNext()) {
            title = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_TITLE)
            );
        }
        cursor.close();
        todoTitle.setText(title);
    }
}
