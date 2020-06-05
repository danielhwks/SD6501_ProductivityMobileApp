package com.example.assignment_1_study_app.ui.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class FlashCardsDeckEditFragment extends Fragment implements CardRecyclerViewAdapter.OnCardListener {

    EditText editTitle;
    Button btnDeleteDeck;
    Button btnSaveDeck;
    FloatingActionButton fabAddCard;
    FloatingActionButton fabPlayDeck;

    Long dId;

    ArrayList<Long> mCardsIds;
    ArrayList<String> mCards;

    FlashCardsDbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_flashcards_cardsview, container, false);

        dbHelper = new FlashCardsDbHelper(getContext());

        editTitle = root.findViewById(R.id.deckTitle);
        btnSaveDeck = root.findViewById(R.id.btnSaveDeck);
        btnDeleteDeck = root.findViewById(R.id.btnDeleteDeck);
        fabAddCard = root.findViewById(R.id.fabAddCard);
        fabPlayDeck = root.findViewById(R.id.fabPlayDeck);

        Bundle args = getArguments();
        dId = args.getLong("id");

        mCardsIds = new ArrayList<>();
        mCards = new ArrayList<String>();
        fetchCards();

        editTitle.setText(getTitleById(dId));

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_flashcards_card);
        CardRecyclerViewAdapter adapter = new CardRecyclerViewAdapter(getActivity().getApplicationContext(), mCards, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        fabPlayDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCards.size() <= 0) {
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = "No cards to play";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                NavController controller = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("id", dId);
                controller.navigate(R.id.nav_play_deck, args);
            }
        });

        fabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(FlashCardsContract.CardEntry.COLUMN_NAME_DECK, dId);
                values.put(FlashCardsContract.CardEntry.COLUMN_NAME_FRONT, "Front Face");
                values.put(FlashCardsContract.CardEntry.COLUMN_NAME_BACK, "Back Face");

                long newRowId = db.insert(FlashCardsContract.CardEntry.TABLE_NAME, null, values);

                NavController controller = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("id", newRowId);
                controller.navigate(R.id.nav_edit_card, args);
            }
        });

        btnSaveDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(FlashCardsContract.DeckEntry.COLUMN_NAME_NAME, editTitle.getText().toString());

                String selection = FlashCardsContract.DeckEntry._ID + " = ?";
                String[] selectionArgs = { dId.toString() };

                int count = db.update(
                        FlashCardsContract.DeckEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        btnDeleteDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Delete cards first
                String selection = FlashCardsContract.CardEntry.COLUMN_NAME_DECK + " = ?";
                String[] selectionArgs = { dId.toString() };

                db.delete(FlashCardsContract.CardEntry.TABLE_NAME, selection, selectionArgs);

                selection = FlashCardsContract.DeckEntry._ID + " = ?";
                selectionArgs[0] = dId.toString();

                db.delete(FlashCardsContract.DeckEntry.TABLE_NAME, selection, selectionArgs);

                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.nav_decks);
            }
        });

        return root;
    }

    private String getTitleById(Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FlashCardsContract.DeckEntry.COLUMN_NAME_NAME
        };

        String selection = FlashCardsContract.DeckEntry._ID + " = ?";
        String[] selectionArgs = { dId.toString() };

        Cursor cursor = db.query(
                FlashCardsContract.DeckEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String title = "New Deck";

        while (cursor.moveToNext()) {
            title = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.DeckEntry.COLUMN_NAME_NAME)
            );
        }
        cursor.close();

        return title;
    }

    private void fetchCards() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FlashCardsContract.CardEntry.COLUMN_NAME_FRONT
        };

        String selection = FlashCardsContract.CardEntry.COLUMN_NAME_DECK + " = ?";
        String[] selectionArgs = { dId.toString() };

        Cursor cursor = db.query(
                FlashCardsContract.CardEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.CardEntry._ID)
            );
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.CardEntry.COLUMN_NAME_FRONT)
            );
            mCardsIds.add(id);
            mCards.add(name);
        }
        cursor.close();
    }

    @Override
    public void onCardClick(View view, int position) {
        NavController controller = Navigation.findNavController(view);
        Bundle args = new Bundle();
        args.putLong("id", mCardsIds.get(position));
        controller.navigate(R.id.nav_edit_card, args);
    }
}
