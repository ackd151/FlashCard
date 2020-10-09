package com.slack.flashcard.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.services.CardService;

public class EditCard extends AppCompatActivity {

    // Logic components
    private String cardId;
    private Card card;
    private CardRepository cardRepository;

    // UI components
    EditText questionTV, answerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.edit_card_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Edit Card");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cardId = getIntent().getStringExtra("cardId");
        cardRepository = new CardService(this);
        card = cardRepository.getCard(Integer.parseInt(cardId));

        questionTV = findViewById(R.id.edit_card_question_tv);
        questionTV.setText(card.getQuestion());
        questionTV.setSelectAllOnFocus(true);
        answerTV = findViewById(R.id.edit_card_answer_tv);
        answerTV.setText(card.getAnswer());
        answerTV.setSelectAllOnFocus(true);

    }

    // Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_card_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set toolbar icon actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Back button - return to previous activity
                finish();
                return true;
            case R.id.toolbar_edit_card_save:
                // Save title to db
                if (validateEditText(questionTV) && validateEditText(answerTV)) {
                    card.setQuestion(questionTV.getText().toString());
                    card.setAnswer(answerTV.getText().toString());
                    cardRepository.updateCard(card);
                    finish();
                }
                return true;
            case R.id.toolbar_edit_card_clear:
                // Clear title EditText
                questionTV.setText("");
                answerTV.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper method to ensure edittext field is not empty
    private boolean validateEditText(EditText editText) {
        if (editText.getText().toString().length() < 1) {
            editText.setError(getResources().getString(R.string.validation_entry_required));
            editText.requestFocus();
            return false;
        }
        return true;
    }
}