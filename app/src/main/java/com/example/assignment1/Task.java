/*
 *   FILE: Task.java
 *   Project: A3
 *   PROGRAMMER: Jessica Sim
 *   FIRST VERSION: 2021-04-01
 *   DESCRIPTION:
 *	    This file contains all the fields in Party Planner database and there are few functions that we can
 *      get specific field or set the information to a field
 */
package com.example.assignment1;

import android.content.ContentValues;


/*
 * NAME     :    Task
 * PURPOSE :    Task class contains all the fields in Party Planner database and there are few functions that we can
 *      get specific field or set the information to a field
 */
public class Task {
    private String id;
    private String eventName;
    private String eventType;
    private String eventDate;
    private String eventAddress;
    private String eventGuest;
    private String eventMenu;
    private String eventSupply;

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    /*
     * FUNCTION: Task
     * DESCRIPTION:
     *      This function is going to be called as default when this page the Task class is initiated
     *      from another class and sets all the variables in this class to null
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public Task() {
        id = "";
        eventName = "";
        eventType = "";
        eventDate = "";
        eventAddress = "";
        eventGuest = "";
        eventMenu = "";
        eventSupply = "";
    }

    /*
     * FUNCTION: Task
     * DESCRIPTION:
     *      This function is going to be called as default when this page the Task class is initiated
     *      with some parameters from another class and sets all the variables in this class to null
     * PARAMETER:
     *      String id: string of id
     *      String eventName: string of eventName
     *      String eventType: string of eventType
     *      String eventDate: string of eventDate
     *      String eventAddress: string of eventAddress
     *      String eventGuest: string of eventGuest
     *      String eventMenu: string of eventMenu
     *      String eventSupply: string of eventSupply
     * RETURNS:
     *      void: there's no return value
     */
    public Task(String id, String eventName, String eventType, String eventDate, String eventAddress,
                String eventGuest, String eventMenu, String eventSupply) {
        this.id = id;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventAddress = eventAddress;
        this.eventGuest = eventGuest;
        this.eventMenu = eventMenu;
        this.eventSupply = eventSupply;
    }

    /*
     * FUNCTION: Task
     * DESCRIPTION:
     *      This function is going to be called as default when this page the Task class is initiated
     *      with ContentValues parameter from another class and sets all the variables in this class to null
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public Task(ContentValues values) {
        this.id = (String) values.get(PartyPlannerDB.COL_ID);
        this.eventName = (String)values.get(PartyPlannerDB.COL_NAME);
        this.eventType = (String)values.get(PartyPlannerDB.COL_TYPE);
        this.eventDate = (String)values.get(PartyPlannerDB.COL_DATE);
        this.eventAddress = (String) values.get(PartyPlannerDB.COL_ADDRESS);
        this.eventGuest = (String) values.get(PartyPlannerDB.COL_GUEST);
        this.eventMenu = (String) values.get(PartyPlannerDB.COL_MENU);
        this.eventSupply = (String) values.get(PartyPlannerDB.COL_SUPPLY);
    }

    /*
     * FUNCTION: getId
     * DESCRIPTION:
     *      This function is going to be called to get the id
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getId() {
        return id;
    }

    /*
     * FUNCTION: setId
     * DESCRIPTION:
     *      This function is going to be called to set the id
     * PARAMETER:
     *      String id: event id
     * RETURNS:
     *      void: there's no return value
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * FUNCTION: getId
     * DESCRIPTION:
     *      This function is going to be called to get the id
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventName() {
        return eventName;
    }

    /*
     * FUNCTION: setEventName
     * DESCRIPTION:
     *      This function is going to be called to set the eventName
     * PARAMETER:
     *      String eventName: event name
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /*
     * FUNCTION: getEventType
     * DESCRIPTION:
     *      This function is going to be called to get the type of event
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventType() {
        return eventType;
    }

    /*
     * FUNCTION: setEventType
     * DESCRIPTION:
     *      This function is going to be called to set the eventType
     * PARAMETER:
     *      String eventType: event type
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /*
     * FUNCTION: getEventDate
     * DESCRIPTION:
     *      This function is going to be called to get the date
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventDate() {
        return eventDate;
    }

    /*
     * FUNCTION: setEventDate
     * DESCRIPTION:
     *      This function is going to be called to set the eventDate
     * PARAMETER:
     *      String eventDate: event date
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /*
     * FUNCTION: getEventAddress
     * DESCRIPTION:
     *      This function is going to be called to get the address
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventAddress() {
        return eventAddress;
    }

    /*
     * FUNCTION: setEventAddress
     * DESCRIPTION:
     *      This function is going to be called to set the eventAddress
     * PARAMETER:
     *      String eventAddress: event address
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    /*
     * FUNCTION: getEventGuest
     * DESCRIPTION:
     *      This function is going to be called to get the guest
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventGuest() {
        return eventGuest;
    }

    /*
     * FUNCTION: setEventGuest
     * DESCRIPTION:
     *      This function is going to be called to set the eventGuest
     * PARAMETER:
     *      String eventGuest: event guest
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventGuest(String eventGuest) {
        this.eventGuest = eventGuest;
    }

    /*
     * FUNCTION: getEventMenu
     * DESCRIPTION:
     *      This function is going to be called to get the menu
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventMenu() {
        return eventMenu;
    }

    /*
     * FUNCTION: setEventMenu
     * DESCRIPTION:
     *      This function is going to be called to set the eventMenu
     * PARAMETER:
     *      String eventMenu: event menu
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventMenu(String eventMenu) {
        this.eventMenu = eventMenu;
    }

    /*
     * FUNCTION: getEventSupply
     * DESCRIPTION:
     *      This function is going to be called to get the supply
     * PARAMETER:
     *      there's no parameters
     * RETURNS:
     *      void: there's no return value
     */
    public String getEventSupply(){
        return eventSupply;
    }

    /*
     * FUNCTION: setEventSupply
     * DESCRIPTION:
     *      This function is going to be called to set the supply
     * PARAMETER:
     *      String eventSupply: event supply
     * RETURNS:
     *      void: there's no return value
     */
    public void setEventSupply(String eventSupply) {
        this.eventSupply = eventSupply;
    }
}
