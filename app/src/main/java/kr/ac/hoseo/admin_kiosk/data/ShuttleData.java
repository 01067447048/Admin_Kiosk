package kr.ac.hoseo.admin_kiosk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShuttleData {
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("state")
    private String state;
    @Expose
    @SerializedName("shuttle_stop_name")
    private String shuttle_stop_name;
    @Expose
    @SerializedName("sid")
    private String sid;

    public ShuttleData(String url, String state, String shuttle_stop_name, String sid) {
        this.url = url;
        this.state = state;
        this.shuttle_stop_name = shuttle_stop_name;
        this.sid = sid;
    }

    public ShuttleData(String url, String state, String shuttle_stop_name) {
        this.url = url;
        this.state = state;
        this.shuttle_stop_name = shuttle_stop_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShuttle_stop_name() {
        return shuttle_stop_name;
    }

    public void setShuttle_stop_name(String shuttle_stop_name) {
        this.shuttle_stop_name = shuttle_stop_name;
    }

    /*public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }*/
}
