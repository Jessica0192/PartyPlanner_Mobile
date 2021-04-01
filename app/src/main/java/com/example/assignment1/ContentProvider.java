/*
 *   FILE: ContentProvider.java
 *   Project: A3
 *   PROGRAMMER: Jessica Sim
 *   FIRST VERSION: 2021-04-01
 *   DESCRIPTION:
 *	    This file contains the logic of wrapping the Party Planner database with Content Provider.
 */
package com.example.assignment1;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * NAME     :    ContentProvider
 * PURPOSE :    CreateEventActivity class contains the functionality of wrapping the Party Planner Database with
 *              Content Provider
 */
public class ContentProvider extends android.content.ContentProvider {
    public static final String AUTHORITY = "com.example.herd.contentprovider";

    public static final int NO_MATCH = -1;
    public static final int ALL_TASKS_URI = 0;
    public static final int SINGLE_TASK_URI = 1;

    private PartyPlannerDB db = null;
    private UriMatcher uriMatcher = null;

    /*
     * FUNCTION: onCreate
     * DESCRIPTION:
     *      This function is going to be called as default when this page is loaded and there are
     *      several functions and listeners that does some actions
     * PARAMETER:
     *      Bundle savedInstanceState: save instance state
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    public boolean onCreate() {
        db = new PartyPlannerDB(getContext());
        uriMatcher = new UriMatcher(NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "tasks", ALL_TASKS_URI);
        uriMatcher.addURI(AUTHORITY, "tasks/#", SINGLE_TASK_URI);
        return true;
    }

    /*
     * FUNCTION: query
     * DESCRIPTION:
     *      This function is going to return the query tasks from Party Planner Database
     * PARAMETER:
     *      Uri uri: A URI reference includes a URI and a fragment, the component of the URI following a '#'
     *      String[] projection: String array that contains projections
     *      String selection: a string of selection
     *      String[] selectionArgs: string array that contains selection arguments
     *      String sortOrder: string of sort order
     * RETURNS:
     *      Cursor: it returns the cursor which is queried
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case ALL_TASKS_URI:
                return db.queryTasks(projection, selection,
                        selectionArgs, sortOrder);
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported");
        }
    }

    /*
     * FUNCTION: getType
     * DESCRIPTION:
     *      This function is going to get a type of task URI
     * PARAMETER:
     *      Uri uri: A URI reference includes a URI and a fragment, the component of the URI following a '#'
     * RETURNS:
     *      String: returns a string of URI
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri)) {
            case ALL_TASKS_URI:
                return "vnd.android.cursor.dir/vnd.herd.tasklist.tasks";
            case SINGLE_TASK_URI:
                return "vnd.android.cursor.item/vnd.herd.tasklist.tasks";
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported");
        }
    }

    /*
     * FUNCTION: insert
     * DESCRIPTION:
     *      This function is going to insert passed content values and return the Uri
     * PARAMETER:
     *      Uri uri: A URI reference includes a URI and a fragment, the component of the URI following a '#'
     *      ContentValues values: values of ContentValues
     * RETURNS:
     *      Uri: it returns URI reference includes a URI and a fragment, the component of the URI following a '#'
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case ALL_TASKS_URI:
                long insertId = db.insertTask(new Task(values));
                getContext().getContentResolver().notifyChange(uri, null);
                return uri.buildUpon().appendPath(
                        String.valueOf(insertId)).build();
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported");
        }
    }

    /*
     * FUNCTION: delete
     * DESCRIPTION:
     *      This function is going to delete particular id in database
     * PARAMETER:
     *      Uri uri: A URI reference includes a URI and a fragment, the component of the URI following a '#'
     *      String selection: a string of selection
     *      String[] selectionArgs: string array that contains selection arguments
     * RETURNS:
     *      int: it returns the integer of row in database which is deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleteCount = 0;
        switch (uriMatcher.match(uri)) {
            case SINGLE_TASK_URI:
                String taskId = uri.getLastPathSegment();
                deleteCount = db.deleteTask(Long.getLong(taskId));
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteCount;
            case ALL_TASKS_URI:
                deleteCount = db.deleteTask(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return deleteCount;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported");
        }
    }

    /*
     * FUNCTION: update
     * DESCRIPTION:
     *      This function is going to update information in database
     * PARAMETER:
     *      Uri uri: A URI reference includes a URI and a fragment, the component of the URI following a '#'
     *      String selection: a string of selection
     *      String[] selectionArgs: string array that contains selection arguments
     * RETURNS:
     *      int: it returns the integer of row in database which is updated
     */
        @Override
        public int update (Uri uri, ContentValues values, String selection, String[]selectionArgs){
            int updateCount = 0;
            switch (uriMatcher.match(uri)) {
                case SINGLE_TASK_URI:
                    String taskId = uri.getLastPathSegment();
                    Task task = new Task(values);
                    task.setId(taskId);
                    updateCount = db.updateTask(task);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return updateCount;
                case ALL_TASKS_URI:
                    updateCount = db.updateTask(values, selection, selectionArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return updateCount;
                default:
                    throw new UnsupportedOperationException("URI " + uri + " is not supported");
            }
        }
}
