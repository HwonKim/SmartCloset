package com.hwon.smartcloset;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar Setting//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("로그인");
        //뒤로가기 화살표//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmail = (EditText)findViewById(R.id.et_login_email);
        etPwd = (EditText)findViewById(R.id.et_login_pwd);
    }

    //메뉴 선택//
    public boolean onOptionsItemSelected(MenuItem item) {
        //뒤로가기 적용//
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickLogin(View v){
        final String url = "http://106.243.132.150:8038/SmartCloset/Login.jsp";
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        AsyncHttpTask task = new AsyncHttpTask();
        task.execute(url, email, pwd);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "Http Connection";
        private String[] blogTitles;
        String req;
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                JSONObject json = new JSONObject();
                json.put("email", params[1]);
                json.put("pwd", params[2]);

                OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream());
                os.write(json.toString());
                os.flush();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONObject tempJson = new JSONObject(convertInputStreamToString(inputStream));
                    req = tempJson.getString("status");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return req;
        }
        protected void onPostExecute(String result){
            if(req.equals("success")){
                Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }else if(req.equals("fail")){
                Toast.makeText(getApplicationContext(), "없는 아이디입니다", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
            }else if(req.equals("wrong")){
                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                etPwd.requestFocus();
            }
        }
        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null){
                result += line;
            }
            if(null!=inputStream){
                inputStream.close();
            }
            return result;
        }
    }
}
