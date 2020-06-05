package com.example.assignment_1_study_app.ui.flashcards;

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

public class DeckRecyclerViewAdapter extends RecyclerView.Adapter<DeckRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mTitles = new ArrayList<>();
    private Context mContext;
    private OnDeckListener mOnDeckListener;

    public DeckRecyclerViewAdapter(Context context, ArrayList<String> titles, OnDeckListener onDeckListener) {
        mTitles = titles;
        mContext = context;
        mOnDeckListener = onDeckListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_decks, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnDeckListener);
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
        OnDeckListener onDeckListener;

        public ViewHolder(View itemView, OnDeckListener onDeckListener) {
            super(itemView);
            title = itemView.findViewById(R.id.listitem_decks_title);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onDeckListener = onDeckListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDeckListener.onDeckClick(v, getAdapterPosition());
        }
    }

    public interface OnDeckListener {
        void onDeckClick(View view, int position);
    }
}
