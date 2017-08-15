package com.example.android.Model.GoogleTasksAPI;

/**
 * Created by shafe on 8/7/2017.
 */

import android.graphics.Color;

import com.example.android.Model.ITask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shafe on 8/7/2017.
 */

public class TaskGoogle implements ITask {


    class Link {
        String type;
        String description;
        String link;

        Link(){

        }

        Link(String type, String description, String link){
            this.type = type;
            this.description = description;
            this.link = link;
        }
    }

    private String kind; // will be used for priority.
    private String id;
    private String etag;
    private Date updated;
    private String selfLink;
    private String parent;
    private String position;
    private String notes;
    private String status;
    private Date due;
    private Date completed;
    private boolean deleted;
    private boolean hidden;
    private String title;
    private List<Link> links;
    private String taskListID;


    public TaskGoogle( ){
        links = new ArrayList<Link>();
        color = Color.GRAY;
    }

    public void addLink(String type, String description, String link){
        if ( links != null ){
            Link l = new Link();
            l.description = description;
            l.type = type;
            l.link = link;
            links.add( l );
        }

    }



    // get accessors.

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String newTitle) {
        this.title = newTitle;

    }

    @Override
    public String getPriority() {
        return kind;
    }

    @Override
    public void setPriority(String kind) {
        this.kind = kind;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String ID) {
        this.id = ID;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Date getDue() {
        return due;
    }

    @Override
    public void setDueDate(Date dueDate) {
        this.due = dueDate;
    }

    @Override
    public String getTaskListID() {
        return taskListID;
    }

    @Override
    public String getTaskListName() {
        return null;
    }

    @Override
    public Date getLastUpdated() {
        // TODO - update getLastUpdated status.
        return updated;
    }

    @Override
    public void setLastUpdated(Date updatedDate) {
            // TODO - update LastUpdated status.
        this.updated = updatedDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public void setTaskListID(String taskListID) {
        this.taskListID = taskListID;
    }

    public String get_etag(){
        return etag;
    }
    public String get_selfLink(){
        return selfLink;
    }
    public String get_parent(){
        return parent;
    }
    public String get_position(){
        return position;
    }


    public Date get_due(){
        return due;
    }
    public Date get_completed(){
        return completed;
    }
    public boolean get_deleted(){
        return deleted;
    }
    public boolean get_hidden(){
        return hidden;
    }
    public Object[] get_links(){
        if ( links != null ) {
            return links.toArray();
        }
        return null;
    }

    // add / set methods
    public void addLink(Link link){
        if ( links != null )
            links.add( link );
    }


    public void  set_etag(String s){
        etag = s;
    }
    public void set_updated(Date d){
        updated = d;
    }
    public void set_selfLink( String s){
        selfLink = s;
    }
    public void set_parent(String s){
        parent = s;
    }
    public void set_position(String s){
        position = s;
    }
    public void set_completed(Date d){
         completed = d;
    }
    public void set_deleted( boolean b){
        deleted = b;
    }
    public void set_hidden(boolean b){
         hidden = b;
    }
//    public void set_tasklistID( String taskListID ){  this.taskListID = taskListID ; }


  /*  public set_links(Object[] ){
        return links.toArray();
    }
    */

    // add on methods
    private int color = -1;
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}



/*
JSON Format
{
  "kind": "tasks#task",
  "id": string,
  "etag": etag,
  "title": string,
  "updated": datetime,
  "selfLink": string,
  "parent": string,
  "position": string,
  "notes": string,
  "status": string,
  "due": datetime,
  "completed": datetime,
  "deleted": boolean,
  "hidden": boolean,
  "links": [
    {
      "type": string,
      "description": string,
      "link": string
    }
  ]
}
 */
