package com.slack.flashcard.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.services.CardService;

public class NewCard extends AppCompatActivity {

    // Logic Components
    private String cardSetId;
    private CardRepository cardRepository;

    // UI Components
    private EditText questionValueET, answerValueET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        // Set logic components
        cardSetId = getIntent().getExtras().getString("cardSetId");
        cardRepository = new CardService(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.new_card_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Add Card");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set ui components
        questionValueET = findViewById(R.id.question_value_et);
        answerValueET = findViewById(R.id.answer_value_et);
    }

    // Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_card_menu, menu);
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
            case R.id.toolbar_new_card_save:
                // Save card to db
                long cardId = cardRepository.addCard(new Card(cardSetId, questionValueET.getText().toString(),
                        answerValueET.getText().toString()));
                Intent returnIntent = new Intent();
                returnIntent.putExtra("cardId", String.valueOf(cardId));
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            case R.id.toolbar_new_card_clear:
                // Clear card EditTexts
                questionValueET.setText("");
                answerValueET.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}