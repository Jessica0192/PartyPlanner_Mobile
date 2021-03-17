package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PartySupplyDB {

    // database constants
    public static final String DB_NAME = "PartyPlanner.db";
    public static final int    DB_VERSION = 1;

    public static int index = 0;
    public static final String TABLE_NAME = "supplyInfo";
    public static final String COL_ID = "_id";
    public static final int COL_ID_INDEX = index++;
    public static final String COL_ITEM = "supplyItem";
    public static final int COL_ITEM_INDEX = index++;

    // CREATE TABLE statement
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID   + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    COL_ITEM + " TEXT    NOT NULL " +
                    ");";

    // DROP TABLE statement
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TAG = "EventListActivity";
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(
                Context context,
                String name,
                SQLiteDatabase.CursorFactory factory,
                int version
        ) {
            super(context, name, factory, version);
            getWritableDatabase();
        }

        public void reset(SQLiteDatabase db){
            db.execSQL(PartyPlannerDB.DROP_TABLE);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            if (!isTableExists(TABLE_NAME, db))
            {
                db = getWritableDatabase();
                db.execSQL(CREATE_TABLE);
            }
        }

        @Override
        public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion, int newVersion
        ) {
            db.execSQL(PartyPlannerDB.DROP_TABLE);
            onCreate(db);
        }

        public boolean isTableExists(String tableName, SQLiteDatabase db) {
            if(db == null || !db.isOpen()) {
                db = getReadableDatabase();
            }
            if(!db.isReadOnly()) {
                db.close();
                db = getReadableDatabase();
            }

            String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'";
            try (Cursor cursor = db.rawQuery(query, null)) {
                if(cursor!=null) {
                    if(cursor.getCount()>0) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    // database and database helper objects
    private SQLiteDatabase db = null;
    private PartySupplyDB.DBHelper dbHelper = null;

    // constructor
    public PartySupplyDB(Context context) {
        dbHelper = new PartySupplyDB.DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        dbHelper.onCreate(db);
    }

    // private methods
    private void openReadableDB() {
        if(db == null || !db.isOpen()) {
            db = dbHelper.getReadableDatabase();
        }
        if(!db.isReadOnly()) {
            db.close();
            db = dbHelper.getReadableDatabase();
        }
    }

    private void openWriteableDB() {
        if(db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        if(db.isReadOnly()) {
            db.close();
            db = dbHelper.getWritableDatabase();
        }
    }

    public void closeDB() {
        if (db != null)
            db.close();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

    // public methods
    public ArrayList<List> getSupplies() {
        ArrayList<List> lists = new ArrayList<List>();
        openReadableDB();
        Cursor cursor = db.query(TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(cursor.getInt(COL_ID_INDEX)));
            list.add(cursor.getString(COL_ITEM_INDEX));
        }
        closeCursor(cursor);
        closeDB();
        return lists;
    }

    public String getSupplyDetails(
        int eventID
    ) {
        ArrayList<List> events = getSupplies();
        int eventCount;
        if (events.size() == 0)
        {
            return "<NO DATA>";
        }
        for (eventCount = 0; eventCount < events.size() ; )
        {
            eventCount ++;
        }
        if( eventID> eventCount || eventID < 0)
        {
            return "<INVALID ID>";
        }
        String rtnDetails = "";
        rtnDetails += "" +
                events.get(eventID).get(COL_ITEM_INDEX);
        return rtnDetails;
    }

    public long insertSupply(
        String supplyItem
    ) {
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM, supplyItem);
        long rowID = db.insert(TABLE_NAME, null, cv);
        this.closeDB();
        return rowID;
    }

    public int updateSupply(
            String eventId,
            String supplyItem
    ) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM, supplyItem);

        String where = COL_ID + "= ?";
        String[] whereArgs = { eventId };

        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteSupply(
        String eventId
    ) {
        int eventNum = Integer.parseInt(eventId);
        ArrayList<List> events = getSupplies();
        int eventCount;
        if (events.size() == 0)
        {
            return -1;
        }
        for (eventCount = 0; eventCount < events.size() ; )
        {
            eventCount ++;
        }
        if( eventNum> eventCount || eventNum < 0)
        {
            return -2;
        }

        String where = COL_ID + "= ?";
        String[] whereArgs = { eventId };

        this.openWriteableDB();
        int rowCount = db.delete(TABLE_NAME, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

}
