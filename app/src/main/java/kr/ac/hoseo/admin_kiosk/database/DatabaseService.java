package kr.ac.hoseo.admin_kiosk.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import kr.ac.hoseo.admin_kiosk.RequestService;
import kr.ac.hoseo.admin_kiosk.SSLUtil;
import kr.ac.hoseo.admin_kiosk.data.CheckMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseService {

    private Context context;
    Database db;
    SQLiteDatabase sqldb;
    private String TAG = "DATABASE";
    private String type,campus,sid,DbTime;
    private final String BASE_URL = "https://hub.hsu.ac.kr";

    public void DatabaseInit(Context context){
        db = new Database(context);
        sqldb = db.getWritableDatabase();
        if(sqldb.isOpen()){
            sqldb.close();
            return;
        }
        else{
            db.onUpgrade(sqldb,1,2);
            sqldb.close();
        }
    }

    public void DatabaseSelect(){
        sqldb = db.getReadableDatabase();
        if(sqldb.isOpen()){
            Cursor cursor;
            cursor = sqldb.rawQuery("SELECT * FROM Shuttle3",null);
            while(cursor.moveToNext()){
                Log.d(TAG,"Select : "+cursor.getString(0)+" / "+cursor.getString(1)+" / "+cursor.getString(2)+" / "+cursor.getString(3));
            }
            cursor.close();
            sqldb.close();
        }
        else{
            return;
        }

    }

    public void DatabaseInsert(String sid, String type, String campus, String time){
        sqldb = db.getWritableDatabase();
        if(sqldb.isOpen()){
            sqldb.execSQL("INSERT INTO Shuttle3 VALUES ('"+sid+"','"+type+"','"+campus+"','"+time+"');");
            sqldb.close();
            DatabaseSelect();
        }
        else{
            return;
        }

    }

    public void DatabaseReset(){
        sqldb=db.getWritableDatabase();
        if(sqldb.isOpen()){
            sqldb.execSQL("DELETE FROM Shuttle3");
            sqldb.close();
            DatabaseSelect();
        }
        else{
            return;
        }
    }

    public void DatabaseDelete(String time){
        sqldb = db.getWritableDatabase();
        if(sqldb.isOpen()){
            sqldb.execSQL("DELETE FROM Shuttle3 WHERE Time='" + time +"'");
            sqldb.close();
        }
        else{
            return;
        }
    }

    public void SendServer(){
        sqldb = db.getReadableDatabase();
        if(sqldb.isOpen()){
            Cursor cursor;
            String result = "";
            cursor = sqldb.rawQuery("SELECT * FROM Shuttle3",null);

            while(cursor.moveToNext()){
                result += cursor.getString(0);
            }

            if(result.equals("")){
                DatabaseReset();
            }
            else{
                cursor.moveToFirst();
                sid = cursor.getString(0);
                type = cursor.getString(1);
                campus = cursor.getString(2);
                DbTime = cursor.getString(3);
                DatabaseDelete(DbTime);
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String nowDate = format.format(currentTime);

                try{
                    result = AES256Util.AES_Encode(sid+nowDate);
                }catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e){
                    e.printStackTrace();
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(SSLUtil.getUnsafeOkHttpClient().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RequestService service = retrofit.create(RequestService.class);
                HashMap<String, Object> input = new HashMap<>();
                input.put("url", result);/*url*/
                input.put("state", type);
                input.put("shuttle_stop_name", campus);

                service.shuttleBus(input).enqueue(new Callback<CheckMessage>() {
                    @Override
                    public void onResponse(Call<CheckMessage> call, Response<CheckMessage> response) {
                        if (response.isSuccessful() && response.body().getMESSSAGE().equals("정상 탑승 되었습니다.")) { //==null
                            Log.d(TAG, response.toString() + " / " + response.body().getMESSSAGE());
                            DatabaseSelect();
                        } else {
                            Log.d(TAG, response.toString() + " / " + response.body().getMESSSAGE());
                            DatabaseSelect();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckMessage> call, Throwable t) {
                        Log.d("TAGGING", "fail");
                        Log.e("ERROR", t.getMessage().toString());
                        DatabaseSelect();
                    }
                });

            }
            cursor.close();
            sqldb.close();
        }
        else{
            return;
        }
    }

}
