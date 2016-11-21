package com.example.ooongi.serverprograming;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoinActivity extends Activity {
    boolean add_address_DB=false;//1번만 address값을 추가하기 위해서 사용하는 변수.
    boolean add_address_ID=false;
    ProgressDialog mProgressDialog;
    String user_id, user_pw, user_email1, user_email2, user_name;
    String tv;
    static String address ="http://172.16.18.178:55736";
    String temp_address="";
    String result;
    Boolean pass_member=false;//true면 가입가능.
    BackgroundTask task;
    BackgroundTask2 task2;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }
    protected void id_check(View v) {
        EditText edit_id = (EditText) findViewById(R.id.userid);
        user_id = edit_id.getText().toString();
        mProgressDialog=mProgressDialog.show(JoinActivity.this,"아이디 검사중","검사 중입니다...",true);
        task2 = new BackgroundTask2();
        task2.execute();
        SystemClock.sleep(300);
        Log.d("result",result);
        if(result==""){
            iv = (ImageView)findViewById(R.id.iv);
            iv.setImageResource(R.drawable.checked);
            pass_member=true;
        }
        else {
            iv = (ImageView) findViewById(R.id.iv);
            iv.setImageResource(R.drawable.forbidden);
            pass_member=false;
        }

    }
    class BackgroundTask2 extends AsyncTask<Integer, Integer, Integer>
    {
        protected void onPreExecute()
        {
            if (add_address_ID == false)
            {
                add_address_ID = true;
                add_address_DB=false;
                temp_address = address + "/check_userid";
            }
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            Log.d("현재 주소:", temp_address);
            Log.d("검색ID:",user_id);
            result = request(temp_address,user_id);
            Log.d("아이디 확인:", result);
            Log.d("123123",result);
            SystemClock.sleep(1500);
            mProgressDialog.dismiss();
            return null;
        }
        protected void onPostExecute(String s) {

        }

    }
    protected void onClick(View v) {
        EditText edit_pw = (EditText) findViewById(R.id.userpw);
        EditText edit_email1 = (EditText) findViewById(R.id.useremail_address1);
        EditText edit_email2 = (EditText) findViewById(R.id.useremail_address2);
        EditText edit_name = (EditText)findViewById(R.id.username);
        user_pw = edit_pw.getText().toString();
        user_email1 = edit_email1.getText().toString();
        user_email2 = edit_email2.getText().toString();
        user_name= edit_name.getText().toString();
        if(pass_member==true){//회원가입 조건이 성사되면 DB에 값을 넣음.
            task = new BackgroundTask();
            task.execute();
        }

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("회원가입");
        Log.d("pass_member",pass_member.toString());
        if(pass_member==true){
            dlg.setMessage("회원가입이 완료되었습니다.");
            pass_member=false;
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
            {// 확인 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){
                    Intent i = new Intent(JoinActivity.this,LoginActivity.class);
                    startActivity(i);
                }
            });
        }
        else{
            dlg.setMessage("아이디를 다시 확인하여주세요.");
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
            {// 확인 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){}});
        }
        dlg.show();
    }
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
            if(add_address_DB==false){
                add_address_DB=true;
                add_address_ID=false;
                temp_address =address+"/join";}
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            Log.d("현재 주소:",address);
            result =request(temp_address);
            Log.d("결과 값:",result);

            return null;
        }


    }
    private String request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            Log.d("사용중","1");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            Log.d("데이터 전송","4");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Log.d("사용중","1");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String param="id="+user_id+"&pw="+user_pw+"&email1="+user_email1+"&email2="+user_email2+"&username="+user_name+"&master=2";
            DataOutputStream out_stream = new DataOutputStream(conn.getOutputStream());
            out_stream.writeBytes(param);
            out_stream.flush();
            Log.d("사용 중","1");
            out_stream.close();
            Log.d("데이터 전송","4");
            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                String line = null;
                while(true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }
        } catch(Exception ex) {
            Toast toast = Toast.makeText(this,"실패",Toast.LENGTH_SHORT);
            toast.show();
            ex.printStackTrace();
        }

        return output.toString();
        //return "";
    }
    private String request(String urlStr,String user_id) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            Log.d("데이터 전송","5");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out_stream = new DataOutputStream(conn.getOutputStream());
            String param="id="+user_id;
            out_stream.writeBytes(param);
            out_stream.flush();
            out_stream.close();
            Log.d("데이터 전송","5");
            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                String line = null;
                while(true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }
        } catch(Exception ex) {
            Toast toast = Toast.makeText(this,"실패",Toast.LENGTH_SHORT);
            toast.show();
            ex.printStackTrace();
        }
        return output.toString();
    }

}