package ru.stiznt.mapinkotlin.DataBase.officeDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.stiznt.mapinkotlin.Models.Cabinet;

public class DBConnector{

    private OfficeDBHelper officeDBHelper;
    private ContentValues contentValues;
    private SQLiteDatabase database;

    public DBConnector(Context context) {
        officeDBHelper = new OfficeDBHelper(context);
        database = officeDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    public void addDB(Cabinet cabinet){
        contentValues.put(OfficeDBHelper.KEY_FIO, "fio");
        contentValues.put(OfficeDBHelper.KEY_CABINET, cabinet.getName());
        contentValues.put(OfficeDBHelper.KEY_CABINET_ID, cabinet.getId());
        database.insert(OfficeDBHelper.TABLE_CONTACTS, null, contentValues);
    }

    public ArrayList<Cabinet> readDB(){
        ArrayList<Cabinet> linesDB = new ArrayList<Cabinet>();
        Cursor cursor = database.query(OfficeDBHelper.TABLE_CONTACTS, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()){
            do {

                linesDB.add(new Cabinet(cursor.getString(cursor.getColumnIndex(OfficeDBHelper.KEY_CABINET)),
                        cursor.getInt(cursor.getColumnIndex(OfficeDBHelper.KEY_CABINET_ID))));
            } while (cursor.moveToNext());
        }
            cursor.close();
        return linesDB;
    }
}

