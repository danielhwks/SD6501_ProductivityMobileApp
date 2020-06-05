package com.example.assignment_1_study_app.ui.flashcards;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.assignment_1_study_app.R;
import com.example.assignment_1_study_app.database.flashcards.FlashCardsContract;
import com.example.assignment_1_study_app.database.flashcards.FlashCardsDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class FlashCardsDeckPlayFragment extends Fragment {

    Button btnToggleCard;
    TextView txtFaceInfo;
    TextView txtCardPosition;
    FloatingActionButton btnCardPrev;
    FloatingActionButton btnCardNext;

    Long dId;
    ArrayList<Long> cardIds;
    Integer currentCardId;
    String currentFront;
    String currentBack;
    Boolean showFront;

    FlashCardsDbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_play_deck, container, false);

        btnToggleCard = root.findViewById(R.id.btnToggleCard);
        txtFaceInfo = root.findViewById(R.id.txtFaceInfo);
        txtCardPosition = root.findViewById(R.id.txtCardPosition);
        btnCardPrev = root.findViewById(R.id.btnCardPrev);
        btnCardNext = root.findViewById(R.id.btnCardNext);

        cardIds = new ArrayList<>();
        currentCardId = 0;
        currentFront = "";
        currentBack = "";
        showFront = true;

        dbHelper = new FlashCardsDbHelper(getContext());

        Bundle args = getArguments();
        dId = args.getLong("id");

        fetchCardIds();

        updateCard();

        btnToggleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showFront) {
                    showFront = false;
                } else {
                    showFront = true;
                }
                updateCard();
            }
        });

        btnCardPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCard(-1);
            }
        });

        btnCardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCard(1);
            }
        });

        return root;
    }

    private void fetchCardIds() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FlashCardsContract.CardEntry._ID
        };

        String selection = FlashCardsContract.CardEntry.COLUMN_NAME_DECK + " = ?";
        String[] selectionArgs = { dId.toString() };

        System.out.println(selection);
        System.out.println(dId.toString());

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
            cardIds.add(id);
        }
        cursor.close();

        // put the cards in a random order for review
        Collections.shuffle(cardIds);
    }

    private void getCardById(Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FlashCardsContract.CardEntry.COLUMN_NAME_FRONT,
                FlashCardsContract.CardEntry.COLUMN_NAME_BACK
        };

        String selection = FlashCardsContract.CardEntry._ID + " = ?";
        String[] selectionArgs = { id.toString() };

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
            currentFront = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.CardEntry.COLUMN_NAME_FRONT)
            );
            currentBack = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.CardEntry.COLUMN_NAME_BACK)
            );
        }
        cursor.close();
    }

    private void updateCard() {
        getCardById(cardIds.get(currentCardId));
        if (showFront) {
            btnToggleCard.setText(currentFront);
            txtFaceInfo.setText("Front");
        } else {
            btnToggleCard.setText(currentBack);
            txtFaceInfo.setText("Back");
        }

        txtCardPosition.setText((currentCardId + 1) + "/" + cardIds.size());
    }

    private void changeCard(Integer direction) {
        Integer newPosition = currentCardId + direction;
        if (newPosition < 0 || newPosition >= cardIds.size()) {
            return;
        }
        currentCardId = newPosition;
        showFront = true;
        updateCard();
    }
}