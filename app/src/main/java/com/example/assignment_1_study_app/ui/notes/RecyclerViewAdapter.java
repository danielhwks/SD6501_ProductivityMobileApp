package com.example.assignment_1_study_app.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_1_study_app.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mTitles = new ArrayList<>();
    private Context mContext;
    private OnNoteListener mOnNoteListener;

    public RecyclerViewAdapter(Context context, ArrayList<String> titles, OnNoteListener onNoteListener) {
        mTitles = titles;
        mContext = context;
        mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_notes, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        RelativeLayout parentLayout;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.listitem_notes_title);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(v, getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(View view, int position);
    }
}
