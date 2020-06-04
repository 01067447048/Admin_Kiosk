package kr.ac.hoseo.admin_kiosk.ui;

import android.animation.Animator;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import kr.ac.hoseo.admin_kiosk.AES256Util;
import kr.ac.hoseo.admin_kiosk.R;
import kr.ac.hoseo.admin_kiosk.RequestService;
import kr.ac.hoseo.admin_kiosk.SSLUtil;
import kr.ac.hoseo.admin_kiosk.data.CheckMessage;
import kr.ac.hoseo.admin_kiosk.database.DatabaseService;
import kr.ac.hoseo.admin_kiosk.database.NetworkStatus;
import kr.ac.hoseo.admin_kiosk.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShuttleBusActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String url;
    private String type, bus_type, type_for_server;
    private int station;
    private Date current;
    private String time;
    private String campus;
    private static Handler handler;
    private AudioManager audioManager;
    private Uri notification;
    private Ringtone ringtone;
    private DatabaseService service;
    private String DbTime;
    private String student_id;

    private final String BASE_URL = "https://hub.hsu.ac.kr";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        service = new DatabaseService();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                current = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 /  HH:mm:ss");
                time = format.format(current);

                binding.time.setText(time);
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                service.DatabaseInit(getApplicationContext());
                while (true) {
                    if(NetworkStatus.getConnectType(getApplicationContext())==-1){
                        service.DatabaseSelect();
                    }
                    else{
                        service.SendServer();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

        initAnimation();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        campus = intent.getStringExtra("campus");
        bus_type = intent.getStringExtra("bus_type");

        if (type.equals("승차")) {
            type_for_server = "하교";
        } else {
            type_for_server = "등교";
        }

        binding.data.setText(campus + " / " + bus_type + " / " + type + "(" + type_for_server + ")");

        binding.btn0.setOnClickListener(view -> binding.getId.append("0"));
        binding.btn1.setOnClickListener(view -> binding.getId.append("1"));
        binding.btn2.setOnClickListener(view -> binding.getId.append("2"));
        binding.btn3.setOnClickListener(view -> binding.getId.append("3"));
        binding.btn4.setOnClickListener(view -> binding.getId.append("4"));
        binding.btn5.setOnClickListener(view -> binding.getId.append("5"));
        binding.btn6.setOnClickListener(view -> binding.getId.append("6"));
        binding.btn7.setOnClickListener(view -> binding.getId.append("7"));
        binding.btn8.setOnClickListener(view -> binding.getId.append("8"));
        binding.btn9.setOnClickListener(view -> binding.getId.append("9"));

        binding.btnDel.setOnClickListener(view -> {
            if (binding.getId.getText().length() > 0) {
                String data = binding.getId.getText().toString();
                binding.getId.setText(data.substring(0, data.length() - 1));
                binding.getId.setSelection(binding.getId.getText().length());
            }
        });
        binding.btnDel.setOnLongClickListener(view -> {
            binding.getId.setText("");
            return false;
        });

        binding.btnEnter.setOnClickListener(view -> {
            student_id = binding.getId.getText().toString();
            url=letsEncrypt(student_id);
            letsDecrypt(url);
        });
    }
    private void checkedshuttlelQR(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(SSLUtil.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestService service = retrofit.create(RequestService.class);
        HashMap<String, Object> input = new HashMap<>();
        input.put("url", url);/*url*/
        input.put("state", type_for_server);
        input.put("shuttle_stop_name", campus);

        service.shuttleBus(input).enqueue(new Callback<CheckMessage>() {
            @Override
            public void onResponse(Call<CheckMessage> call, Response<CheckMessage> response) {
                if (response.isSuccessful() && response.body().getMESSSAGE().equals("정상 탑승 되었습니다.")) { //==null
                    Log.d("TAGGING", response.toString() + " / " + response.body().getMESSSAGE());
                    Toast.makeText(ShuttleBusActivity.this, response.body().getMESSSAGE(), Toast.LENGTH_SHORT).show();
                    playCheck();
                } else {
                    Toast.makeText(ShuttleBusActivity.this, response.body().getMESSSAGE(), Toast.LENGTH_SHORT).show();
                    playError();
                    playFail();
                    Log.d("TAGGING", response.toString() + " / " + response.body().getMESSSAGE());
                }
            }

            @Override
            public void onFailure(Call<CheckMessage> call, Throwable t) {
                Toast.makeText(ShuttleBusActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAGGING", "fail");
                Log.e("ERROR", t.getMessage().toString());
                playFail();
                playError();
            }
        });
    }
    private void initAnimation() {
        binding.aniView.setAnimation("qr.json");
        binding.aniView.setMaxFrame(148);
        binding.aniView.setSpeed(1f);
        binding.aniView.playAnimation();
    }

    private void playCheck() {
        binding.aniView.pauseAnimation();
        binding.aniView.setAnimation("check_animation.json");
        binding.aniView.playAnimation();
        binding.aniView.setSpeed(1.5f);

        binding.aniView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                initAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void playError() {
        binding.aniView.setAnimation("error.json");
        binding.aniView.playAnimation();
        //binding.animationView.setSpeed(1.5f);

        binding.aniView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                initAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void playFail() {
        ringtone.play();
    }

    private String letsEncrypt(String studentId) {

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        String nowDate = format.format(currentTime);
        String result="";

        try {
            result = AES256Util.AES_Encode(studentId + nowDate);
            //Log.d("Encrypted Source: ", "20141683" + nowDate);
            //Log.d("Encrypted Value: ", result);

        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void letsDecrypt(String url){ // DB작업
        String returnValue="00";
        String sid;
        try{
            String result;
            result = AES256Util.AES_Decode(url);
            if(result.length() == 22){
                Log.d("Decrypted",result);
                sid = result.substring(0,8);
                DbTime = result.substring(8);
                if(NetworkStatus.getConnectType(getApplicationContext())==-1){
                    service.DatabaseInsert(sid,type_for_server,campus,DbTime);
                }
                else if(NetworkStatus.getConnectType(getApplicationContext())==1 || NetworkStatus.getConnectType(getApplicationContext())==2){
                    checkedshuttlelQR(url);
                    service.DatabaseSelect();
                }
                else{
                    Toast.makeText(getApplicationContext(),"letsDecrypt Error 22",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Log.d("Decrypted",returnValue+result);
                sid = result.substring(0,6);
                DbTime = result.substring(6);
                if(NetworkStatus.getConnectType(getApplicationContext()) == -1){
                    service.DatabaseInsert(returnValue+sid,type_for_server,campus,DbTime);
                }
                else if(NetworkStatus.getConnectType(getApplicationContext())==1 || NetworkStatus.getConnectType(getApplicationContext())==2){
                    checkedshuttlelQR(letsEncrypt(returnValue+sid));
                    service.DatabaseSelect();
                }
                else{
                    Toast.makeText(getApplicationContext(),"letsDecrypt Error 20",Toast.LENGTH_SHORT).show();
                }
            }
        }catch(UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
}
