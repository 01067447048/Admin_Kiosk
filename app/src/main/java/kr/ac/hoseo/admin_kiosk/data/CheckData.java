package kr.ac.hoseo.admin_kiosk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckData {
    @SerializedName("TICKET")
    @Expose
    private TicketDB ticketDB;

    public CheckData(TicketDB ticketDB) {
        this.ticketDB = ticketDB;
    }

    public TicketDB getTicketDB() {
        return ticketDB;
    }

    public void setTicketDB(TicketDB ticketDB) {
        this.ticketDB = ticketDB;
    }
}