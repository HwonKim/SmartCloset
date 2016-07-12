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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etName;
    private EditText etPwd;
    private EditText etPwd2;
    private EditText etAge;
    private String sex = "M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //Toolbar Setting//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("회원가입");
        //뒤로가기 화살표 표시//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //UI 생성//
        etEmail = (EditText)findViewById(R.id.et_join_email);
        etName = (EditText)findViewById(R.id.et_join_name);
        etPwd = (EditText)findViewById(R.id.et_join_pwd);
        etPwd2 = (EditText)findViewById(R.id.et_join_pwd2);
        etAge = (EditText)findViewById(R.id.et_join_age);

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

    //라디오 버튼 처리//
    public void onRadioButtonClicked(View view){
        switch (view.getId()){
            case R.id.rb_join_male:
                sex = "M";
                break;
            case R.id.rb_join_female:
                sex = "F";
                break;
        }
    }

    // 회원가입 버튼 처리 //
    public void onClickJoin(View v){
        //8038포트 포트 포워딩 되어 있어야 한다.//
        final String url = "http://106.243.132.150:8038/SmartCloset/Join.jsp";
        String email = etEmail.getText().toString();
        String name = etName.getText().toString();
        String pwd = etPwd.getText().toString();
        String pwd2 = etPwd2.getText().toString();
        String age = etAge.getText().toString();

        if(checkData(email, name, pwd, pwd2, age)){
            AsyncHttpTask task = new AsyncHttpTask();
            task.execute(url, email, name, pwd, age, sex);
        }
    }

    // 입력 데이타 확인 //
    private boolean checkData(String... params){
        if(params[0].equals("")){
            Toast.makeText(getApplicationContext(), "e-mail을 입력해주세요", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        if(params[1].equals("")){
            Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        }
        if(params[2].equals("")){
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            etPwd.requestFocus();
            return false;
        }
        if(params[3].equals("")){
            Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
            etPwd2.requestFocus();
            return false;
        }
        if(!params[3].equals(params[2])){
            Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
            etPwd.requestFocus();
            return false;
        }
        if(params[4].equals("")){
            Toast.makeText(getApplicationContext(), "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
            etAge.requestFocus();
            return false;
        }else if(!params[4].equals("")){
            Integer temp = Integer.parseInt(params[4]);
            if(temp < 0 || temp >150){
                Toast.makeText(getApplicationContext(), "나이를 잘못입력하였습니다", Toast.LENGTH_SHORT).show();
                etAge.requestFocus();
                return false;
            }
        }
        return true;
    }
    //서버 접속 및 회원 가입 진행//
    public class AsyncHttpTask extends AsyncTask<String, Void, String>{
        private static final String TAG ="HttpConnection";

        String req;

        protected String doInBackground(String... params){
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

                JSONObject outputJson = new JSONObject();
                outputJson.put("email", params[1]);
                outputJson.put("name", params[2]);
                outputJson.put("pwd", params[3]);
                outputJson.put("age", params[4]);
                outputJson.put("sex", params[5]);

                OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream());
                os.write(outputJson.toString());
                os.flush();

                int respondCode = urlConnection.getResponseCode();
                if(HttpURLConnection.HTTP_OK == respondCode){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    JSONObject inputJson = new JSONObject(convertInputStreamToString(inputStream));
                    req = inputJson.getString("status");
                }
            }catch (Exception e){
                Log.e(TAG, e.getLocalizedMessage());
            }

            return req;
        }

        // inputstream to String //
        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while( null != (line = bufferedReader.readLine())){
                result +=line;
            }
            if(null != inputStream)
                inputStream.close();
            return result;
        }

        // 서버 연결 후 실행, 결과 판단 //
        protected void onPostExecute(String result){

            if(req.equals("success")){
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(JoinActivity.this, IntroActivity.class);
                startActivity(intent);
            }else if(req.equals("InsertFail")){
                Toast.makeText(getApplicationContext(), "존재하는 이메일입니다", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
            }else if(req.equals("DBFail")) {
                Toast.makeText(getApplicationContext(), "DB 접속 실패", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
            }else if(req.equals("DataSendFail")) {
                Toast.makeText(getApplicationContext(), "데이타 전송 실패", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
            }
        }
    }
}
