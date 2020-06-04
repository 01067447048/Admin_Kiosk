package kr.ac.hoseo.admin_kiosk.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QRData {
    @SerializedName("generatedMaps")
    @Expose
    String[] GeneratedMaps;
    @SerializedName("raw")
    @Expose
    Raw raw;

    public QRData(String[] generatedMaps, Raw raw) {
        GeneratedMaps = generatedMaps;
        this.raw = raw;
    }

    public String[] getGeneratedMaps() {
        return GeneratedMaps;
    }

    public void setGeneratedMaps(String[] generatedMaps) {
        GeneratedMaps = generatedMaps;
    }

    public Raw getRaw() {
        return raw;
    }

    public void setRaw(Raw raw) {
        this.raw = raw;
    }
}
