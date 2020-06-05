package com.example.assignment_1_study_app.ui.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.todo.TodoContract;
import com.example.assignment_1_study_app.database.todo.TodoDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Long> mTodoIds;
    private ArrayList<String> mTodoTitles;
    private ArrayList<Boolean> mTodoToggles;
    private Context mContext;
    private OnTodoListener mOnTodoListener;

    private TodoDbHelper dbHelper;

    public RecyclerViewAdapter(Context context, ArrayList todoIds, ArrayList todos, ArrayList todoToggles, OnTodoListener onTodoListener) {
        mTodoIds = todoIds;
        mTodoTitles = todos;
        mTodoToggles = todoToggles;
        mContext = context;
        mOnTodoListener = onTodoListener;
        dbHelper = new TodoDbHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_todos, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnTodoListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(mTodoTitles.get(position));
        holder.cbxDone.setChecked(mTodoToggles.get(position));
        holder.cbxDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_NAME_TICKED, holder.cbxDone.isChecked() ? 1 : 0);

                String selection = TodoContract.TodoEntry._ID + " = ?";
                String[] selectionArgs = { Long.toString(mTodoIds.get(position)) };

                int count = db.update(
                        TodoContract.TodoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                // update local too
                mTodoToggles.set(position, holder.cbxDone.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodoTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        CheckBox cbxDone;
        RelativeLayout parentLayout;
        OnTodoListener onTodoListener;

        public ViewHolder(View itemView, OnTodoListener onTodoListener) {
            super(itemView);
            title = itemView.findViewById(R.id.listitem_todos_title);
            cbxDone = itemView.findViewById(R.id.cbxTodo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onTodoListener = onTodoListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTodoListener.onTodoClick(v, getAdapterPosition());
        }
    }

    public interface OnTodoListener {
        void onTodoClick(View view, int position);
    }
}
