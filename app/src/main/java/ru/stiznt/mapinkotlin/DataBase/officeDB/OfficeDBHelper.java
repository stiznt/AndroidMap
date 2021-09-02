package ru.stiznt.mapinkotlin.DataBase.officeDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OfficeDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactOfficeDb";
    public static final String TABLE_CONTACTS = "office";

    public static final String KEY_ID = "_id";
    public static final String KEY_FIO = "fio";
    public static final String KEY_CABINET = "cabinet";
    public static final String KEY_FREE = "free";

    public OfficeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_FIO + " text," + KEY_CABINET + " text," + KEY_FREE + " text"  + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
}