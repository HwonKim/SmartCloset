package com.hwon.smartcloset;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

public class GalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SELECT_RESULTS_ARRAY = "default_select_array";
    public static final String SELECT_RESULTS = "default_select";
    public static final String RESULTS_TYPE = "result_type";
    public static final String IS_MULTI_SELECT = "is_multi_select";

    private File takePhotoFile;

    public boolean showMenu = true;
    public boolean isMultiSelect = true;
    public boolean isShowGif = true;
    private static final String ALL_PHOTO_DIR_NAME = "전체보기";
    private static final int TAKE_PHOTO = 100;

    // 폴더별 현재 보여줄 아이템 리스트 //
    private List<PhotoItem> currentDisplayPhotos;
    // photo Recycle Adapter 관련 변수 //
    private RecyclerView rvContainer;
    private PhotoListAdapter photoListAdapter;
    private ArrayList<String> getPhotoUris;
    private String resultPhotoUri;

    private ArrayList<String> menuDirs = new ArrayList<>();
    private Map<String, List<PhotoItem>> photoDirMap = new ArrayMap<>();

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        /*전달받은 Intent 처리*/
        if(null != bundle){
            if(isMultiSelect){
                getPhotoUris = bundle.getStringArrayList(SELECT_RESULTS_ARRAY);
                if(getPhotoUris == null){
                    getPhotoUris = new ArrayList<>();
                }
            }else{
                resultPhotoUri = bundle.getString(SELECT_RESULTS);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("갤러리");
        //뒤로가기 화살표 표시//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvContainer = (RecyclerView)findViewById(R.id.rv_gallery);

        if(rvContainer != null){
            rvContainer.setHasFixedSize(true);
            rvContainer.setLayoutManager(new GridLayoutManager(this, 3));
            rvContainer.setItemAnimator(new DefaultItemAnimator());
            photoListAdapter = new PhotoListAdapter(this, null);
            photoListAdapter.setOnItemCheckListener(new OnItemCheckListener() {
                @Override
                public void OnItemCheck(ArrayList<String> resultPhotoUris) {
                    getPhotoUris = resultPhotoUris;
                    if(!getPhotoUris.isEmpty()){
                        if(showMenu) {
                            showMenu = false;
                            invalidateOptionsMenu();
                        }

                    }else{
                        if(!showMenu) {
                            showMenu = true;
                            invalidateOptionsMenu();
                        }

                    }

                }
            });
            rvContainer.setAdapter(photoListAdapter);
        }
        //내가 가지고 있는 onCreateLoader 호출
        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        CursorLoader cursorLoader = new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME},
                "mime_type=? or mime_type=?" + (isShowGif ? "or mime_type=?" : ""),
                isShowGif ? new String[]{"image/jpeg", "image/png", "image/gif"} : new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_ADDED + " DESC"

        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        if(data.moveToFirst()){
            List<PhotoItem> allPhotoUris = new ArrayList<>();
            if(!photoDirMap.containsKey(ALL_PHOTO_DIR_NAME)){
                photoDirMap.put(ALL_PHOTO_DIR_NAME, allPhotoUris);
            }
            if(!menuDirs.contains(ALL_PHOTO_DIR_NAME)){
                menuDirs.add(ALL_PHOTO_DIR_NAME);
            }
            List<PhotoItem>  photoItems;
            do{
                String photoDir = data.getString(data.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                String photoUri = data.getString(data.getColumnIndex(MediaStore.Images.Media.DATA));
                if(!photoDirMap.containsKey(photoDir)){
                    photoItems = new ArrayList<>();
                    photoDirMap.put(photoDir, photoItems);
                    menuDirs.add(photoDir);
                }else{
                    photoItems = photoDirMap.get(photoDir);
                }
                PhotoItem photoItem = new PhotoItem();
                photoItem.uri = photoUri;
                photoItems.add(photoItem);
                allPhotoUris.add(photoItem);
            }while(data.moveToNext());
            notifyChangePhotoList(ALL_PHOTO_DIR_NAME, allPhotoUris);
        }
    }

    private void notifyChangePhotoList(String subTitle, List<PhotoItem> photoItems){
        currentDisplayPhotos = photoItems;
        photoListAdapter.setData(photoItems);
        photoListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //do nothing
    }

    public boolean onCreateOptionsMenu(Menu menu){
        if(showMenu) {
            MenuItem menuCamera = menu.add(Menu.NONE, R.id.menu_camera, 0, "촬영").setIcon(R.drawable.ic_photo_camera_black_24dp);
            MenuItemCompat.setShowAsAction(menuCamera, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
            SubMenu menuDir = menu.addSubMenu(Menu.NONE, R.id.menu_dir, 1, "폴더").setIcon(R.drawable.ic_folder_open_black_24dp);
            MenuItemCompat.setShowAsAction(menuDir.getItem(), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        }else{
            MenuItem menuDone = menu.add(Menu.NONE, R.id.menu_done, 0, "완료").setTitle("완료");
            MenuItemCompat.setShowAsAction(menuDone, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //뒤로가기 적용//
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_dir:
                SubMenu subMenu = item.getSubMenu();
                subMenu.clear();
                //menuDirs값으로 sub menu만듬
                for(String text : menuDirs){
                    subMenu.add(Menu.NONE, Menu.FIRST + 1, 0, text);
                }
                return true;
            case R.id.menu_camera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePhotoFile = createImageFile();
                if(takePhotoFile != null){
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takePhotoFile));
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }else{
                    Toast.makeText(getApplicationContext(), "Camera open failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_done:
                Intent returnData = new Intent();
                returnData.putExtra(RESULTS_TYPE, 0);
                returnData.putExtra(SELECT_RESULTS_ARRAY, getPhotoUris);
                setResult(RESULT_OK, returnData);
                finish();
            default:
                String menuTitle = item.getTitle().toString();
                List<PhotoItem> photoItems = photoDirMap.get(menuTitle);
                if(photoItems != null)
                    notifyChangePhotoList(menuTitle, photoItems);
        }
        return super.onOptionsItemSelected(item);
    }

    private File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if(!storageDir.exists()){
            if(!storageDir.mkdir()){
                return null;
            }
        }
        File file = null;
        try{
            file = File.createTempFile(imageFileName, ".jpg", storageDir);
        }catch (IOException e){
            e.printStackTrace();
        }

        return file;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PHOTO && resultCode == RESULT_OK){
            if(takePhotoFile != null){
                notifyMediaUpdate(takePhotoFile);
                Uri fileUri = Uri.fromFile(takePhotoFile);
                resultPhotoUri = fileUri.toString();
                Intent returnData = new Intent();
                returnData.putExtra(RESULTS_TYPE, 1);
                returnData.putExtra(SELECT_RESULTS, resultPhotoUri);
                setResult(RESULT_OK, returnData);
                finish();
            }
        }
    }

    public void notifyMediaUpdate(File file){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
