package com.hwon.smartcloset;

/**
 * Created by hwkim_000 on 2016-07-14.
 */
public class CardItem {
    int image;
    String title;

    CardItem(int image, String title){
        this.image = image;
        this.title = title;
    }

    int getImage(){
        return this.image;
    }

    String getTitle(){
        return this.title;
    }


}
