package com.slack.flashcard.domain.model;

import java.util.List;

public class CardSet {

    private String id;
    private String title;
    private List<Card> cardList;

    public CardSet(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public CardSet(String id, String title, List<Card> cardList) {
        this.id = id;
        this.title = title;
        this.cardList = cardList;
    }

    public CardSet(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}
