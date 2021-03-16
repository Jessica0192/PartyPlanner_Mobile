package com.example.assignment1;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PartyPlannerDB {
//    public static final String TAG = "DB";

    // database constants
    public static final String DB_NAME = "PartyPlanner.db";
    public static final int    DB_VERSION = 1;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // plannerInfo Table
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static int index = 0;
    public static final String TABLE_NAME = "plannerInfo";
    //
    public static final String COL_ID = "_id";
    public static final int COL_ID_INDEX = index++;
    //
    public static final String COL_NAME = "eventName";
    public static final int COL_NAME_INDEX = index++;
    //
    public static final String COL_TYPE = "eventType";
    public static final int COL_TYPE_INDEX = index++;
    //
    public static final String COL_DATE = "eventDate";
    public static final int COL_DATE_INDEX = index++;
    //
    public static final String COL_ADDRESS = "eventAddress";
    public static final int COL_ADDRESS_INDEX= index++;
    //
    public static final String COL_GUEST = "eventGuest";
    public static final int COL_GUEST_INDEX = index++;
    //
    public static final String COL_MENU = "eventMenu";
    public static final int COL_MENU_INDEX = index++;
    //
    public static final String COL_SUPPLY = "eventSupply";
    public static final int COL_SUPPLY_INDEX = index++;

    // CREATE TABLE statement
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID   + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT    NOT NULL, " +
                    COL_TYPE + " TEXT    NOT NULL, " +
                    COL_DATE + " TEXT    NOT NULL, " +
                    COL_ADDRESS + " TEXT    NOT NULL, " +
                    COL_GUEST + " TEXT    NOT NULL, " +
                    COL_MENU + " TEXT    NOT NULL, " +
                    COL_SUPPLY + " TEXT    NOT NULL" +
                    ");";

    // DROP TABLE statement
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // plannerInfo Table
    ///////////////////////////////////////////////////////////////////////////////////////////////

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
            Log.d(TAG, "==================================================== DB onCreate ...");

            ////////////////TEST EVENTS/////////////////
            //db.execSQL("INSERT INTO plannerInfo VALUES (1, 'eventName1','eventType1','eventDate1','eventAddress1','eventGuest1a, eventGuest1a ','eventMenu1, eventMenu2','eventSupply1')");
            //db.execSQL("INSERT INTO plannerInfo VALUES (2, 'eventName2','eventType2','eventDate2','eventAddress2','eventGuest2','eventMenu2','eventSupply2')");
            //db.execSQL("INSERT INTO plannerInfo VALUES (3, 'eventName3','eventType3','eventDate3','eventAddress3','eventGuest3','eventMenu3','eventSupply3')");

            if (!isTableExists(TABLE_NAME, db))
            {
                db = getWritableDatabase();
                db.execSQL(CREATE_TABLE);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Event list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

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
    private PartyPlannerDB.DBHelper dbHelper = null;

    // constructor
    public PartyPlannerDB(Context context) {
        dbHelper = new PartyPlannerDB.DBHelper(context, DB_NAME, null, DB_VERSION);



//        // Validate insertEvent Function
//        insertEvent(
//            "eventName1",
//            "eventType1",
//            "eventDate1",
//            "eventAddress1",
//            "eventGuest1",
//            "eventMenu1",
//            "eventSupply1"
//        )
        // Validate insertEvent Function
        openWriteableDB();
        dbHelper.onCreate(db);
//        insertEvent(
//            "eventName1", "eventType1", "eventDate1", "eventAddress1",
//            "eventGuest1", "eventMenu1", "eventSupply1"
//        );

        Log.d(TAG, "==================================================== DB constructor ...");
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
    public ArrayList<List> getEvents() {
        ArrayList<List> lists = new ArrayList<List>();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM plannerInfo;";

        Cursor cursor = db.rawQuery(selectQuery, null);

        //Cursor cursor = db.query(TABLE_NAME,
                //null, null, null, null, null, null);
        cursor.moveToFirst();
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(String.valueOf(cursor.getInt(COL_ID_INDEX)));
            list.add(cursor.getString(COL_NAME_INDEX));
            list.add(cursor.getString(COL_TYPE_INDEX));
            list.add(cursor.getString(COL_DATE_INDEX));
            list.add(cursor.getString(COL_ADDRESS_INDEX));
            list.add(cursor.getString(COL_GUEST_INDEX));
            list.add(cursor.getString(COL_MENU_INDEX));
            list.add(cursor.getString(COL_SUPPLY_INDEX));
            lists.add(list);
        }

        closeCursor(cursor);
        closeDB();
        return lists;
    }

    public String getFormattedEventsSummary() {
        //dbHelper.reset(dbHelper.getWritableDatabase());
        ArrayList<List> events = getEvents();

        if (events.size() == 0)
        {
            return "<NO DATA>";
        }
        String rtnEvents = "";
        for (int eventCount = 0; eventCount < events.size() ; ) {
            if (eventCount != 0)
            {
                rtnEvents += ";";
            }
            rtnEvents += "" + (eventCount+1) + " . " +
                    events.get(eventCount).get(COL_NAME_INDEX) + " : " +
                    events.get(eventCount).get(COL_DATE_INDEX);
            eventCount++;
        }
        return rtnEvents;
    }

    public String getEventDetails(int eventID) {
        //dbHelper.reset(dbHelper.getWritableDatabase());
        ArrayList<List> events = getEvents();
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
                events.get(eventID).get(COL_NAME_INDEX) + "\r\n" +
                events.get(eventID).get(COL_TYPE_INDEX) + "\r\n" +
                events.get(eventID).get(COL_DATE_INDEX) + "\r\n" +
                events.get(eventID).get(COL_ADDRESS_INDEX) + "\r\n" +
                events.get(eventID).get(COL_GUEST_INDEX) + "\r\n" +
                events.get(eventID).get(COL_MENU_INDEX) + "\r\n" +
                events.get(eventID).get(COL_SUPPLY_INDEX);
        return rtnDetails;
    }

    public long insertEvent(
            String eventName,
            String eventType,
            String eventDate,
            String eventAddress,
            String eventGuest,
            String eventMenu,
            String eventSupply
    ) {
        db = dbHelper.getWritableDatabase();

        Log.d(TAG, "==================================================== INSERT EVENT ...");
        ContentValues cv = new ContentValues();
//        cv.put(TASK_LIST_ID, task.getListId());
        cv.put(COL_NAME, eventName);
        cv.put(COL_TYPE, eventType);
        cv.put(COL_DATE, eventDate);
        cv.put(COL_ADDRESS, eventAddress);
        cv.put(COL_GUEST, eventGuest);
        cv.put(COL_MENU, eventMenu);
        cv.put(COL_SUPPLY, eventSupply);
        //this.openWriteableDB();
        long rowID = db.insert(TABLE_NAME, null, cv);
        Log.d(TAG, cv.toString());




        ////////////To check if the data is inserted into database/////////////////
        String selectQuery = "SELECT * FROM plannerInfo;";
        String value = null;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
           value = cursor.getString(3);
        }

        Log.d(TAG, "HERE: "+value);

        this.closeDB();
        return rowID;
    }


    public int updateEvent(
            String eventId,
            String eventName,
            String eventType,
            String eventDate,
            String eventAddress,
            String eventGuest,
            String eventMenu,
            String eventSupply
    ) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, eventName);
        cv.put(COL_TYPE, eventType);
        cv.put(COL_DATE, eventDate);
        cv.put(COL_ADDRESS, eventAddress);
        cv.put(COL_GUEST, eventGuest);
        cv.put(COL_MENU, eventMenu);
        cv.put(COL_SUPPLY, eventSupply);

        String where = COL_ID + "= ?";
        String[] whereArgs = { eventId };

        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

//    public int deleteEvent(
//            String eventId
//    ) {
//        String where = COL_ID + "= ?";
//        String[] whereArgs = { eventId };
//
//        this.openWriteableDB();
//        int rowCount = db.delete(TABLE_NAME, where, whereArgs);
//        this.closeDB();
//
//        return rowCount;
//    }

    public int deleteEvent(
            String eventId
    ) {
        int eventNum = Integer.parseInt(eventId);
        ArrayList<List> events = getEvents();
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
