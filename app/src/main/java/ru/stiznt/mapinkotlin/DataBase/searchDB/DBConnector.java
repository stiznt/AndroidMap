package ru.stiznt.mapinkotlin.DataBase.searchDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBConnector{

    private SearchDBHelper searchDBHelper;
    private ContentValues contentValues;
    private SQLiteDatabase database;

    public DBConnector(Context context) {
        searchDBHelper = new SearchDBHelper(context);
        database = searchDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    public void addDB(String command){
        ArrayList<String> linesDB = new ArrayList<String>();
        linesDB.addAll(readDB());

        if(!linesDB.contains(command)){
            contentValues.put(SearchDBHelper.KEY_COMMAND, command);
            database.insert(SearchDBHelper.TABLE_CONTACTS, null, contentValues);
        }
    }

    public ArrayList<String> readDB(){
        ArrayList<String> linesDB = new ArrayList();
        Cursor cursor = database.query(SearchDBHelper.TABLE_CONTACTS, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()){
            int commandIndex = cursor.getColumnIndex(SearchDBHelper.KEY_COMMAND);
            do {
                linesDB.add(cursor.getString(commandIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return linesDB;
    }
}
