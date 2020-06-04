package kr.ac.hoseo.admin_kiosk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Shuttle3 (Sid CHAR(8), Type CHAR(10), Campus CHAR(20), Time CHAR(14))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Shuttle3");
        onCreate(db);
    }

    public Database(@Nullable Context context) {
        super(context, "Shuttle3", null, 1);
    }
}
