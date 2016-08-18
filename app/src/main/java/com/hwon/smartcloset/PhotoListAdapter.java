package com.hwon.smartcloset;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwkim_000 on 2016-07-27.
 */
public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoVH> {

    //ItemCheck Listener Interface
    private OnItemCheckListener onItemCheckListener = null;
    //Photo Item 담을 PhotoItem 타입 List
    private ArrayList<String> resultPhotoUris = new ArrayList<>();
    //PhotoItem 하나 선택 가능한 경우 담을 uri;
    private String resultPhotoUri;
    private final List<PhotoItem> photoItems = new ArrayList<>();
    private final Activity activity;
    private boolean isMultiSelect = true;
    private int maxSize = 10;

    // ListAdapter 생성자 activity와 photoItem List 전달 받음//
    public PhotoListAdapter(Activity activity, List<PhotoItem> photoItems){
        if(photoItems != null){
            this.photoItems.addAll(photoItems);
        }
        this.activity = activity;
    }

    @Override
    public int getItemCount(){
        return photoItems.size();
    }

    public void setData(List<PhotoItem> photoUris){
        photoItems.clear();
        photoItems.addAll(photoUris);
    }

    @Override
    // View Holder 생성 //
    public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        // 만들어 놓은 PhotoItem layout 가져옴 //
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_photo, null);
        //PhotoVH 객체에 itemView 넣어서 생성한 후 리턴 //
        return new PhotoVH(itemView);
    }
    @Override
    public void onBindViewHolder(PhotoVH holder, int position){
        ImageView ivPhoto = holder.iv_photo;
        PhotoItem photoItem = photoItems.get(position);
        Glide.with(activity).load(photoItem.uri).thumbnail(.1f).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT).into(ivPhoto);
        CheckBox cbCheck = holder.cb_check;
        cbCheck.setChecked(photoItem.isChecked);
        cbCheck.setTag(position);
        cbCheck.setOnClickListener(checkedChangeListener);
    }

    private View.OnClickListener checkedChangeListener = new View.OnClickListener(){

        private PhotoItem lastPhotoItem;
        private int lastPosition;
        @Override
        public void onClick(View v){

            CheckBox cbCheck = (CheckBox) v;
            boolean bChecked = cbCheck.isChecked();

            if(isMultiSelect && maxSize == resultPhotoUris.size() && bChecked){
                //Toast.makeText(activity.getApplicationContext(), )
                cbCheck.setChecked(false);
                return;
            }

            int position = (int) v.getTag();
            PhotoItem photoItem = photoItems.get(position);
            photoItem.isChecked = bChecked;
            //하나만 선택 가능할 때
            if(!isMultiSelect){
                //lastPhotoItem에 값이 들어가 있을 때
                if(lastPhotoItem != null){
                    //똑같은거 또 클릭한 경우
                    if(lastPhotoItem.equals(photoItem)){
                        lastPhotoItem = null;
                        resultPhotoUris = null;
                    }else{// 다른 것 클릭한 경우
                        lastPhotoItem.isChecked = false;
                        notifyItemChanged(lastPosition);
                        lastPhotoItem = photoItem;
                        lastPosition = position;
                    }
                }else{ //lastPhotoItem에 값이 없는 경우(처음 클릭한 경우)
                    photoItem.isChecked = bChecked;
                    lastPhotoItem = photoItem;
                    lastPosition = position;
                }
                if(bChecked){
                    resultPhotoUri = photoItem.uri;
                }
            }else{ // 여러개 선택 가능한 경우
                String uri = photoItem.uri;
                if(bChecked){ //선택 된 경우
                    if(!resultPhotoUris.contains(uri)){ //uri 리스트에 uri 없으면
                        resultPhotoUris.add(uri);
                    }
                }else{ //선택 해제 된 경우
                    if(resultPhotoUris.contains(uri)){
                        resultPhotoUris.remove(uri);
                    }
                }
            }

            if(onItemCheckListener != null){
                onItemCheckListener.OnItemCheck(resultPhotoUris);
            }
        }

    };

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener){
        this.onItemCheckListener = onItemCheckListener;
    }


    class PhotoVH extends RecyclerView.ViewHolder{
        ImageView iv_photo;
        CheckBox cb_check;

        public PhotoVH(View itemView){
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            cb_check = (CheckBox) itemView.findViewById(R.id.cb_select);
        }
    }
}
