package com.example.assignment_1_study_app.ui.todo;

import android.content.ContentValues;
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
import com.example.assignment_1_study_app.database.todo.TodoContract;
import com.example.assignment_1_study_app.database.todo.TodoDbHelper;
import com.example.assignment_1_study_app.ui.todo.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class TodoFragment extends Fragment implements RecyclerViewAdapter.OnTodoListener {

    ArrayList<Long> mTodoIds;
    ArrayList<String> mTodos;
    ArrayList<Boolean> mTodoToggles;

    FloatingActionButton fabAddTodo;

    TodoDbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        dbHelper = new TodoDbHelper(getContext());

        fabAddTodo = root.findViewById(R.id.fabAddTodo);

        getTodoValues();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_todo);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), mTodoIds, mTodos, mTodoToggles, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_NAME_TITLE, "New Todo");
                values.put(TodoContract.TodoEntry.COLUMN_NAME_TICKED, 0);

                long newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values);

                NavController controller = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("id", newRowId);
                controller.navigate(R.id.nav_edit_todo, args);
            }
        });

        return root;
    }

    public void getTodoValues() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        mTodoIds = new ArrayList<>();
        mTodos = new ArrayList<>();
        mTodoToggles = new ArrayList<>();

        String[] projection = {
                BaseColumns._ID,
                TodoContract.TodoEntry.COLUMN_NAME_TITLE,
                TodoContract.TodoEntry.COLUMN_NAME_TICKED
        };

        String orderBy = TodoContract.TodoEntry.COLUMN_NAME_TICKED + " ASC";

        Cursor cursor = db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                orderBy
        );

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TodoContract.TodoEntry._ID)
            );
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_TITLE)
            );
            Integer toggle = cursor.getInt(
                    cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_TICKED)
            );
            mTodoIds.add(id);
            mTodos.add(name);
            if (toggle == 1)
                mTodoToggles.add(true);
            else
                mTodoToggles.add(false);
        }
        cursor.close();
    }

    @Override
    public void onTodoClick(View view, int position) {
        NavController controller = Navigation.findNavController(view);
        Bundle args = new Bundle();
        args.putLong("id", mTodoIds.get(position));
        controller.navigate(R.id.nav_edit_todo, args);
    }
}
