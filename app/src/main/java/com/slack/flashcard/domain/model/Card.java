package com.slack.flashcard.domain.model;

public class Card {

    private String id;
    private String cardSetId;
    private String question;
    private String answer;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public Card(String cardSetId, String question, String answer) {
        this.cardSetId = cardSetId;
        this.question = question;
        this.answer = answer;
    }

    public Card(String id, String cardSetId, String question, String answer) {
        this.id = id;
        this.cardSetId = cardSetId;
        this.question = question;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardSetId() {
        return cardSetId;
    }

    public void setCardSetId(String cardSetId) {
        this.cardSetId = cardSetId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
