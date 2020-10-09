package com.slack.flashcard.domain.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.slack.flashcard.domain.db.DatabaseHelper;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.repositories.CardSetRepository;

import java.util.ArrayList;
import java.util.List;

import static com.slack.flashcard.domain.db.DatabaseHelper.CARDSET_TABLE_NAME;
import static com.slack.flashcard.domain.db.DatabaseHelper.TITLE;
import static com.slack.flashcard.domain.db.DatabaseHelper._ID;

public class CardSetService implements CardSetRepository {

    private DatabaseHelper dbHelper;
    private Context context;

    public CardSetService(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        this.context = context;
    }


    @Override
    public long addCardSet(CardSet cardSet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, cardSet.getTitle());

        long cardSetId = db.insert(CARDSET_TABLE_NAME, null, contentValues);
        db.close();
        return cardSetId;
    }

    @Override
    public CardSet getCardSet(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(CARDSET_TABLE_NAME, new String[]{_ID, TITLE}, _ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        CardSet cardSet = new CardSet(c.getString(0), c.getString(1));
        CardRepository cardRepository = new CardService(context);
        cardSet.setCardList(cardRepository.getAllCardSetCards(Integer.parseInt(cardSet.getId())));
        db.close();
        return cardSet;
    }

    @Override
    public List<CardSet> getAllCardSets() {
        List<CardSet> cardSets = new ArrayList<>();

        // Select ALL query
        String selectAllQuery = "SELECT * FROM " + CARDSET_TABLE_NAME;

        // Get db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try(Cursor cursor = db.rawQuery(selectAllQuery, null)) {
            // Loop through all rows and add to list
            if (cursor.moveToFirst()) {
                do {
                    cardSets.add(new CardSet(cursor.getString(0), cursor.getString(1)));
                } while (cursor.moveToNext());
            }
        }
        CardRepository cardRepository = new CardService(context);
        for (CardSet cardSet : cardSets) {
            cardSet.setCardList(cardRepository.getAllCardSetCards(Integer.parseInt(cardSet.getId())));
        }
        db.close();
        return cardSets;
    }

    @Override
    public long updateCardSet(CardSet cardSet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, cardSet.getTitle());

        long numRowsUpdated = db.update(CARDSET_TABLE_NAME, contentValues, _ID + "= ?",
                new String[]{ cardSet.getId() });
        db.close();
        return numRowsUpdated;
    }

    @Override
    public void deleteCardSet(CardSet cardSet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CARDSET_TABLE_NAME, _ID + "= ?", new String[] { cardSet.getId() });
        db.close();
    }
}
