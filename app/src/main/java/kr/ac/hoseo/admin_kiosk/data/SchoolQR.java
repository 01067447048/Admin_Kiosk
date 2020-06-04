package kr.ac.hoseo.admin_kiosk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolQR {
    @SerializedName("message")
    @Expose
    private String MESSSAGE;


    public SchoolQR(String MESSSAGE) {
        this.MESSSAGE = MESSSAGE;
    }

    public String getMESSSAGE() {
        return MESSSAGE;
    }

    public void setMESSSAGE(String MESSSAGE) {
        this.MESSSAGE = MESSSAGE;
    }
}