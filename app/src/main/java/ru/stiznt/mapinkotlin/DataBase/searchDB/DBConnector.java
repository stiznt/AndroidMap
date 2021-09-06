package ru.stiznt.mapinkotlin.DataBase.searchDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.stiznt.mapinkotlin.Models.Cabinet;

public class DBConnector{

    private SearchDBHelper searchDBHelper;
    private ContentValues contentValues;
    private SQLiteDatabase database;

    public DBConnector(Context context) {
        searchDBHelper = new SearchDBHelper(context);
        database = searchDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    public void addDB(Cabinet cabinet){
        ArrayList<Cabinet> linesDB = new ArrayList<Cabinet>();
        linesDB.addAll(readDB());

        if(!linesDB.contains(cabinet)){
            contentValues.put(SearchDBHelper.KEY_COMMAND, cabinet.getName());
            contentValues.put(SearchDBHelper.KEY_COMMAND_ID, cabinet.getId());
            database.insert(SearchDBHelper.TABLE_CONTACTS, null, contentValues);
        }
    }

    public ArrayList<Cabinet> readDB(){
        ArrayList<Cabinet> linesDB = new ArrayList();
        Cursor cursor = database.query(SearchDBHelper.TABLE_CONTACTS, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()){
            int commandIndex = cursor.getColumnIndex(SearchDBHelper.KEY_COMMAND);
            int idIndex = cursor.getColumnIndex(SearchDBHelper.KEY_COMMAND_ID);
            do {
                linesDB.add(new Cabinet(cursor.getString(commandIndex), cursor.getInt(idIndex)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return linesDB;
    }
}
