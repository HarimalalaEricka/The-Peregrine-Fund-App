package com.example.theperegrinefund;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyItems;
    private OnHistoryItemClickListener clickListener;

    public interface OnHistoryItemClickListener {
        void onHistoryItemClick(HistoryItem item, int position);
    }

    public HistoryAdapter(List<HistoryItem> historyItems, OnHistoryItemClickListener clickListener) {
        this.historyItems = historyItems;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.history_item_text);
        }

        public void bind(HistoryItem item, int position) {
            // Utilisez getTitle() au lieu de getText()
            textView.setText(item.getTitle());

            if (item.isSelected()) {
                textView.setBackgroundResource(R.drawable.history_item_selected);
            } else {
                textView.setBackgroundResource(R.drawable.history_item_normal);
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onHistoryItemClick(item, position);
                }
            });
        }
    }
}