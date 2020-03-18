package kr.ac.hoseo.admin_kiosk;

import java.util.HashMap;

import kr.ac.hoseo.admin_kiosk.data.CheckData;
import kr.ac.hoseo.admin_kiosk.data.CheckMessage;
import kr.ac.hoseo.admin_kiosk.data.TicketDB;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface RequestService {

    @PATCH("/QR/shuttle/")
    Call<CheckMessage> shuttleBus(@Body HashMap<String,Object> map);

    @PATCH("/QR/commute")
    Call<TicketDB> schoolBus(@Query("ticket_id") String ticket_id);

    @GET("ticket/list")
    Call<CheckData> getTicket(@Query("sid") String sid);

}
