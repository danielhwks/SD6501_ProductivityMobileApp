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

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mTitles = new ArrayList<>();
    private Context mContext;
    private OnCardListener mOnCardListener;

    public CardRecyclerViewAdapter(Context context, ArrayList<String> titles, OnCardListener onCardListener) {
        mTitles = titles;
        mContext = context;
        mOnCardListener = onCardListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_cards, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnCardListener);
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
        OnCardListener onCardListener;

        public ViewHolder(View itemView, OnCardListener onCardListener) {
            super(itemView);
            title = itemView.findViewById(R.id.listitem_cards_title);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(v, getAdapterPosition());
        }
    }

    public interface OnCardListener {
        void onCardClick(View view, int position);
    }
}
