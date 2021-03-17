/*
 * FILE          : PartyMenuDB.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2020-03-19
 * DESCRIPTION   : This file contains the definition and functions of the menuInfo table
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
 * NAME     :   PartyMenuDB
 * PURPOSE :    PartyMenuDB class contains the definition and functions of the menuInfo table
 *              in the PartyPlanner database
 */
public class PartyMenuDB {

    // Database constants
    public static final String DB_NAME = "PartyPlanner.db";
    public static final int    DB_VERSION = 1;
    public static int index = 0;
    public static final String TABLE_NAME = "menuInfo";
    public static final String COL_ID = "_id";
    public static final int COL_ID_INDEX = index++;
    public static final String COL_ITEM = "menuItem";
    public static final int COL_ITEM_INDEX = index++;
    // Constants for error code
    private static final int ERROR_EMPTY =  -1;
    private static final int ERROR_INVALID =  -2;

    // Database and database helper objects
    private SQLiteDatabase db = null;
    private PartyMenuDB.DBHelper dbHelper = null;


    // CREATE TABLE statement
    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID   + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                COL_ITEM + " TEXT    NOT NULL " +
        ");";

    // DROP TABLE statement
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

//    public static final String TAG = "EventListActivity";
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
            // Create tables
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
        public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion, int newVersion
        ) {
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


    // Constructor
    public PartyMenuDB(Context context) {
        dbHelper = new PartyMenuDB.DBHelper(context, DB_NAME, null, DB_VERSION);
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
            cursor.close();
    }


    /*
     * FUNCTION: getMenuList
     * DESCRIPTION:
     *      This function is going to be called to get the details of the table
     * PARAMETER:
     *      None
     * RETURNS:
     *      list: list of details
     */
    public ArrayList<List> getMenuList() {
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

    /*
     * FUNCTION: getMenuDetails
     * DESCRIPTION:
     *      This function is going to be called to get the details of an event
     * PARAMETER:
     *      int: eventID
     * RETURNS:
     *      list: details of event
     */
    public String getMenuDetails(int eventID) {
        ArrayList<List> events = getMenuList();
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

    /*
     * FUNCTION: insertMenuItem
     * DESCRIPTION:
     *      This function is going to be called to insert item the table
     * PARAMETER:
     *      string: menuItem
     * RETURNS:
     *      long: rowID
     */
    public long insertMenuItem(
         String menuItem
    ) {
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM, menuItem);
        long rowID = db.insert(TABLE_NAME, null, cv);
        this.closeDB();
        return rowID;
    }

    /*
     * FUNCTION: updateMenuItem
     * DESCRIPTION:
     *      This function is going to be called to update item the table
     * PARAMETER:
     *      string: eventId
     *      string: menuItem
     * RETURNS:
     *      long: rowID
     */
    public int updateMenuItem(
        String eventId,
        String menuItem
    ) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM, menuItem);

        String where = COL_ID + "= ?";
        String[] whereArgs = { eventId };

        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    /*
     * FUNCTION: deleteMenuItem
     * DESCRIPTION:
     *      This function is going to be called to delete item the table
     * PARAMETER:
     *      string: eventId
     * RETURNS:
     *      int: rowID
     */
    public int deleteMenuItem(
            String eventId
    ) {
        int eventNum = Integer.parseInt(eventId);
        ArrayList<List> events = getMenuList();
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
