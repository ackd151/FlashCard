package com.slack.flashcard.domain.repositories;

import com.slack.flashcard.domain.model.CardSet;

import java.util.List;

public interface CardSetRepository {

    long addCardSet(CardSet cardSet);

    CardSet getCardSet(int id);

    List<CardSet> getAllCardSets();

    long updateCardSet(CardSet cardSet);

    void deleteCardSet(CardSet cardSet);

}
