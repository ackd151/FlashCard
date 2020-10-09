package com.slack.flashcard.domain.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.slack.flashcard.domain.db.DatabaseHelper;
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.repositories.CardRepository;

import java.util.ArrayList;
import java.util.List;

import static com.slack.flashcard.domain.db.DatabaseHelper.ANSWER;
import static com.slack.flashcard.domain.db.DatabaseHelper.CARDSET_ID;
import static com.slack.flashcard.domain.db.DatabaseHelper.CARD_TABLE_NAME;
import static com.slack.flashcard.domain.db.DatabaseHelper.QUESTION;
import static com.slack.flashcard.domain.db.DatabaseHelper._ID;

public class CardService implements CardRepository {

    private DatabaseHelper dbHelper;
    private Context context;

    public CardService(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        this.context = context;
    }

    @Override
    public long addCard(Card card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CARDSET_ID, card.getCardSetId());
        contentValues.put(QUESTION, card.getQuestion());
        contentValues.put(ANSWER, card.getAnswer());

        long cardId = db.insert(CARD_TABLE_NAME, null, contentValues);
        db.close();
        return cardId;
    }

    @Override
    public Card getCard(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(CARD_TABLE_NAME, new String[]{_ID, CARDSET_ID, QUESTION,
                        ANSWER}, _ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        Card card = new Card(c.getString(0), c.getString(1), c.getString(2),
                c.getString(3));
        db.close();
        return card;
    }

    @Override
    public List<Card> getAllCardSetCards(int cardSetId) {
        List<Card> cards = new ArrayList<>();
        // Select ALL query
        String selectQuery = "SELECT * FROM " + CARD_TABLE_NAME
                + " WHERE " + CARDSET_ID + " = ?";

        // Get db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(cardSetId)});
        // Loop through all rows and add tasks to list
        if (c.moveToFirst()) {
            do {
                cards.add(new Card(c.getString(0), c.getString(1), c.getString(2),
                        c.getString(3)));
            } while (c.moveToNext());
        }
        db.close();
        return cards;
    }

    @Override
    public long updateCard(Card card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CARDSET_ID, card.getCardSetId());
        contentValues.put(QUESTION, card.getQuestion());
        contentValues.put(ANSWER, card.getAnswer());

        long numRowsUpdated = db.update(CARD_TABLE_NAME, contentValues, _ID + "= ?",
                new String[]{ card.getId() });
        db.close();
        return numRowsUpdated;
    }

    @Override
    public void deleteCard(Card card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CARD_TABLE_NAME, _ID + "= ?", new String[] { card.getId() });
        db.close();
    }
}
