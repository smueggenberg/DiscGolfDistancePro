package edu.css.smueggenberg.discgolfdistancepro;

/**
 * Created by smueggenberg on 4/22/2015.
 */
public class Throw {
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

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public long getDistance() { return distance; }
    public void setDistance(long distance) { this.distance = distance; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    //TODO: to string method that formats the results of the throw
}
