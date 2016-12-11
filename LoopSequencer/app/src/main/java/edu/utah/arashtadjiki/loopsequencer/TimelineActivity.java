package edu.utah.arashtadjiki.loopsequencer;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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

    /*Start Up Functions*/

    public void initializeUI(){

        findViewById(R.id.add_track_button).setOnClickListener(this);
        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
    }

    /*Pop Up Dialog Functions*/

    private void AddTrackDialog(View v) {

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

    private void TrackPressDialog(View v) {

        final View clicked = v;
        Log.i("Debug", "Track Selected: " + clicked.getId());
        final CharSequence TrackOptions[] = new CharSequence[]{"Add Clip", "Remove"};

        AlertDialog.Builder choiceDialog = new AlertDialog.Builder(TimelineActivity.this);
        choiceDialog.setTitle("Track Options");
        choiceDialog.setItems(TrackOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(which == 0){ AddClipDialog(clicked);
              } else if (which == 1){ RemoveTrack(clicked);}}});
        choiceDialog.show();
    }

    private void AddClipDialog(View v) {
        final View clicked = v;
        final int trackType = timeline.getTracks().get(clicked.getId()).getTrackType();
        final CharSequence Clips[] = TimelineController.getClipOptions(trackType);
        AlertDialog.Builder clipDialog = new AlertDialog.Builder(this);
        clipDialog.setCancelable(false);
        clipDialog.setTitle("Pick A Clip");
        clipDialog.setItems(Clips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Button button = new Button(getApplicationContext());
                button.setText(Clips[which].toString());
                button.setTextColor(Color.WHITE);
                button.setOnClickListener(TimelineActivity.this);
                // the user clicked on colors[which]
                tracksToClips.get(clicked).addView(button);
                timeline.getTracks().get(clicked.getId()).clips.add(getClipFromSelection(trackType, which));

                Log.i("Debug", "New Clip, '" + Clips[which] + "' Added To Track: " + clicked .getId());
            }
        });
        clipDialog.show();
    }

    private void ClipPressDialog(View v){

        final View clicked = v;
        Log.i("Debug", "Clip selected: ");
        final CharSequence TrackOptions[] = new CharSequence[]{"Swap", "Remove"};

        AlertDialog.Builder choiceDialog = new AlertDialog.Builder(TimelineActivity.this);
        choiceDialog.setTitle("Clip Options");
        choiceDialog.setItems(TrackOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){ SwapClipDialog(clicked);
                } else if (which == 1){ RemoveClip(clicked);}}});
        choiceDialog.show();

    }

    private void SwapClipDialog(View v) {

        final View clicked = v;
        int trackIndex = -1;
        int clipIndex = -1;
        int newResource = -1;

        //get track index
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        int debug = clipLayout.getChildCount();
        for(int i = 0; i<clipLayout.getChildCount(); i++){

            LinearLayout temp = (LinearLayout) clipLayout.getChildAt(i);
            if(temp.equals((LinearLayout) clicked.getParent())){
                trackIndex = i;
                break;
            }
        }

        if(trackIndex == -1) return;
        final int finalTrackIndex = trackIndex;

        //get clip index
        LinearLayout parent = (LinearLayout) clicked.getParent();
        for(int i = 0; i < parent.getChildCount(); i++){
            if(((View) parent.getChildAt(i)).equals(clicked)){
                clipIndex = i;
                break;
            }
        }

        if(clipIndex == -1) return;
        final int finalClipIndex = clipIndex;

        //get resource selection and send command
        final int trackType = timeline.getTracks().get(trackIndex).getTrackType();
        final CharSequence Clips[] = TimelineController.getClipOptions(trackType);
        AlertDialog.Builder clipDialog = new AlertDialog.Builder(this);
        clipDialog.setCancelable(false);
        clipDialog.setTitle("Pick New Clip");
        clipDialog.setItems(Clips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SwapClip(finalTrackIndex, finalClipIndex, which, trackType, clicked, Clips[which].toString());
            }
        });
        clipDialog.show();
    }

    /* Data-UI Controller Functions*/

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

    //removes selected track from both UI and data model
    private void RemoveTrack(View v) {

        final View clicked = v;
        int tag = clicked.getId();

        //remove track from back end
        timeline.getTracks().remove(timeline.getTracks().get(tag));
        //remove track/clip layout pairing
        Button trackButton = (Button) findViewById(tag);
        tracksToClips.remove(trackButton);

        //remove track and remove attached clip layout
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        clipLayout.removeView(clipLayout.getChildAt(tag));
        LinearLayout trackLayout = (LinearLayout) findViewById(R.id.track_Layout);
        trackLayout.removeView(trackLayout.getChildAt(tag));
    }

    private void SwapClip(int trackIndex, int clipIndex, int which, int trackType, View v, String name){

        //swap clip on backend
        int resourceID = getClipFromSelection(trackType, which).getResource();
        AudioClip[] clips = timeline.getTracks().get(trackIndex).clips.toArray(new AudioClip[timeline.getTracks().get(trackIndex).clips.size()]);
        clips[clipIndex].setResource(resourceID);

        //swap clip on UI
        Button clipButton = (Button) v;
        clipButton.setText(name);

        Log.i("Debug", "Clip " + clipIndex + " in track " + trackIndex + " changed to " + name);
    }

    private void RemoveClip(View v) {

        //get track index
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        int trackIndex = -1;
        int clipIndex = -1;

        int debug = clipLayout.getChildCount();
        for(int i = 0; i<clipLayout.getChildCount(); i++){

            LinearLayout temp = (LinearLayout) clipLayout.getChildAt(i);
            if(temp.equals((LinearLayout) v.getParent())){
                trackIndex = i;
                break;
            }
        }

        if(trackIndex == -1) return;

        //get clip index
        LinearLayout parent = (LinearLayout) v.getParent();
        for(int i = 0; i < parent.getChildCount(); i++){
            if(((View) parent.getChildAt(i)).equals(v)){
                clipIndex = i;
                break;
            }
        }

        if(clipIndex == -1) return;

        //remove clip in back end
        AudioClip[] clips = timeline.getTracks().get(trackIndex).clips.toArray(new AudioClip[timeline.getTracks().get(trackIndex).clips.size()]);
        AudioClip clip = clips[clipIndex];
        timeline.getTracks().get(trackIndex).clips.remove(clip);

        //remove clip in UI
        parent.removeView(v);

        Log.i("Debug", "Clip " + clipIndex + "removed from track " + trackIndex);

    }

    /*
        Misc Helper Functions
     */

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

    /*
      Listener Functions
     */
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.add_track_button){ AddTrackDialog(v);}
        else if (v.getId() == R.id.save_button){
            try {serializeTimeline();} catch (IOException e) {e.printStackTrace();}}
        else if(v.getId() == R.id.playButton){

            HashMap<Integer, List<AudioClip>> buffer =  TimelineController.prepareBuffer(timeline);

            // TimelineController.playBuffer(this,buffer);

        } else if (validTrackId(v.getId())){
            TrackPressDialog(v);
        } else{
            int parentId = ((View) v.getParent().getParent()).getId();
            if(parentId == R.id.clip_Layout){
                Log.i("Debug", "Clip Selected");
                ClipPressDialog(v);
            }
        }
    }

    /*
        File save/load functions
     */


    public void serializeTimeline() throws IOException {

        String filename = "timeline"+timeline.getProjectName();
        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(),"")+ File.separator+filename));
            out.writeObject(timeline);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] listOfFiles = getFilesDir().list();
        Log.i("File", "Counting "+ (listOfFiles.length-1) + " files");
        Log.i("File", "File written: " + filename + " to " + getFilesDir());
    }
}
