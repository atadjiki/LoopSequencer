package edu.utah.arashtadjiki.loopsequencer;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Arash on 11/29/2016.
 */

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {

    private Timeline timeline;
    private HashMap<Button, LinearLayout> tracksToClips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timeline_layout);

        initializeUI();

        tracksToClips = new HashMap<>();
        timeline = new Timeline("default");

    }

    public void initializeUI(){

        findViewById(R.id.add_track_button).setOnClickListener(this);
        findViewById(R.id.playButton).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.add_track_button){

            DoAddTrack(v);

        } else if(v.getId() == R.id.playButton){

            Log.i("Debug", "Playing Timeline");

            HashMap<Integer, List<AudioClip>> buffer =  TimelineController.prepareBuffer(timeline);

            // TimelineController.playBuffer(this,buffer);

        } else if (validTrackId(v.getId())){
            DoTrackPress(v);
        }
    }

    private void DoAddTrack(View v) {

        final View clicked = v;
        final CharSequence Tracks[] = TimelineController.getTrackOptions();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick A Track Type");
        builder.setItems(Tracks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // the user clicked on colors[which]
                insertTrack(which);
                Log.i("Debug", "Track inserted of type ");
            }
        });
        builder.show();
    }

    private void DoTrackPress(View v) {

        final View clicked = v;
        Log.i("Debug", "Track Selected: " + clicked .getId());
        final int trackType = timeline.getTracks().get(clicked .getId()).getTrackType();
        final CharSequence Clips[] = TimelineController.getClipOptions(trackType);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick A Clip");
        builder.setItems(Clips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Button button = new Button(getApplicationContext());
                button.setText(Clips[which].toString());
                button.setTextColor(Color.WHITE);
                // the user clicked on colors[which]
                tracksToClips.get(clicked).addView(button);
                timeline.getTracks().get(clicked.getId()).clips.add(getClipFromSelection(trackType, which));

                Log.i("Debug", "New Clip, '" + Clips[which] + "' Added To Track: " + clicked .getId());
            }
        });
        builder.show();
    }

    private AudioClip getClipFromSelection(int trackType, int index) {

        AudioClip audioClip = new AudioClip();

        if(trackType == Track.DRUM){
            audioClip.setAudioType(AudioClip.AUDIO);
            if(index == 0)
                audioClip.setResource(R.raw.drum1);
            else if(index == 1)
                audioClip.setResource(R.raw.drum2);
            else if(index == 2)
                audioClip.setResource(R.raw.drum3);
            else if(index == 3)
                audioClip.setResource(R.raw.drum4);
        } else if(trackType == Track.BASS){
            audioClip.setAudioType(AudioClip.AUDIO);
            if(index == 0)
                audioClip.setResource(R.raw.bass1);
            else if(index == 1)
                audioClip.setResource(R.raw.bass2);
            else if(index == 2)
                audioClip.setResource(R.raw.bass3);
            else if(index == 3)
                audioClip.setResource(R.raw.bass4);
        } else if (trackType == Track.SAMPLE){
            audioClip.setAudioType(AudioClip.AUDIO);
            if(index == 0)
                audioClip.setResource(R.raw.sample1);
            else if(index == 1)
                audioClip.setResource(R.raw.sample2);
            else if(index == 2)
                audioClip.setResource(R.raw.sample3);
            else if(index == 3)
                audioClip.setResource(R.raw.sample4);
        }  else if(index == 4){
            audioClip.setAudioType(AudioClip.NULL);
        }

        return audioClip;

    }

    private boolean validTrackId(int id) {

        for(Button track : tracksToClips.keySet()){

            if(id == track.getId())
                return true;
        }

        return false;
    }

    //inserts track to timeline object
    //as well as to UI
    private void insertTrack(int trackType) {

        LinearLayout trackLayout = (LinearLayout) findViewById(R.id.track_Layout);
        int tag = trackLayout.getChildCount()-1;

        //add new track to back end
        Track track = new Track(trackType);
        timeline.getTracks().add(track);

        //add track to UI
        Button trackButton = new Button(this);
        trackButton.setId(tag);
        trackButton.setText(track.toString());
        trackButton.setOnClickListener(this);
        trackLayout.addView(trackButton, tag);

        //add clip layout to UI
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        LinearLayout newClips = new LinearLayout(this);
        newClips.setOrientation(LinearLayout.HORIZONTAL);
        newClips.setBackgroundColor(trackTypeToColor(track.getTrackType()));
        clipLayout.addView(newClips,tag);

        //pair clip layout with track
        tracksToClips.put(trackButton, newClips);
    }

    public int trackTypeToColor(int TrackType){

        if(TrackType == Track.BASS)
            return Color.GREEN;
        else if(TrackType == Track.SAMPLE)
            return Color.CYAN;
        else if(TrackType == Track.DRUM)
            return Color.MAGENTA;
        else
            return 0;
    }
}
