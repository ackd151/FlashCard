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
import com.slack.flashcard.domain.model.Card;
import com.slack.flashcard.domain.repositories.CardRepository;
import com.slack.flashcard.domain.services.CardService;
import com.slack.flashcard.ui.activities.EditCard;

import java.util.List;

public class CardLVAdapter extends ArrayAdapter<Card> {

    private List<Card> cards;
    private Context context;

    public CardLVAdapter(Context context, int resource, List<Card> cards) {
        super(context, resource, cards);
        this.cards = cards;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.card_lv_card, null);
        }

        Card card = cards.get(position);

        TextView questionTV, editTV, deleteTV;
        questionTV = view.findViewById(R.id.card_lv_card_question_tv);
        editTV = view.findViewById(R.id.card_lv_card_edit_tv);
        deleteTV = view.findViewById(R.id.card_lv_card_delete_tv);

        questionTV.setText(card.getQuestion());
        editTV.setClickable(true);
        editTV.setOnClickListener(v -> editCard(card));
        deleteTV.setClickable(true);
        deleteTV.setOnClickListener(v -> deleteCard(card));

        return view;
    }

    private void editCard(Card card) {
        Intent editCardIntent = new Intent(context, EditCard.class);
        editCardIntent.putExtra("cardId", card.getId());
        context.startActivity(editCardIntent);
    }

    private void deleteCard(Card card) {
        createDeleteCardDialog(card).show();
    }

    // Build card delete alert dialog
    private AlertDialog createDeleteCardDialog(Card card) {
        return new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.alert_dialog_delete_card_title))
                .setMessage(context.getResources().getString(R.string.alert_dialog_delete_card_message))
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(context.getResources().getString(R.string.alert_dialog_confirm),
                        (dialogInterface, i) -> {
                            CardRepository cardRepository = new CardService(context);
                            cardRepository.deleteCard(card);
                            dialogInterface.dismiss();
                            cards.clear();
                            cards.addAll(cardRepository.getAllCardSetCards(Integer.parseInt(card.getCardSetId())));
                            notifyDataSetChanged();
                        })
                .setNegativeButton(context.getResources().getString(R.string.alert_dialog_cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }
}
