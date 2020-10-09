package com.slack.flashcard.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.slack.flashcard.R;
import com.slack.flashcard.bootstrap.SeedDB;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardSetService;
import com.slack.flashcard.ui.adapters.CardSetLVAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Logic components
    private List<CardSet> cardSets;
    private ArrayAdapter<CardSet> cardSetArrayAdapter;
    private CardSetRepository cardSetRepository;

    // UI components
    private ListView cardSetsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.toolbar_main_title);

        ///////////////
        SeedDB seedDB = new SeedDB(this);

        // Set Logic components
        cardSetRepository = new CardSetService(this);
        cardSets = cardSetRepository.getAllCardSets();

        cardSetArrayAdapter = new CardSetLVAdapter(this, R.layout.cardset_lv_card, cardSets);
        cardSetsLV = findViewById(R.id.card_set_lv);
        cardSetsLV.setAdapter(cardSetArrayAdapter);

        // Make ListView items clickable and set listener/handler
        cardSetsLV.setClickable(true);
        cardSetsLV.setOnItemClickListener((adapterView, view, i, l) -> {
            // start task sign off activity
            CardSet cs = (CardSet) cardSetsLV.getItemAtPosition(i);
            Intent cardSetOverviewActivity = new Intent(this, CardSetOverview.class);
            cardSetOverviewActivity.putExtra("cardSetId", cs.getId());
            startActivity(cardSetOverviewActivity);
        });
    }

    // Inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Set toolbar menu icon actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_main_add_cardset:
                // Start new CardSet activity
                Intent intent = new Intent(this, NewCardSet.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cardSetArrayAdapter
        cardSets = cardSetRepository.getAllCardSets();
        cardSetArrayAdapter.clear();
        cardSetArrayAdapter.addAll(cardSets);
    }
}