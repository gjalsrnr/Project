package com.example.ooongi.serverprograming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.ooongi.serverprograming.JoinActivity.address;

/**
 * Created by ooongi on 2016-11-20.
 */

public class FindidpwActivity extends Activity {
    String user_id;
    String user_email1,user_email2;
    String user_name;
    String temp_address="";
    String result;
    boolean add_address_DB=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_pw);
    }
    protected void Click_find_id(View v){
        EditText user_name_in_ID = (EditText)findViewById(R.id.username_findid);
        EditText user_email1_in_ID = (EditText)findViewById(R.id.useremail1_findid);
        EditText user_email2_in_ID = (EditText)findViewById(R.id.useremail2_findid);
        user_name=user_name_in_ID.getText().toString();
        user_email1=user_email1_in_ID.getText().toString();
        user_email2=user_email2_in_ID.getText().toString();
        final BackgroundTask task = new BackgroundTask();
        task.execute();
        SystemClock.sleep(500);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);//대화상자 객체 생성
        dlg.setTitle("ID찾기");//대화상자 타이틀 설정
        if(result==""){
            dlg.setMessage("정보를 다시 입력해주세요.");//로그인 실패시 대화상자 출력내용
        }
        else{//조인성공
            dlg.setMessage("이메일로 송신 되었습니다.");//로그인 성공시 대화상자 출력내용
        }
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
        {// 확인 버튼 클릭시 설정
            public void onClick(DialogInterface dialog, int whichButton){
            }
        });
        dlg.show();
    }
    protected void Click_find_pw(View v){
        EditText user_name_in_PW = (EditText)findViewById(R.id.username_findpw);
        EditText user_email1_in_PW = (EditText)findViewById(R.id.useremail1_findpw);
        EditText user_email2_in_PW = (EditText)findViewById(R.id.useremail2_findpw);
        EditText user_id_in_PW = (EditText)findViewById(R.id.userid_findpw);
        user_id=user_id_in_PW.getText().toString();
        user_email1=user_email1_in_PW.getText().toString();
        user_email2=user_email2_in_PW.getText().toString();
        user_name=user_name_in_PW.getText().toString();
        BackgroundTask2 task = new BackgroundTask2();
        task.execute();
        SystemClock.sleep(500);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);//대화상자 객체 생성
        dlg.setTitle("ID찾기");//대화상자 타이틀 설정
        if(result==""){
            dlg.setMessage("정보를 다시 입력해주세요.");//로그인 실패시 대화상자 출력내용
        }
        else{//조인성공
            dlg.setMessage("이메일로 송신 되었습니다.");//로그인 성공시 대화상자 출력내용
        }
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
        {// 확인 버튼 클릭시 설정
            public void onClick(DialogInterface dialog, int whichButton){
            }
        });
        dlg.show();
    }

    class BackgroundTask extends AsyncTask<Integer, Integer, Integer>//find id
    {
        protected void onPreExecute() {
            temp_address =address+"/findid";
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            Log.d("현재 주소:",temp_address);
            result =request(temp_address);
            Log.d("결과 값:",result);
            return null;
        }
        protected void onPostExecute(String s) {

        }
    }
    class BackgroundTask2 extends AsyncTask<Integer, Integer, Integer>//find id
    {
        protected void onPreExecute() {
            if(add_address_DB==false){
                add_address_DB=true;
                temp_address =address+"/findpw";}
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            Log.d("현재 주소:",temp_address);
            result =request2(temp_address);
            Log.d("결과 값:",result);
            return null;
        }
    }
    private String request(String urlStr)//find id
    {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String param="username="+user_name+"&user_email1="+user_email1+"&user_email2="+user_email2;
            DataOutputStream out_stream = new DataOutputStream(conn.getOutputStream());
            out_stream.writeBytes(param);
            out_stream.flush();
            out_stream.close();
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
    private String request2(String urlStr)//find id
    {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String param="username="+user_name+"&user_email1="+user_email1+"&user_email2="+user_email2+"&userid="+user_id;
            DataOutputStream out_stream = new DataOutputStream(conn.getOutputStream());
            out_stream.writeBytes(param);
            out_stream.flush();
            out_stream.close();
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
}
