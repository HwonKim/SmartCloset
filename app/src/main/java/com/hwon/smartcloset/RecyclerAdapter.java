package com.hwon.smartcloset;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by hwkim_000 on 2016-07-14.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<CardItem> cardItems;

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_POST = 2;

    public RecyclerAdapter(Context context, List<CardItem> CardItems){
        this.context = context;
        this.cardItems = CardItems;
    }
    //onCreateViewHolder, onBindViewHolder, getItemCount 꼭 구현해줘야 함
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, null);
                    view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    return new HeaderViewHolder(view);
            case TYPE_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
                    view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    return new ViewHolder(view);
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof ViewHolder) {
            final CardItem item = cardItems.get(position-1);
            Drawable drawable = ContextCompat.getDrawable(context, item.getImage());
            ((ViewHolder) holder).image.setBackground(drawable);
            ((ViewHolder) holder).title.setText(item.getTitle());
            ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).btnCodi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Codi Btn Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            ((HeaderViewHolder)holder).btnPic.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Pic Btn Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public int getItemCount() {
        return this.cardItems.size() + 1;
    }

    public int getItemViewType(int position){
        return position == 0 ? TYPE_HEADER : TYPE_POST;
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

    public class HeaderViewHolder extends  RecyclerView.ViewHolder{
        LinearLayout header_layout;
        Button btnCodi;
        Button btnPic;
        public HeaderViewHolder(View itemView){
            super(itemView);
            header_layout = (LinearLayout) itemView.findViewById(R.id.item_header);
            btnCodi = (Button)itemView.findViewById(R.id.btn_itemHeader_codi);
            btnPic = (Button)itemView.findViewById(R.id.btn_itemHeader_pic);
        }
    }
}
