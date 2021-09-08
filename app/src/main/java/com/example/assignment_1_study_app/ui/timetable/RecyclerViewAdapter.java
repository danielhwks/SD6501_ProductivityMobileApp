package com.example.assignment_1_study_app.ui.timetable;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.timetable.TimetableDbHelper;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TimetableSession> mTimetables;
    private Context mContext;
    private OnTimetableListener mOnTimetableListener;

    private TimetableDbHelper dbHelper;

    public RecyclerViewAdapter(Context context, ArrayList timetables, OnTimetableListener onTimetableListener) {
        mTimetables = timetables;
        mContext = context;
        mOnTimetableListener = onTimetableListener;
        dbHelper = new TimetableDbHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_timetables, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnTimetableListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(mTimetables.get(position).getTitle());
        holder.time.setText(mTimetables.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mTimetables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView time;
        RelativeLayout parentLayout;
        OnTimetableListener onTimetableListener;

        public ViewHolder(View itemView, OnTimetableListener onTimetableListener) {
            super(itemView);
            title = itemView.findViewById(R.id.listitem_timetables_title);
            time = itemView.findViewById(R.id.listitem_timetables_time);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onTimetableListener = onTimetableListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTimetableListener.onTimetableClick(v, getAdapterPosition());
        }
    }

    public interface OnTimetableListener {
        void onTimetableClick(View view, int position);
    }
}
