package com.example.assignment_1_study_app.ui.flashcards;

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
import com.example.assignment_1_study_app.database.flashcards.FlashCardsContract;
import com.example.assignment_1_study_app.database.flashcards.FlashCardsDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FlashCardsFragment extends Fragment implements DeckRecyclerViewAdapter.OnDeckListener {

    private ArrayList<String> mDecks;
    private ArrayList<Long> mDecksIds;
    FlashCardsDbHelper dbHelper;

    FloatingActionButton fabAddDeck;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_flashcards, container, false);

        dbHelper = new FlashCardsDbHelper(getContext());

        fabAddDeck = root.findViewById(R.id.fabAddDeck);

        fetchDecks();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_flashcards_deck);
        DeckRecyclerViewAdapter adapter = new DeckRecyclerViewAdapter(getActivity().getApplicationContext(), mDecks, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        fabAddDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(FlashCardsContract.DeckEntry.COLUMN_NAME_NAME, "New Deck");

                long newRowId = db.insert(FlashCardsContract.DeckEntry.TABLE_NAME, null, values);

                NavController controller = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("id", newRowId);
                controller.navigate(R.id.nav_edit_deck, args);
            }
        });

        return root;
    }

    private void fetchDecks() {
        mDecks = new ArrayList<>();
        mDecksIds = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FlashCardsContract.DeckEntry.COLUMN_NAME_NAME
        };

        Cursor cursor = db.query(
                FlashCardsContract.DeckEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.DeckEntry._ID)
            );
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.DeckEntry.COLUMN_NAME_NAME)
            );
            mDecksIds.add(id);
            mDecks.add(name);
        }
        cursor.close();
    }

    @Override
    public void onDeckClick(View view, int position) {
        NavController controller = Navigation.findNavController(view);
        Bundle args = new Bundle();
        args.putLong("id", mDecksIds.get(position));
        controller.navigate(R.id.nav_edit_deck, args);
    }
}
