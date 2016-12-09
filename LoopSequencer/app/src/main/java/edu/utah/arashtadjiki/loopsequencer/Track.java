package edu.utah.arashtadjiki.loopsequencer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Arash on 11/30/2016.
 */
public class Track implements Serializable {

    public static final int DRUM = 0;
    public static final int BASS = 1;
    public static final int SAMPLE = 2;
    private int trackType;

    public Queue<AudioClip> clips;

    public Track(int _trackType){
        trackType = _trackType;
        clips = new LinkedList<>();
    }

    public int getTrackType() {
        return trackType;
    }

    public void setTrackType(int _trackType) {
        if(_trackType == DRUM)
            trackType = DRUM;
        else if(_trackType == BASS)
            trackType = BASS;
        else if(_trackType == SAMPLE)
            trackType = SAMPLE;
    }

    public String toString() {

        if (trackType == DRUM)
            return "Drum";
        else if (trackType == SAMPLE)
            return "Sample";
        else if(trackType == BASS)
            return "Bass";

        return null;
    }
}
