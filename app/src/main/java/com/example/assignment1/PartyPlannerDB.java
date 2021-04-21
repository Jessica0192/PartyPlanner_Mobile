/*
 * FILE          : PartyPlannerDB.java
 * PROJECT       : PROG3150 - Assignment #3
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2021-04-19
 * DESCRIPTION   : This file contains the definition and functions of the plannerInfo table
 *                 in the PartyPlanner database
 */

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


/*
 * NAME     :   PartyPlannerDB
 * PURPOSE :    PartyPlannerDB class contains the definition and functions of the menuInfo table
 *              in the PartyPlanner database
 */
public class PartyPlannerDB {

    // Database constants
    public static final String DB_NAME = "PartyPlanner.db";
    public static final int    DB_VERSION = 1;

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

    // Constants for error code
    private static final int ERROR_EMPTY =  -1;
    private static final int ERROR_INVALID =  -2;

    // Database and database helper objects
    private SQLiteDatabase db = null;
    private PartyPlannerDB.DBHelper dbHelper = null;

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



    // Constructor
    public PartyPlannerDB(Context context) {
        dbHelper = new PartyPlannerDB.DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        dbHelper.onCreate(db);
        Log.d(TAG, "==================================================== DB constructor ...");
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
     * FUNCTION: getEvents
     * DESCRIPTION:
     *      This function is going to be called to get the details of the table
     * PARAMETER:
     *      None
     * RETURNS:
     *      list: list of details
     */
    public ArrayList<List> getEvents() {
        ArrayList<List> lists = new ArrayList<List>();
        //db = dbHelper.getReadableDatabase();
        openReadableDB();
        Cursor cursor = db.query(TABLE_NAME,
                null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            List<String> list = new ArrayList<>();
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
        //closeDB();
        return lists;
    }

    /*
     * FUNCTION: getFormattedEventsSummary
     * DESCRIPTION:
     *      This function is going to be called to get the info of event name and date
     * PARAMETER:
     *      None
     * RETURNS:
     *      list: info of event name and date
     */
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

    /*
     * FUNCTION: getEventDetails
     * DESCRIPTION:
     *      This function is going to be called to get the details of an event
     * PARAMETER:
     *      int: eventID
     * RETURNS:
     *      list: details of event
     */
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
        if( eventID>= eventCount || eventID < 0)
        {
            return "<INVALID ID>";
        }
        String rtnDetails = "";
        rtnDetails += "" +
                "     NAME : " + events.get(eventID).get(COL_NAME_INDEX) + "\r\n" +
                "     TYPE : " + events.get(eventID).get(COL_TYPE_INDEX) + "\r\n" +
                "     DATE : " + events.get(eventID).get(COL_DATE_INDEX) + "\r\n" +
                "     ADDRESS : " + events.get(eventID).get(COL_ADDRESS_INDEX) + "\r\n" +
                "     GUEST : " + events.get(eventID).get(COL_GUEST_INDEX) + "\r\n" +
                "     MENU : " + events.get(eventID).get(COL_MENU_INDEX) + "\r\n" +
                "     SUPPLY : " + events.get(eventID).get(COL_SUPPLY_INDEX);
        return rtnDetails;
    }

    /*
     * FUNCTION: insertEvent
     * DESCRIPTION:
     *      This function is going to be called to insert event to the table
     * PARAMETER:
     *      string: eventName, eventType, eventDate, eventAddress, eventGuest, menuItem, eventSupply
     * RETURNS:
     *      long: rowID
     */
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
        //Log.d(TAG, cv.toString());

        //this.closeDB();
        return rowID;
    }

    /*
     * FUNCTION: updateEvent
     * DESCRIPTION:
     *      This function is going to be called to update an event in the table
     * PARAMETER:
     *      list: record
     * RETURNS:
     *      long: rowID
     */
    public int updateEvent(
        List<String> record
    ) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, record.get(PartyPlannerDB.COL_NAME_INDEX));
        cv.put(COL_TYPE, record.get(PartyPlannerDB.COL_TYPE_INDEX));
        cv.put(COL_DATE, record.get(PartyPlannerDB.COL_DATE_INDEX));
        cv.put(COL_ADDRESS, record.get(PartyPlannerDB.COL_ADDRESS_INDEX));
        cv.put(COL_GUEST, record.get(PartyPlannerDB.COL_GUEST_INDEX));
        cv.put(COL_MENU, record.get(PartyPlannerDB.COL_MENU_INDEX));
        cv.put(COL_SUPPLY, record.get(PartyPlannerDB.COL_SUPPLY_INDEX));
        Log.d(TAG, "'Supply' =========supply===========" + record.get(PartyPlannerDB.COL_SUPPLY_INDEX));
        String where = COL_ID + "= ?";
        String[] whereArgs = { record.get(PartyPlannerDB.COL_ID_INDEX) };

        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        //this.closeDB();

        return rowCount;
    }





    /*************************NEW******************************/


    public long insertTask(Task task) {
        db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, task.getEventName());
        cv.put(COL_TYPE, task.getEventType());
        cv.put(COL_DATE, task.getEventDate());
        cv.put(COL_ADDRESS, task.getEventAddress());
        cv.put(COL_GUEST, task.getEventGuest());
        cv.put(COL_MENU, task.getEventMenu());
        cv.put(COL_SUPPLY, task.getEventSupply());

        long rowID = db.insert(TABLE_NAME, null, cv);
        this.closeDB();

        Log.d(TAG, cv.toString());



        return rowID;
    }



    public int deleteTask(String where, String[] whereArgs) {
        this.openWriteableDB();
        int rowCount = db.delete(TABLE_NAME, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int deleteTask(long id) {
        String where = COL_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(TABLE_NAME, where, whereArgs);
        this.closeDB();

        return rowCount;
    }


    public int updateTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, task.getEventName());
        cv.put(COL_TYPE, task.getEventType());
        cv.put(COL_DATE, task.getEventDate());
        cv.put(COL_ADDRESS, task.getEventAddress());
        cv.put(COL_GUEST, task.getEventGuest());
        cv.put(COL_MENU, task.getEventMenu());
        cv.put(COL_SUPPLY, task.getEventSupply());

        String where = COL_ID + "= ?";
        String[] whereArgs = { String.valueOf(task.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int updateTask(ContentValues cv, String where, String[] whereArgs) {
        this.openWriteableDB();
        int rowCount = db.update(TABLE_NAME, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public Cursor queryTasks(String[] columns, String where,
                             String[] whereArgs, String orderBy) {
        this.openReadableDB();
        Cursor cursor = db.query(TABLE_NAME, columns,
                where, whereArgs,
                null, null, orderBy);
        return cursor;
    }


    /*************************UNTIL HERE******************************/


    /*
     * FUNCTION: updateEvent
     * DESCRIPTION:
     *      This function is going to be called to update an event in the table
     * PARAMETER:
     *      string: eventName, eventType, eventDate, eventAddress, eventGuest, menuItem, eventSupply
     * RETURNS:
     *      long: rowID
     */
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
        //this.closeDB();

        return rowCount;
    }

    /*
     * FUNCTION: deleteEvent
     * DESCRIPTION:
     *      This function is going to be called to delete an event in the table
     * PARAMETER:
     *      string: eventId
     * RETURNS:
     *      int: rowID
     */
    public int deleteEvent(
            String eventId
    ) {
        Log.d(TAG, "============='planner db'=======delete====== ");
        int eventNum = Integer.parseInt(eventId);
        ArrayList<List> events = getEvents();
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

        Log.d(TAG, "value in: "+value);

        String where = COL_ID + "= ?";
        String[] whereArgs = { value };

        this.openWriteableDB();
        int rowCount = db.delete(TABLE_NAME, where, whereArgs);
        //this.closeDB();

        return rowCount;
    }


}
