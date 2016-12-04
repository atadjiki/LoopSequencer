package edu.utah.arashtadjiki.loopsequencer;

/**
 * Created by Arash on 11/30/2016.
 */
public class AudioClip {

    public static final int NULL = 0;
    public static final int AUDIO = 1;
    private int audioType;
    private int resource;

    public AudioClip(){audioType = NULL; resource = NULL;}

    public AudioClip(int resource){audioType = NULL; resource = AUDIO; setResource(resource);}

    public int getAudioType() {return audioType;}

    public void setAudioType(int audioType) {this.audioType = audioType;}

    public int getResource() {return resource;}

    public void setResource(int resource) {this.resource = resource;}
}
