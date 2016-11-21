package com.example.ooongi.serverprograming;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.ooongi.serverprograming.JoinActivity.address;

/**
 * Created by ooongi on 2016-11-07.
 */

public class LoginActivity extends Activity {
    String user_id;
    String user_pw;
    String temp_address;
    Boolean add_address_ID=false;
    String result="";
    BackgroundTask task; //쓰레드 객체 사용 선언
    Boolean login_OK=false;//로그인 성공 여부 변수
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onResume();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void click_Login(View v)
    {
        EditText edit_id = (EditText) findViewById(R.id.userid);
        EditText edit_pw = (EditText) findViewById(R.id.userpw);
        user_id = edit_id.getText().toString();
        user_pw = edit_pw.getText().toString();
        task = new BackgroundTask();//쓰레드 객체 생성
        task.execute();//쓰레드 실행
        SystemClock.sleep(500);

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);//대화상자 객체 생성
        dlg.setTitle("로그인");//대화상자 타이틀 설정
        if(result==""){
            Log.d("1","로그인실패");
            login_OK=false;
        }
        else{//조인성공
            Log.d("1","로그인성공");
            login_OK=true;
        }
        if(login_OK==true){
            dlg.setMessage("로그인이 완료되었습니다.");//로그인 성공시 대화상자 출력내용
            login_OK=false;
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
            {// 확인 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){
                    Intent i = new Intent(LoginActivity.this,mainscreenActivity.class);
                    startActivity(i);
                }
            });
        }
        else{
            dlg.setMessage("아이디와 비밀번호를 다시 확인하세요.");//로그인 실패시 대화상자 출력내용
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
            {// 확인 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){
                }
            });
            login_OK=false;
        }
        dlg.show();
    }
    protected void click_join(View v){
        Intent i = new Intent(LoginActivity.this,JoinActivity.class);
        startActivity(i);
    }
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
            if (add_address_ID == false)
            {
                add_address_ID = true;
                temp_address = address + "/login";//login_post URL로 이동하기 위해서 사용
            }
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub
            Log.d("현재 주소:", address);
            Log.d("검색ID:",user_id);
            result = request(temp_address);
            Log.d("아이디 확인:", result);
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
            String param="id="+user_id+"&pw="+user_pw;
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
    protected void save_ID_to_File(View v)//파일입출력 사용
    {
        EditText getid = (EditText)findViewById(R.id.userid);
        EditText getpw = (EditText)findViewById(R.id.userpw);
        String FILE_NAME = "hidden_id_log.txt";
        String str = getid.getText().toString();//id
        String str2 = getpw.getText().toString();//pw
        String sum_strs = str +" "+ str2;//split하기 위해서 합침
        try{
            FileOutputStream fos = openFileOutput("test.txt", Context.MODE_PRIVATE);
            fos.write(sum_strs.getBytes());
            Log.d("저장",sum_strs.getBytes().toString());
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);//대화상자 객체 생성
            dlg.setTitle("아이디 저장");//대화상자 타이틀 설정
            dlg.setPositiveButton("완료", new DialogInterface.OnClickListener()
            {// 확인 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){}
            });
            dlg.show();
            fos.close();
        }catch(Exception e){
            Log.d("실패","원인은:"+e);
        }
    }
    protected void load_File_to_ID(View v){
        try {
            EditText getid = (EditText)findViewById(R.id.userid);
            EditText getpw = (EditText)findViewById(R.id.userpw);
            FileInputStream fis = openFileInput("test.txt");
            byte[] data = new byte[fis.available()];
            String datas="";
            String[] temp_datas=new String[2];
            while(fis.read(data) != -1) {;}
            fis.close();
            datas=datas+new String(data);//byte를 String객체로 생성.
            Log.d("datas",datas);
            temp_datas=datas.split(" ");
            getid.setText(new String(temp_datas[0]));//스페이스바를 분기로 id  pw
            Log.d("temp0",temp_datas[0]);
            getpw.setText(new String(temp_datas[1]));
            Log.d("temp1",temp_datas[1]);
            EditText edit_id = (EditText) findViewById(R.id.userid);
            EditText edit_pw = (EditText) findViewById(R.id.userpw);
            user_id = edit_id.getText().toString();
            user_pw = edit_pw.getText().toString();
            task = new BackgroundTask();//쓰레드 객체 생성
            task.execute();//쓰레드 실행


            AlertDialog.Builder dlg = new AlertDialog.Builder(this);//대화상자 객체 생성
            dlg.setTitle("로그인");//대화상자 타이틀 설정
            if(result==""){
                Log.d("1","로그인실패");
                login_OK=false;
            }
            else{//조인성공
                Log.d("1","로그인성공");
                login_OK=true;
            }
            if(login_OK==true){
                dlg.setMessage("로그인이 완료되었습니다.");//로그인 성공시 대화상자 출력내용
                login_OK=false;
            }
            else{
                dlg.setMessage("아이디와 비밀번호를 다시 확인하세요.");//로그인 실패시 대화상자 출력내용
                login_OK=false;
            }

            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener()
            {// 확인 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){
                    Intent i = new Intent(LoginActivity.this,mainscreenActivity.class);
                    startActivity(i);
                }
            });
            dlg.show();
        } catch(Exception e) {
        }
    }
    protected void goFindActivity(View v){
        Intent i = new Intent(LoginActivity.this,FindidpwActivity.class);
        startActivity(i);
    }
}
