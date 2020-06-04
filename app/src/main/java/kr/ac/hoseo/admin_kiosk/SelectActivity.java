package kr.ac.hoseo.admin_kiosk;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import kr.ac.hoseo.admin_kiosk.databinding.ActivitySelectBinding;
import kr.ac.hoseo.admin_kiosk.ui.SchoolBusActivity;
import kr.ac.hoseo.admin_kiosk.ui.ShuttleBusActivity;

public class SelectActivity extends AppCompatActivity {

    ActivitySelectBinding binding;
    private String campus;
    private String type,bus_type;
    private boolean isCheckedcampus = false, isCheckedbus = false, isCheckeddirve = false;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select);

        binding.Asan.setOnClickListener(view->{
            binding.Asan.setTextColor(Color.WHITE);
            binding.Asan.setBackgroundResource(R.drawable.letf);
            binding.Cheonan.setTextColor(Color.argb(255,112,112,112));
            binding.Cheonan.setBackgroundResource(0);
            campus = "아산캠퍼스";
            isCheckedcampus = true;
        });

        binding.Cheonan.setOnClickListener(view->{
            binding.Cheonan.setTextColor(Color.WHITE);
            binding.Cheonan.setBackgroundResource(R.drawable.right);
            binding.Asan.setTextColor(Color.argb(255,112,112,112));
            binding.Asan.setBackgroundResource(0);
            campus = "천안캠퍼스";
            isCheckedcampus = true;
        });

        binding.School.setOnClickListener(view->{
            binding.School.setTextColor(Color.WHITE);
            binding.School.setBackgroundResource(R.drawable.letf);
            binding.Shuttle.setTextColor(Color.argb(255,112,112,112));
            binding.Shuttle.setBackgroundResource(0);
            isCheckedbus = true;
            bus_type = "통학";
        });

        binding.Shuttle.setOnClickListener(view->{
            binding.Shuttle.setTextColor(Color.WHITE);
            binding.Shuttle.setBackgroundResource(R.drawable.right);
            binding.School.setTextColor(Color.argb(255,112,112,112));
            binding.School.setBackgroundResource(0);
            isCheckedbus = true;
            bus_type = "셔틀";
        });

        binding.rideup.setOnClickListener(view->{
            binding.rideup.setTextColor(Color.WHITE);
            binding.rideup.setBackgroundResource(R.drawable.letf);
            binding.ridedown.setTextColor(Color.argb(255,112,112,112));
            binding.ridedown.setBackgroundResource(0);
            type = "승차";
            isCheckeddirve = true;
        });

        binding.ridedown.setOnClickListener(view->{
            binding.ridedown.setTextColor(Color.WHITE);
            binding.ridedown.setBackgroundResource(R.drawable.right);
            binding.rideup.setTextColor(Color.argb(255,112,112,112));
            binding.rideup.setBackgroundResource(0);
            type = "하차";
            isCheckeddirve = true;
        });

        binding.StartLayout.setOnClickListener(view->{
            if(bus_type.equals("셔틀")){
                Intent intent = new Intent(this, ShuttleBusActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("campus",campus);
                intent.putExtra("bus_type",bus_type);
                if(isCheckedcampus && isCheckedbus && isCheckeddirve){
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SelectActivity.this, "누락된 정보를 확인해 주세요",Toast.LENGTH_SHORT).show();
                }
            }
            else if(bus_type.equals("통학")){
                Intent intent = new Intent(this, SchoolBusActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("campus",campus);
                intent.putExtra("bus_type",bus_type);
                if(isCheckedcampus && isCheckedbus && isCheckeddirve){
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SelectActivity.this, "누락된 정보를 확인해 주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        isCheckeddirve = false;
        isCheckedbus = false;
        isCheckedcampus = false;
        campus = "";
        type = "";

        binding.Cheonan.setTextColor(Color.argb(255,112,112,112));
        binding.Cheonan.setBackgroundResource(0);
        binding.Asan.setTextColor(Color.argb(255,112,112,112));
        binding.Asan.setBackgroundResource(0);
        binding.Shuttle.setTextColor(Color.argb(255,112,112,112));
        binding.Shuttle.setBackgroundResource(0);
        binding.School.setTextColor(Color.argb(255,112,112,112));
        binding.School.setBackgroundResource(0);
        binding.rideup.setTextColor(Color.argb(255,112,112,112));
        binding.rideup.setBackgroundResource(0);
        binding.ridedown.setTextColor(Color.argb(255,112,112,112));
        binding.ridedown.setBackgroundResource(0);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        dialog.show();
    }

    private Dialog FinishedApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogtitle)
                .setMessage(R.string.message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }

}
