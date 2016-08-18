package com.hwon.smartcloset;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WritePostActivity extends AppCompatActivity implements TurboImageViewListener {
    private static final String TAG = "WritePostActivity";

    private Button btnGallery;
    private Button btnDelItem;
    private EditText etText;
    private LinearLayout imageContainer;
    private TurboImageView turboImageView;
    int GET_PICTURE_URI;

    public static final String SELECT_RESULTS_ARRAY = "default_select_array";
    public static final String SELECT_RESULTS = "default_select";
    public static final String RESULTS_TYPE = "result_type";

    public static final int MY_PERMISSION_REQUEST_STORAGE = 0;

    private ArrayList<String> resultPhotoUris;
    private String resultPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissionDenial();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("코디");
        //뒤로가기 화살표 표시//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGallery = (Button) findViewById(R.id.btn_writepost_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WritePostActivity.this, GalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(GalleryActivity.IS_MULTI_SELECT, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, GET_PICTURE_URI);
            }
        });

        btnDelItem = (Button) findViewById(R.id.btn_del_item);
        btnDelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turboImageView.removeSelectedObject();
            }
        });

        etText = (EditText)findViewById(R.id.et_writepost_text);
       // imageContainer = (LinearLayout)findViewById(R.id.layout_imageContainer);

        turboImageView = (TurboImageView) findViewById(R.id.turboImageView);
        turboImageView.setListener(this);

        //imageView = (ImageView) findViewById(R.id.imageView);

    }

    //메뉴 선택//
    public boolean onOptionsItemSelected(MenuItem item) {
        //뒤로가기 적용//
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.write:
                Bitmap bitmap;
                turboImageView.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(turboImageView.getDrawingCache());
                turboImageView.setDrawingCacheEnabled(false);
                turboImageView.addObject(getApplicationContext(), bitmap);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write_post_menu, menu);
        return true;
    }

    private void requestPermissionDenial() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{

                }
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            int resultType = data.getExtras().getInt(RESULTS_TYPE);
            if(1 == resultType){
                resultPhotoUri = data.getExtras().getString(SELECT_RESULTS);
                addItem(resultPhotoUri);
            }else if(0 == resultType){
                resultPhotoUris = data.getExtras().getStringArrayList(SELECT_RESULTS_ARRAY);
                for(int i = 0; i < resultPhotoUris.size(); i++) {
                    addItem(resultPhotoUris.get(i));
                }
            }
        }
    }



    //ControlImageView Listener
    public void onImageObjectSelected(MultiTouchObject multiTouchObject){
        Log.d(TAG, "image object selected");
    }

    public void onImageObjectDropped() {
        Log.d(TAG, "image object dropped");
    }

    public void onCanvasTouched() {
        //turboImageView.deselectAll();
        turboImageView.deselectAll();
    }
    public void addItem(String uri){
        //String comletePath = "file://" + uri;
        //Uri completeUri = Uri.parse(comletePath);

        try {
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), completeUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(uri, options);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
            turboImageView.addObject(this, bitmap);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "bitmap 생성 실패", Toast.LENGTH_LONG).show();
        }
    }
}
