package com.slack.flashcard.domain.repositories;

import com.slack.flashcard.domain.model.Card;

import java.util.List;

public interface CardRepository {

    long addCard(Card card);

    Card getCard(int id);

    List<Card> getAllCardSetCards(int cardSetId);

    long updateCard(Card card);

    void deleteCard(Card card);

}
