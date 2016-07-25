package com.hwon.smartcloset;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    final int ITEM_SIZE = 6;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
        private RecyclerView.LayoutManager layoutManager;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_home, container, false);
            recyclerView = (RecyclerView) v.findViewById(R.id.recycle_view);
            recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<CardItem> cardItemList = new ArrayList<>();

        CardItem[] cardItem = new  CardItem[ITEM_SIZE];
        cardItem[0] = new CardItem(R.drawable.one, "#one");
        cardItem[1] = new CardItem(R.drawable.two, "#two");
        cardItem[2] = new CardItem(R.drawable.three, "#three");
        cardItem[3] = new CardItem(R.drawable.four, "#four");
        cardItem[4] = new CardItem(R.drawable.five, "#five");
        cardItem[5] = new CardItem(R.drawable.six, "#six");

        for(int i = 0; i < ITEM_SIZE; i++){
            cardItemList.add(cardItem[i]);
        }
        adapter = new RecyclerAdapter(getContext() ,cardItemList);
        recyclerView.setAdapter(adapter);
        return v;


    }

}
