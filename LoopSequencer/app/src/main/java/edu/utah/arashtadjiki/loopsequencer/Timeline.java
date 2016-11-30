package edu.utah.arashtadjiki.loopsequencer;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arash on 11/30/2016.
 */

/*
 * The data model representing a timeline.
 * Houses tracks containing audio clips which are arranged in sequence
 */
public class Timeline implements Serializable {

    private List<Track> tracks; //con
    private String projectName;

    public Timeline(String _projectName){
        tracks = new ArrayList<>();
        projectName = _projectName;
    }

    public List<Track> getTracks() {return tracks;}
    public void setTracks(List<Track> tracks) {this.tracks = tracks;}

}
