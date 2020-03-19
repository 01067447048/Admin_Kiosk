package kr.ac.hoseo.admin_kiosk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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

import kr.ac.hoseo.admin_kiosk.data.CheckData;
import kr.ac.hoseo.admin_kiosk.data.CheckMessage;
import kr.ac.hoseo.admin_kiosk.data.TicketDB;
import kr.ac.hoseo.admin_kiosk.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String student_id;
    private String BASE_URL = "https://hub.hsu.ac.kr";
    private String type,campus,bus_type,type_for_server;
    private Date current;
    private String time;
    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                current = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 /  HH:mm:ss");
                time = format.format(current);

                binding.time.setText(time);
            }
        };

        class NewRunnable implements Runnable{
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
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

        if(type.equals("승차")){
            type_for_server = "하교";
        }
        else{
            type_for_server = "등교";
        }

        binding.data.setText(campus + " / " + bus_type + " / " + type+"("+type_for_server+")");

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
            if (student_id.length() > 5) {
                if (bus_type.equals("통학")) {
                    usingStudentIdSchool(student_id);
                } else {
                    letsEncrypte(student_id);
                }
                binding.getId.setText("");
            } else {
                binding.getId.setError("학번을 확인해 주세요!");
                binding.getId.setClickable(false);
            }
        });

    }

    private void letsEncrypte(String studentId) {

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); //형식 맞추기
        String nowDate = format.format(currentTime);

        try {
            String result;
            result = AES256Util.AES_Encode(studentId + nowDate); //이거를 QR로 만들어 제작하기
            //TODO 30초마다 QR코드 리셋하
            Log.d("TAGGING", result);
            checkedshuttlelQR(result);
        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
        input.put("state", /*type_for_server*/"등교");
        input.put("shuttle_stop_name", /*campus*/"아산캠퍼스");

        service.shuttleBus(input).enqueue(new Callback<CheckMessage>() {
            @Override
            public void onResponse(Call<CheckMessage> call, Response<CheckMessage> response) {
                //CheckMessage message = response.body();
                if (response.isSuccessful()&&response.body().getMESSSAGE().equals("정상 탑승 되었습니다.")) { //==null
                    Log.d("TAGGING", response.toString()+" / "+response.body().getMESSSAGE());
                    Toast.makeText(MainActivity.this, response.body().getMESSSAGE(), Toast.LENGTH_SHORT).show();
                    playCheck();
                } else {
                    Toast.makeText(MainActivity.this, response.body().getMESSSAGE(), Toast.LENGTH_SHORT).show();
                    playError();
                    Log.d("TAGGING", response.toString()+" / "+response.body().getMESSSAGE());
                }
            }

            @Override
            public void onFailure(Call<CheckMessage> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAGGING", "fail");
                Log.e("ERROR", t.getMessage().toString());
            }
        });
    }

    private void usingStudentIdSchool(String sid) {

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //형식 맞추기
        String now = format.format(currentTime);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(SSLUtil.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestService service = retrofit.create(RequestService.class);
        service.getTicket(sid).enqueue(new Callback<CheckData>() {
            @Override
            public void onResponse(Call<CheckData> call, Response<CheckData> response) {
                if(response.isSuccessful()){
                    if(response.body().getTicketDB() == null){
                        playError();
                        Toast.makeText(MainActivity.this,"오늘 티켓이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d("TIME",response.body().getTicketDB().getTICKET_DATE()+now);
                        if(now.equals(response.body().getTicketDB().getTICKET_DATE())){
                            if((response.body().getTicketDB().getSTART().equals("아산캠퍼스")||response.body().getTicketDB().getSTART().equals("천안캠퍼스"))&&type_for_server.equals("하교")){
                                checkedshcoolQR(String.valueOf(response.body().getTicketDB().getTICKETID()));
                            }
                            else if((response.body().getTicketDB().getEND().equals("아산캠퍼스")||response.body().getTicketDB().getEND().equals("천안캠퍼스"))&&type_for_server.equals("등교")){
                                checkedshcoolQR(String.valueOf(response.body().getTicketDB().getTICKETID()));
                            }
                            else{
                                playError();
                                Toast.makeText(MainActivity.this,"승하차를 확인 해 주세요.",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            playError();
                            Toast.makeText(MainActivity.this,"오늘 티켓이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"실패",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<CheckData> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });

    }

    private void checkedshcoolQR(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(SSLUtil.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestService service = retrofit.create(RequestService.class);

        service.schoolBus(id).enqueue(new Callback<TicketDB>() {
            @Override
            public void onResponse(Call<TicketDB> call, Response<TicketDB> response) {
                //Toast.makeText(ReaderActivity.this, "Checked",Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    //Toast.makeText(ReaderActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                    playCheck();
                } else {
                    Toast.makeText(MainActivity.this, "Checked Error", Toast.LENGTH_SHORT).show();
                    playError();
                }
            }

            @Override
            public void onFailure(Call<TicketDB> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
