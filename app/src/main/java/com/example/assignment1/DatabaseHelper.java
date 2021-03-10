package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "plannerInfo";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "eventName";
    public static final String COL_3 = "eventType";
    public static final String COL_4 = "date";
    public static final String COL_5 = "address";
    public static final String COL_6 = "guests";
    public static final String COL_7 = "menu";
    public static final String COL_8 = "supplies";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table plannerInfo (_id integer primary key autoincrement, eventName text, " +
                "eventType text, date text, address text, guests text, menu text, supplies text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           //if exist do something
    }

    /*  -- Function Header Comment
    Name	:   insertInfo (String eventName , String eventType, String date, String address, String guests, String menu, String supplies)
    Purpose :   To insert to the database
    Inputs	:	String   eventName
                String   eventType
                String   date
                String   address
                String   guests
                String   menu
                String   supplies
    Outputs	:	NONE
    Returns	:	NONE
    */
    public boolean insertInfo (String eventName , String eventType, String date, String address, String guests, String menu, String supplies)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cntValues = new ContentValues();
        cntValues.put(COL_2, eventName);
        cntValues.put(COL_3, eventType);
        cntValues.put(COL_4, date);
        cntValues.put(COL_5, address);
        cntValues.put(COL_6, guests);
        cntValues.put(COL_7, menu);
        cntValues.put(COL_8, supplies);
        long returnCode = db.insert(TABLE_NAME, null, cntValues);

        //to make sure the insertion was successful
        if (returnCode == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

}
