package com.slack.flashcard.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardSetService;

public class NewCardSet extends AppCompatActivity {

    // UI components
    EditText cardSetTitleET;
    Button createCardSetButtonBTN;

    // Logic components
    CardSetRepository cardSetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_set);

        cardSetRepository = new CardSetService(this);

        cardSetTitleET = findViewById(R.id.cardset_title_et);
        createCardSetButtonBTN = findViewById(R.id.cardset_create_btn);

        createCardSetButtonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate editText fields before calling createProfile - not empty only requirement
                if (editTextNotEmpty(cardSetTitleET)) {
                    createCardSet(view);
                    finish();
                }
            }
        });
    }

    private void createCardSet(View view) {
        String cardSetTitle = cardSetTitleET.getText().toString();
        cardSetRepository.addCardSet(new CardSet(cardSetTitle));
    }

    // Helper method to validate user input in edittext fields
    private boolean editTextNotEmpty(EditText editText) {
        String editTextValue = editText.getText().toString();
        if (TextUtils.isEmpty(editTextValue)) {
            editText.setError(getResources().getString(R.string.validation_entry_required));
            editText.requestFocus();
            return false;
        }
        return true;
    }
}