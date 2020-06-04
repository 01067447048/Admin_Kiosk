package kr.ac.hoseo.admin_kiosk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Raw {
    @SerializedName("fieldCount")
    @Expose
    int Count;
    @SerializedName("affectedRows")
    @Expose
    int Rows;
    @SerializedName("insertId")
    @Expose
    int Id;
    @SerializedName("info")
    @Expose
    String info;
    @SerializedName("serverStatus")
    @Expose
    int ServerStatus;
    @SerializedName("warningStatus")
    @Expose
    int WarningStatus;
    @SerializedName("changedRows")
    @Expose
    int ChangedRows;

    public Raw(int count, int rows, int id, String info, int serverStatus, int warningStatus, int changedRows) {
        Count = count;
        Rows = rows;
        Id = id;
        this.info = info;
        ServerStatus = serverStatus;
        WarningStatus = warningStatus;
        ChangedRows = changedRows;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getRows() {
        return Rows;
    }

    public void setRows(int rows) {
        Rows = rows;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getServerStatus() {
        return ServerStatus;
    }

    public void setServerStatus(int serverStatus) {
        ServerStatus = serverStatus;
    }

    public int getWarningStatus() {
        return WarningStatus;
    }

    public void setWarningStatus(int warningStatus) {
        WarningStatus = warningStatus;
    }

    public int getChangedRows() {
        return ChangedRows;
    }

    public void setChangedRows(int changedRows) {
        ChangedRows = changedRows;
    }
}
