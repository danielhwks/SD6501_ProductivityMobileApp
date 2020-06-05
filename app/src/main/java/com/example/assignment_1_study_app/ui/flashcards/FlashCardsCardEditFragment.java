package com.example.assignment_1_study_app.ui.flashcards;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.assignment_1_study_app.database.flashcards.FlashCardsContract;
import com.example.assignment_1_study_app.database.flashcards.FlashCardsDbHelper;

public class FlashCardsCardEditFragment extends Fragment {

    EditText editFront;
    EditText editBack;

    Button btnSave;
    Button btnDelete;

    Long cId;
    String frontFace;
    String backFace;

    FlashCardsDbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_card, container, false);

        dbHelper = new FlashCardsDbHelper(getContext());

        Bundle args = getArguments();
        cId = args.getLong("id");

        loadCardById();

        editFront = root.findViewById(R.id.card_edit_front);
        editBack = root.findViewById(R.id.card_edit_back);
        btnSave = root.findViewById(R.id.btnSave);
        btnDelete = root.findViewById(R.id.btnDelete);

        editFront.setText(frontFace);
        editBack.setText(backFace);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(FlashCardsContract.CardEntry.COLUMN_NAME_FRONT, editFront.getText().toString());
                values.put(FlashCardsContract.CardEntry.COLUMN_NAME_BACK, editBack.getText().toString());

                String selection = FlashCardsContract.CardEntry._ID + " = ?";
                String[] selectionArgs = { cId.toString() };

                int count = db.update(
                        FlashCardsContract.CardEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String selection = FlashCardsContract.CardEntry._ID + " = ?";
                String[] selectionArgs = { cId.toString() };

                int deletedRows = db.delete(FlashCardsContract.CardEntry.TABLE_NAME, selection, selectionArgs);

                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        return root;
    }

    private void loadCardById() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FlashCardsContract.CardEntry.COLUMN_NAME_FRONT,
                FlashCardsContract.CardEntry.COLUMN_NAME_BACK
        };

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { cId.toString() };

        Cursor cursor = db.query(
                FlashCardsContract.CardEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String front = "FF";
        String back = "BF";

        while (cursor.moveToNext()) {
            front = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.CardEntry.COLUMN_NAME_FRONT)
            );
            back = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashCardsContract.CardEntry.COLUMN_NAME_BACK)
            );
        }
        cursor.close();

        frontFace = front;
        backFace = back;
    }
}
