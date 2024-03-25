package com.example.notebookandroidproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebookandroidproject.R;
import com.example.notebookandroidproject.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final ItemAdapterInterface itemAdapterInterface;
    private final Context context;
    private final List<Item> itemList;

    public ItemAdapter(ItemAdapterInterface itemAdapterInterface, Context mContext, List<Item> itemList) {
        this.itemAdapterInterface = itemAdapterInterface;
        this.context = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_cardview, parent,false);
        return new ItemAdapter.ViewHolder(view, itemAdapterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.textView.setText(item.getName());
        Picasso.get().load(item.getImageURL()).fit().placeholder(R.drawable.image_icon).fit()
                .centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView, ItemAdapterInterface itemAdapterInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewCardView);
            textView = itemView.findViewById(R.id.textViewCardView);

            itemView.setOnClickListener(v -> {
                if(itemAdapterInterface != null) {
                    int itemPosition = getAdapterPosition();

                    if(itemPosition != RecyclerView.NO_POSITION) {
                        itemAdapterInterface.onItemClick(itemPosition);
                    }
                }
            });
        }
    }
}
