package com.slack.flashcard.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardService;
import com.slack.flashcard.domain.services.CardSetService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewCards extends AppCompatActivity {

    // Logic Components
    private String cardSetId;
    private CardSet cardSet;
    private Card activeCard;
    private int activeIndex = -1;
    private List<Card> cards;
    private boolean answerShown;
    private CardSetRepository cardSetRepository;
    private CardRepository cardRepository;

    // UI Components
    private TextView viewCardsContentTV, viewCardsQATV;
    private Button viewCardsFlipBtn, viewCardsPrevBtn, viewCardsNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cards);

        // Set logic components
        cardSetId = getIntent().getExtras().getString("cardSetId");
        cardSetRepository = new CardSetService(this);
        cardRepository = new CardService(this);
        cardSet = cardSetRepository.getCardSet(Integer.parseInt(cardSetId));
        // Clone cards
        cards = new ArrayList<>(cardSet.getCardList());

        // Set ui components
        viewCardsFlipBtn = findViewById(R.id.view_cards_flip_btn);
        viewCardsFlipBtn.setOnClickListener(view -> flipCard());
        viewCardsPrevBtn = findViewById(R.id.view_cards_prev_btn);
        viewCardsPrevBtn.setOnClickListener(view -> showCard(-1));
        viewCardsNextBtn = findViewById(R.id.view_cards_next_btn);
        viewCardsNextBtn.setOnClickListener(view -> showCard(1));
        viewCardsContentTV = findViewById(R.id.view_cards_content_tv);
        viewCardsQATV = findViewById(R.id.view_cards_q_a_tv);
        // Shuffle cardSet list
        Collections.shuffle(cards);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.view_cards_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(cardSet.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Show first card
        showCard(1);
    }

    // Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_cards_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set toolbar menu icon actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Back button - return to previous activity
                finish();
                return true;
            case R.id.toolbar_view_cards_add_card:
                // Add card to cardSet
                Intent newCardIntent = new Intent(this, NewCard.class);
                newCardIntent.putExtra("cardSetId", cardSet.getId());
                startActivityForResult(newCardIntent, 1);
                return true;
            case R.id.toolbar_view_cards_delete_card:
                // Delete card from cardSet
                createDeleteCardDialog(activeCard).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String cardId = data.getStringExtra("cardId");
                cards.add(cardRepository.getCard(Integer.parseInt(cardId)));
            }
        }
    }

    private void showCard(int incDecIndex) {
        // End if no cards in collection
        if (cards.size() == 0) {
            finish();
            return;
        }
        answerShown = false;
        viewCardsQATV.setText("Question");
        activeIndex += incDecIndex;
        // Set index when skipping past end or front of array
        activeIndex = activeIndex < 0 ? (cards.size() - 1) : activeIndex > (cards.size() - 1) ? 0 : activeIndex;
        activeCard = cards.get(activeIndex);
        viewCardsContentTV.setText(activeCard.getQuestion());
    }

    private void flipCard() {
        answerShown = !answerShown;
        viewCardsQATV.setText(answerShown ? "Answer" : "Question");
        viewCardsContentTV.setText(answerShown ? activeCard.getAnswer() : activeCard.getQuestion());
    }

    // Build card delete alert dialog
    private AlertDialog createDeleteCardDialog(Card card) {
        return new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.alert_dialog_delete_card_title))
                .setMessage(getResources().getString(R.string.alert_dialog_delete_card_message))
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(getResources().getString(R.string.alert_dialog_confirm),
                        (dialogInterface, i) -> {
                            cardRepository.deleteCard(card);
                            cards.remove(card);
                            dialogInterface.dismiss();
                            showCard(1);
                        })
                .setNegativeButton(getResources().getString(R.string.alert_dialog_cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }
}