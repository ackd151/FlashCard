package com.slack.flashcard.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardService;
import com.slack.flashcard.domain.services.CardSetService;
import com.slack.flashcard.ui.adapters.CardLVAdapter;

public class CardSetOverview extends AppCompatActivity {

    // Logic Components
    private String cardSetId;
    private CardSet cardSet;
    private CardSetRepository cardSetRepository;
    private CardRepository cardRepository;
    private ArrayAdapter<Card> cardAdapter;

    // UI Components
    private Button viewCardsBtn, addCardBtn, deleteAllBtn;
    private TextView numCardsTV;
    private ListView cardLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_set_overview);

        // Set Logic components
        cardSetId = getIntent().getExtras().getString("cardSetId");
        cardSetRepository = new CardSetService(this);
        cardSet = cardSetRepository.getCardSet(Integer.parseInt(cardSetId));

        cardRepository = new CardService(this);

        cardAdapter = new CardLVAdapter(this, R.layout.card_lv_card, cardSet.getCardList());
        cardLV = findViewById(R.id.card_set_overview_card_lv);
        cardLV.setAdapter(cardAdapter);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.card_set_overview_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(cardSet.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Get handle on UI components
        numCardsTV = findViewById(R.id.card_set_overview_num_cards_value_tv);
        viewCardsBtn = findViewById(R.id.card_set_overview_view_cards_btn);
        addCardBtn = findViewById(R.id.card_set_overview_add_card_btn);
        deleteAllBtn = findViewById(R.id.card_set_overview_delete_all_btn);

        // Set listeners
        viewCardsBtn.setOnClickListener(view -> viewCards());
        addCardBtn.setOnClickListener(view -> addCard());
        deleteAllBtn.setOnClickListener(view -> deleteAllCards());

        // Make ListView items clickable and set listener/handler
//        cardLV.setClickable(true);
//        cardLV.setOnItemClickListener((adapterView, view, i, l) -> {
//            // start task sign off activity
//            Card c = (Card) cardLV.getItemAtPosition(i);
//            Intent cardSetOverviewActivity = new Intent(this, CardSetOverview.class);
//            cardSetOverviewActivity.putExtra("cardSetId", cs.getId());
//            startActivity(cardSetOverviewActivity);
//        });

        setComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setComponents();
    }

    // Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_set_overview_menu, menu);
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
            case R.id.toolbar_card_set_overview_add_card:
                addCard();
                return true;
            case R.id.toolbar_card_set_overview_delete_all:
                // Delete all Cards
                deleteAllCards();
                setComponents();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setComponents() {
        // Get fresh data
        cardSet = cardSetRepository.getCardSet(Integer.parseInt(cardSetId));
        cardAdapter.clear();
        cardAdapter.addAll(cardSet.getCardList());
        // Set ui components
        numCardsTV.setText(String.valueOf(cardAdapter.getCount()));
    }

    private void viewCards() {
        if (cardSet.getCardList().size() < 1) {
            Toast.makeText(this, R.string.toast_no_cards, Toast.LENGTH_LONG).show();
        } else {
            Intent viewCardsIntent = new Intent(this, ViewCards.class);
            viewCardsIntent.putExtra("cardSetId", cardSetId);
            startActivity(viewCardsIntent);
        }
    }

    private void addCard() {
        // Start new Card activity
        Intent intent = new Intent(this, NewCard.class);
        intent.putExtra("cardSetId", cardSetId);
        startActivity(intent);
    }

    private void deleteAllCards() {
        createDeleteAllCardsDialog().show();
    }

    // Build cardset delete alert dialog
    private AlertDialog createDeleteAllCardsDialog() {
        return new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.alert_dialog_delete_all_cards_title))
                .setMessage(getResources().getString(R.string.alert_dialog_delete_all_cards_message))
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(getResources().getString(R.string.alert_dialog_confirm),
                        (dialogInterface, i) -> {
                            for (Card cardToDelete : cardSet.getCardList()) {
                                cardRepository.deleteCard(cardToDelete);
                            }
                            setComponents();
                        })
                .setNegativeButton(getResources().getString(R.string.alert_dialog_cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }
}