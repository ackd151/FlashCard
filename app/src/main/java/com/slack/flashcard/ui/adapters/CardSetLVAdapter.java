package com.slack.flashcard.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.slack.flashcard.R;
import com.slack.flashcard.domain.model.CardSet;
import com.slack.flashcard.domain.repositories.CardSetRepository;
import com.slack.flashcard.domain.services.CardSetService;
import com.slack.flashcard.ui.activities.EditCardSet;

import java.util.List;

public class CardSetLVAdapter extends ArrayAdapter<CardSet> {

    private List<CardSet> cardSets;
    private Context context;

    public CardSetLVAdapter(Context context, int resource, List<CardSet> cardSets) {
        super(context, resource, cardSets);
        this.context = context;
        this.cardSets = cardSets;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.cardset_lv_card, null);
        }

        CardSet cardSet = cardSets.get(position);

        TextView title = view.findViewById(R.id.setTitleTV);
        TextView qty = view.findViewById(R.id.cardQuantityTV);

        title.setText(cardSet.getTitle());
        qty.setText(String.valueOf(cardSet.getCardList().size()));

        TextView editCardSetTV = view.findViewById(R.id.editSetTV);
        editCardSetTV.setClickable(true);
        editCardSetTV.setOnClickListener(v -> {
            Intent editCardSetIntent = new Intent(context, EditCardSet.class);
            editCardSetIntent.putExtra("cardSetId", cardSet.getId());
            context.startActivity(editCardSetIntent);
        });

        TextView deleteCardSetTV = view.findViewById(R.id.deleteSetTV);
        deleteCardSetTV.setClickable(true);
        deleteCardSetTV.setOnClickListener(v -> createDeleteCardSetDialog(cardSet).show());

        return view;
    }

    // Build cardset delete alert dialog
    private AlertDialog createDeleteCardSetDialog(CardSet cardSet) {
        return new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.alert_dialog_delete_cardset_title))
                .setMessage(context.getResources().getString(R.string.alert_dialog_delete_cardset_message))
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(context.getResources().getString(R.string.alert_dialog_confirm),
                        (dialogInterface, i) -> {
                            CardSetRepository cardSetRepository = new CardSetService(context);
                            cardSetRepository.deleteCardSet(cardSet);
                            dialogInterface.dismiss();
                            cardSets.clear();
                            cardSets.addAll(cardSetRepository.getAllCardSets());
                            notifyDataSetChanged();
                        })
                .setNegativeButton(context.getResources().getString(R.string.alert_dialog_cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }

}
