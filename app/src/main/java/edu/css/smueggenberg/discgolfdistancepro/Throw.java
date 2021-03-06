package edu.css.smueggenberg.discgolfdistancepro;

/**
 * Created by smueggenberg on 4/22/2015.
 * The object representing the items saved in the app's database
 * Stores a throws id in the database, distance, type: putt or drive,
 * course where it was thrown, and the date thrown
 */
public class Throw {
    private int id;
    private long distance;
    private String type;
    private String course;
    private String date;

    public Throw(long distance, String type, String course, String date) {
        this.distance = distance;
        this.type = type;
        this.course = course;
        this.date = date;
    }

    public Throw(int id, long distance, String type, String course, String date) {
        this.id = id;
        this.distance = distance;
        this.type = type;
        this.course = course;
        this.date = date;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public long getDistance() { return distance; }
    public void setDistance(long distance) { this.distance = distance; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Returns a string representation of the throw's data
    public String toString(){
        String result = "";

        result += this.distance + " foot " + this.type;
        if(this.course != null){
            result += " at " + this.course;
        }

        if(this.date != null){
            result += " on " + this.date;
        }

        return result;
    }
}
