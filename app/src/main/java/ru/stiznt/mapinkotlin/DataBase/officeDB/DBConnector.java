package ru.stiznt.mapinkotlin.DataBase.officeDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBConnector{

    private OfficeDBHelper officeDBHelper;
    private ContentValues contentValues;
    private SQLiteDatabase database;

    public DBConnector(Context context) {
        officeDBHelper = new OfficeDBHelper(context);
        database = officeDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    public void addDB(String fio, String cabinet, String free){
        contentValues.put(OfficeDBHelper.KEY_FIO, fio);
        contentValues.put(OfficeDBHelper.KEY_CABINET, cabinet);
        contentValues.put(OfficeDBHelper.KEY_FREE, free);
        database.insert(OfficeDBHelper.TABLE_CONTACTS, null, contentValues);
    }

    public ArrayList<String> readDB(){
        ArrayList<String> linesDB = new ArrayList();
        Cursor cursor = database.query(OfficeDBHelper.TABLE_CONTACTS, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()){
            do {
                linesDB.add(cursor.getString(cursor.getColumnIndex(OfficeDBHelper.KEY_FIO))
                        + " " + cursor.getString(cursor.getColumnIndex(OfficeDBHelper.KEY_CABINET))
                        + " " + cursor.getString(cursor.getColumnIndex(OfficeDBHelper.KEY_FREE)));
            } while (cursor.moveToNext());
        }
            cursor.close();
        return linesDB;
    }
}

