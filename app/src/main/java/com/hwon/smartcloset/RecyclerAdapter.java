package com.hwon.smartcloset;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by hwkim_000 on 2016-07-14.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    List<CardItem> cardItems;

    public RecyclerAdapter(Context context, List<CardItem> CardItems){
        this.context = context;
        this.cardItems = CardItems;
    }
    //onCreateViewHolder, onBindViewHolder, getItemCount 꼭 구현해줘야 함
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        final CardItem item = cardItems.get(position);
        Drawable drawable = ContextCompat.getDrawable(context, item.getImage());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public int getItemCount() {
        return this.cardItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        CardView cardView;
        public ViewHolder(View itemView){
            super(itemView);
            //cardview 안에 있는 것들 찾음
            image = (ImageView) itemView.findViewById(R.id.card_img);
            title = (TextView) itemView.findViewById(R.id.card_title);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

    }
}
