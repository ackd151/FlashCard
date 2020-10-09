package com.slack.flashcard.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardSetService;

public class EditCardSet extends AppCompatActivity {

    // Logic Components
    private CardSetRepository cardSetRepository;
    private CardSet cardSet;
    private String cardSetId;

    // UI Components
    private EditText cardSetTitleValueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card_set);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.edit_card_set_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Edit CardSet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cardSetId = getIntent().getExtras().getString("cardSetId");
        cardSetRepository = new CardSetService(this);
        cardSet = cardSetRepository.getCardSet(Integer.parseInt(cardSetId));

        cardSetTitleValueTV = findViewById(R.id.edit_card_set_title_value_et);
        cardSetTitleValueTV.setText(cardSet.getTitle());
        cardSetTitleValueTV.setSelectAllOnFocus(true);
    }

    // Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_card_set_menu, menu);
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
            case R.id.toolbar_edit_card_set_save:
                // Save title to db
                if (validateEditText(cardSetTitleValueTV)) {
                    cardSet.setTitle(cardSetTitleValueTV.getText().toString());
                    cardSetRepository.updateCardSet(cardSet);
                    finish();
                }
                return true;
            case R.id.toolbar_edit_card_set_clear:
                // Clear title EditText
                cardSetTitleValueTV.setText("");
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