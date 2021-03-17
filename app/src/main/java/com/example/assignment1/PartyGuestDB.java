/*
 * FILE          : PartyGuestDB.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2020-03-19
 * DESCRIPTION   : This file contains the definition and functions of the guestInfo table
 *                 in the PartyPlanner database
 */

package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
 * NAME     :   PartyGuestDB
 * PURPOSE :    PartyGuestDB class contains the definition and functions of the menuInfo table
 *              in the PartyPlanner database
 */
public class PartyGuestDB {

    // Database constants
    public static final String DB_NAME = "PartyPlanner.db";
    public static final int    DB_VERSION = 1;
    public static int index = 0;
    public static final String TABLE_NAME = "guestInfo";
    public static final String COL_ID = "_id";
    public static final int COL_ID_INDEX = index++;
    public static final String COL_NAME = "guestName";
    public static final int COL_NAME_INDEX = index++;
    // Constants for error code
    private static final int ERROR_EMPTY =  -1;
    private static final int ERROR_INVALID =  -2;

    // Database and database helper objects
    private SQLiteDatabase db = null;
    private PartyGuestDB.DBHelper dbHelper = null;

    // CREATE TABLE statement
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID   + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT    NOT NULL " +
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

        /*
         * FUNCTION: onCreate
         * DESCRIPTION:
         *      This function is going to be called as default when this page is loaded
         * PARAMETER:
         *      SQLiteDatabase db
         * RETURNS:
         *      void: there's no return value
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            if (!isTableExists(TABLE_NAME, db))
            {
                db = getWritableDatabase();
                db.execSQL(CREATE_TABLE);
            }
        }

        /*
         * FUNCTION: onUpgrade
         * DESCRIPTION:
         *      This function is going to be called as default when this page is loaded
         * PARAMETER:
         *      SQLiteDatabase db
         * RETURNS:
         *      void: there's no return value
         */
        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Event list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(PartyPlannerDB.DROP_TABLE);
            onCreate(db);
        }

        /*
         * FUNCTION: isTableExists
         * DESCRIPTION:
         *      This function is going to be called to check an existence of a table
         * PARAMETER:
         *      SQLiteDatabase db
         * RETURNS:
         *      void: there's no return value
         */
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

//    // database and database helper objects
//    private SQLiteDatabase db = null;
//    private PartyGuestDB.DBHelper dbHelper = null;

    // Constructor
    public PartyGuestDB(Context context) {
        dbHelper = new PartyGuestDB.DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        dbHelper.onCreate(db);
    }

    /*
     * FUNCTION: openReadableDB
     * DESCRIPTION:
     *      This function is going to be called to read info from the database
     * PARAMETER:
     *      None
     * RETURNS:
     *      void: there's no return value
     */
    private void openReadableDB() {
        if(db == null || !db.isOpen()) {
            db = dbHelper.getReadableDatabase();
        }
        if(!db.isReadOnly()) {
            db.close();
            db = dbHelper.getReadableDatabase();
        }
    }

    /*
     * FUNCTION: openWriteableDB
     * DESCRIPTION:
     *      This function is going to be called to edit info in the database
     * PARAMETER:
     *      None
     * RETURNS:
     *      void: there's no return value
     */
    private void openWriteableDB() {
        if(db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        if(db.isReadOnly()) {
            db.close();
            db = dbHelper.getWritableDatabase();
        }
    }

    /*
     * FUNCTION: closeDB
     * DESCRIPTION:
     *      This function is going to be called to close the database
     * PARAMETER:
     *      None
     * RETURNS:
     *      void: there's no return value
     */
    public void closeDB() {
        if (db != null)
            db.close();
    }

    /*
     * FUNCTION: closeCursor
     * DESCRIPTION:
     *      This function is going to be called to close the cursor
     * PARAMETER:
     *      None
     * RETURNS:
     *      void: there's no return value
     */
    private void closeCursor(Cursor cursor) {
        if (cursor != null)
        {
            cursor.close();
        }
    }

    /*
     * FUNCTION: getGuests
     * DESCRIPTION:
     *      This function is going to be called to get the details of the table
     * PARAMETER:
     *      None
     * RETURNS:
     *      list: list of details
     */
    public ArrayList<List> getGuests() {
        ArrayList<List> lists = new ArrayList<List>();
        openReadableDB();
        Cursor cursor = db.query(TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(cursor.getInt(COL_ID_INDEX)));
            list.add(cursor.getString(COL_NAME_INDEX));
            lists.add(list);
        }
        closeCursor(cursor);
        closeDB();
        return lists;
    }

    /*
     * FUNCTION: getGuestDetails
     * DESCRIPTION:
     *      This function is going to be called to get the details of an event
     * PARAMETER:
     *      int: eventID
     * RETURNS:
     *      list: details of event
     */
    public String getGuestDetails(int eventID) {
        ArrayList<List> events = getGuests();
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
                events.get(eventID).get(COL_NAME_INDEX);
        return rtnDetails;
    }

    /*
     * FUNCTION: insertGuest
     * DESCRIPTION:
     *      This function is going to be called to insert guest the table
     * PARAMETER:
     *      string: menuItem
     * RETURNS:
     *      long: rowID
     */
    public long insertGuest(
        String guestName
    ) {
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, guestName);
        long rowID = db.insert(TABLE_NAME, null, cv);
        this.closeDB();
        return rowID;
    }

    /*
     * FUNCTION: updateGuest
     * DESCRIPTION:
     *      This function is going to be called to update guest the table
     * PARAMETER:
     *      string: eventId
     *      string: menuItem
     * RETURNS:
     *      long: rowID
     */
    public int updateGuest(
        String eventId,
        String guestName
    ) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, guestName);

        String where = COL_ID + "= ?";
        String[] whereArgs = { eventId };

        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    /*
     * FUNCTION: deleteGuest
     * DESCRIPTION:
     *      This function is going to be called to delete guest the table
     * PARAMETER:
     *      string: eventId
     * RETURNS:
     *      int: rowID
     */
    public int deleteGuest(
        String eventId
    ) {
        int eventNum = Integer.parseInt(eventId);
        ArrayList<List> events = getGuests();
        int eventCount;
        if (events.size() == 0)
        {
            return ERROR_EMPTY;
        }
        for (eventCount = 0; eventCount < events.size() ; )
        {
            eventCount ++;
        }
        if( eventNum> eventCount || eventNum < 0)
        {
            return ERROR_INVALID;
        }

        String value = null;
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM plannerInfo;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToPosition(eventNum)){
            value = cursor.getString(0);
        }

        String where = COL_ID + "= ?";
        String[] whereArgs = { eventId };

        this.openWriteableDB();
        int rowCount = db.delete(TABLE_NAME, where, whereArgs);
        this.closeDB();
        return rowCount;
    }

}
