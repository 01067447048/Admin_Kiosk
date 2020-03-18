package kr.ac.hoseo.admin_kiosk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketDB {

    @SerializedName("TICKET_ID")
    @Expose
    private int TICKETID;
    @SerializedName("STUDENT_ID")
    @Expose
    private String STUDENTID;
    @SerializedName("BUS_ID")
    @Expose
    private int BUSID;
    @SerializedName("START")
    @Expose
    private String START;
    @SerializedName("END")
    @Expose
    private String END;
    @SerializedName("TICKET_DATE")
    @Expose
    private String TICKET_DATE;
    @SerializedName("TICKET_TIME")
    @Expose
    private String TICKETTIME;
    @SerializedName("RESERVATION_DATE")
    @Expose
    private String RESERVATIONDATE;
    @SerializedName("SEAT")
    @Expose
    private int SEAT;
    @SerializedName("BOARDING")
    @Expose
    private String BOARDING;
    @SerializedName("PRICE")
    @Expose
    private Integer PRICE;

    public TicketDB(int TICKETID, String STUDENTID, int BUSID, String START, String END, String TICKET_DATE, String TICKETTIME, String RESERVATIONDATE, int SEAT, String BOARDING, int PRICE) {
        this.TICKETID = TICKETID;
        this.STUDENTID = STUDENTID;
        this.BUSID = BUSID;
        this.START = START;
        this.END = END;
        this.TICKET_DATE = TICKET_DATE;
        this.TICKETTIME = TICKETTIME;
        this.RESERVATIONDATE = RESERVATIONDATE;
        this.SEAT = SEAT;
        this.BOARDING = BOARDING;
        this.PRICE = PRICE;
    }

    public int getTICKETID() {
        return TICKETID;
    }

    public void setTICKETID(int TICKETID) {
        this.TICKETID = TICKETID;
    }

    public String getSTUDENTID() {
        return STUDENTID;
    }

    public void setSTUDENTID(String STUDENTID) {
        this.STUDENTID = STUDENTID;
    }

    public int getBUSID() {
        return BUSID;
    }

    public void setBUSID(int BUSID) {
        this.BUSID = BUSID;
    }

    public String getSTART() {
        return START;
    }

    public void setSTART(String START) {
        this.START = START;
    }

    public String getEND() {
        return END;
    }

    public void setEND(String END) {
        this.END = END;
    }

    public String getTICKET_DATE() {
        return TICKET_DATE;
    }

    public void setTICKET_DATE(String TICKET_DATE) {
        this.TICKET_DATE = TICKET_DATE;
    }

    public String getTICKETTIME() {
        return TICKETTIME;
    }

    public void setTICKETTIME(String TICKETTIME) {
        this.TICKETTIME = TICKETTIME;
    }

    public String getRESERVATIONDATE() {
        return RESERVATIONDATE;
    }

    public void setRESERVATIONDATE(String RESERVATIONDATE) {
        this.RESERVATIONDATE = RESERVATIONDATE;
    }

    public int getSEAT() {
        return SEAT;
    }

    public void setSEAT(int SEAT) {
        this.SEAT = SEAT;
    }

    public String getBOARDING() {
        return BOARDING;
    }

    public void setBOARDING(String BOARDING) {
        this.BOARDING = BOARDING;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public void setPRICE(Integer PRICE) {
        this.PRICE = PRICE;
    }
}
