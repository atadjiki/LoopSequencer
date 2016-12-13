package edu.utah.arashtadjiki.loopsequencer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arash on 11/29/2016.
 */

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {

    private Timeline timeline;
    private HashMap<Button, LinearLayout> tracksToClips;
    private int startingPoint = 0;
    private int endingPoint;
    private boolean endpointSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.timeline_layout);
        initializeUI();

        tracksToClips = new HashMap<>();

        if(getIntent().hasExtra("timeline")) {
            Bundle bundle = (Bundle) getIntent().getBundleExtra("timeline");
            timeline = (Timeline) bundle.get("timeline");
        }else {
            timeline = new Timeline("default");
        }
        setTitle("Project - " + timeline.getProjectName());

        setupTimeline();
        resetStartEndPoints();
    }

    /*
        Starting/Ending Point Functions
     */
    private void resetStartEndPoints() {

        startingPoint = 0;
        endingPoint = findLastPosition();
        TextView text = (TextView) findViewById(R.id.start_end);
        text.setText(" Start: " + startingPoint + ", End: " + endingPoint);
        endpointSet = false;
    }

    public int findLastPosition(){

        int lastPosition = 0;

        for(Track track : timeline.getTracks()){

            if(track.clips.size() > lastPosition)
                lastPosition = track.clips.size() -1;
        }

        return lastPosition;
    }

    private void setStartPosition(View v) {

        //get track index
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        int clipIndex = -1;

        int debug = clipLayout.getChildCount();

        //get clip index
        LinearLayout parent = (LinearLayout) v.getParent();
        for(int i = 0; i < parent.getChildCount(); i++){
            if(((View) parent.getChildAt(i)).equals(v)){
                clipIndex = i;
                break;
            }
        }

        if(clipIndex == -1) return;

        //set start position
        startingPoint = clipIndex;
        TextView text = (TextView) findViewById(R.id.start_end);
        text.setText(" Start: " + startingPoint + ", End: " + endingPoint);
    }

    private void setEndPosition(View v) {

        //get track index
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        int clipIndex = -1;

        int debug = clipLayout.getChildCount();

        //get clip index
        LinearLayout parent = (LinearLayout) v.getParent();
        for(int i = 0; i < parent.getChildCount(); i++){
            if(((View) parent.getChildAt(i)).equals(v)){
                clipIndex = i;
                break;
            }
        }

        if(clipIndex == -1) return;

        //set start position
        endingPoint = clipIndex;
        TextView text = (TextView) findViewById(R.id.start_end);
        text.setText(" Start: " + startingPoint + ", End: " + endingPoint);
        endpointSet = true;
    }

    private void updateLastPosition(){
        if(!endpointSet){
            endingPoint = findLastPosition();
            TextView text = (TextView) findViewById(R.id.start_end);
            text.setText(" Start: " + startingPoint + ", End: " + endingPoint);
        }

    }

    /*Start Up Functions*/

    private void setupTimeline() {

        //add each track, then for each, add every clip

        for(int index = 0; index < timeline.getTracks().size(); index++){

            Track track = timeline.getTracks().get(index);
            insertTrack(true, track.getTrackType());

            View trackButton = null;
            for(Button b : tracksToClips.keySet()){
                if(b.getId() == index){
                    trackButton = b;
                }
            }

            if(track.isMute())
                ToggleTrackMute(true, trackButton );

            for (AudioClip clip : track.clips){
                String name = TimelineController.ResolveName(clip.getResource());
                insertClip(true, name, trackButton, clip );
            }
        }
    }

    public void initializeUI(){

        findViewById(R.id.add_track_button).setOnClickListener(this);
        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
        findViewById(R.id.reset_start_end).setOnClickListener(this);
        findViewById(R.id.export_button).setOnClickListener(this);
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
                insertTrack(false, which);
                Log.i("Debug", "Track inserted of type ");
            }
        });
        builder.show();
    }

    private void SwapTrackDialog(View v) {

        final View clicked = v;
        final CharSequence Tracks[] = TimelineController.getTrackOptions();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick A Track Type");
        builder.setItems(Tracks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // the user clicked on colors[which]
                SwapTrack(clicked, clicked.getId(), which);
            }
        });
        builder.show();
    }

    private void TrackPressDialog(View v) {

        final View clicked = v;
        Log.i("Debug", "Track Selected: " + clicked.getId());
        final CharSequence TrackOptions[] = new CharSequence[]{"Add Clip", "Swap", "Remove", "Mute"};

        AlertDialog.Builder choiceDialog = new AlertDialog.Builder(TimelineActivity.this);
        choiceDialog.setTitle("Track Options");
        choiceDialog.setItems(TrackOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(which == 0){ AddClipDialog(clicked);
              } else if(which == 1) {
                   SwapTrackDialog(clicked);
               }else if (which == 2) {
                   RemoveTrack(clicked);
               } else if(which == 3){
                   ToggleTrackMute(false, clicked);
               }
            }});
        choiceDialog.show();
    }



    private void AddClipDialog(View v) {
        final View clicked = v;
        final int trackType = timeline.getTracks().get(clicked.getId()).getTrackType();
        final CharSequence Clips[] = TimelineController.getClipOptions(trackType);
        AlertDialog.Builder clipDialog = new AlertDialog.Builder(this);
        clipDialog.setTitle("Pick A Clip");
        clipDialog.setItems(Clips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                insertClip(Clips[which].toString(), clicked, trackType, which);

                Log.i("Debug", "New Clip, '" + Clips[which] + "' Added To Track: " + clicked .getId());
            }
        });
        clipDialog.show();
    }

    private void ClipPressDialog(View v){

        final View clicked = v;
        Log.i("Debug", "Clip selected: ");
        final CharSequence TrackOptions[] = new CharSequence[]{"Swap", "Remove", "Start Position", "End Position"};

        AlertDialog.Builder choiceDialog = new AlertDialog.Builder(TimelineActivity.this);
        choiceDialog.setTitle("Clip Options");
        choiceDialog.setItems(TrackOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){ SwapClipDialog(clicked);
                } else if (which == 1){RemoveClip(clicked);
                } else if (which == 2){ setStartPosition(clicked);
                } else if (which == 3) { setEndPosition(clicked);}

            }});
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
    private void insertTrack(boolean setup, int trackType) {

        LinearLayout trackLayout = (LinearLayout) findViewById(R.id.track_Layout);
        int tag = trackLayout.getChildCount()-1;

        //add new track to back end
        if(!setup){Track track = new Track(trackType);
            timeline.getTracks().add(track);}



        //add track to UI
        Button trackButton = new Button(this);
        trackButton.setId(tag);
        trackButton.setBackgroundColor(Color.TRANSPARENT);
        trackButton.setText(TimelineController.ResolveTrackType(trackType));
        trackButton.setOnClickListener(this);
        trackLayout.addView(trackButton, tag);

        //add clip layout to UI
        LinearLayout clipLayout = (LinearLayout) findViewById(R.id.clip_Layout);
        LinearLayout newClips = new LinearLayout(this);
        newClips.setOrientation(LinearLayout.HORIZONTAL);
        newClips.setBackgroundColor(trackTypeToColor(trackType));
        clipLayout.addView(newClips,tag);

        //pair clip layout with track
        tracksToClips.put(trackButton, newClips);
    }

    private void SwapTrack(View v, int trackIndex, int which) {

        Button trackButton = (Button) v;

        //change track type in the back end
        timeline.getTracks().get(trackIndex).setTrackType(which);
        timeline.getTracks().get(trackIndex).clips.clear();

        trackButton.setText(TimelineController.ResolveTrackType(which));
        LinearLayout clips = tracksToClips.get(trackButton);
        if(clips.getChildCount() > 0)
            clips.removeAllViews();
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
        updateLastPosition();
    }

    private void insertClip(boolean setup, String name, View v, AudioClip clip ) {

        final View clicked = v;
        Button button = new Button(getApplicationContext());
        button.setText(name);
        button.setTextColor(Color.WHITE);
        button.setOnClickListener(TimelineActivity.this);
        // the user clicked on colors[which]
        tracksToClips.get(clicked).addView(button);

        if(!setup)
            timeline.getTracks().get(clicked.getId()).clips.add(clip);

        updateLastPosition();
    }

    private void insertClip(String name, View v, int trackType, int which ) {

        final View clicked = v;
        Button button = new Button(getApplicationContext());
        button.setText(name);
        button.setTextColor(Color.WHITE);
        button.setOnClickListener(TimelineActivity.this);
        // the user clicked on colors[which]
        tracksToClips.get(clicked).addView(button);
        timeline.getTracks().get(clicked.getId()).clips.add(getClipFromSelection(trackType, which));
        updateLastPosition();
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
        updateLastPosition();

        Log.i("Debug", "Clip " + clipIndex + "removed from track " + trackIndex);

    }

    private void ToggleTrackMute(boolean setup, View v){

        final View clicked = v;
        int tag = clicked.getId();

        //mute track from back end
        if(!setup)
            timeline.getTracks().get(tag).toggleMute();

        boolean mute = timeline.getTracks().get(tag).isMute();
        //remove track/clip layout pairing
        Button trackButton = (Button) findViewById(tag);
        if(mute) {
            trackButton.setBackgroundColor(Color.RED);
        } else trackButton.setBackgroundColor(Color.TRANSPARENT);
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
            if(index == 0)
                audioClip.setResource(R.raw.drum1);
            else if(index == 1)
                audioClip.setResource(R.raw.drum2);
            else if(index == 2)
                audioClip.setResource(R.raw.drum3);
            else if(index == 3)
                audioClip.setResource(R.raw.drum4);
        } else if(trackType == Track.BASS){
            if(index == 0)
                audioClip.setResource(R.raw.bass1);
            else if(index == 1)
                audioClip.setResource(R.raw.bass2);
            else if(index == 2)
                audioClip.setResource(R.raw.bass3);
            else if(index == 3)
                audioClip.setResource(R.raw.bass4);
        } else if (trackType == Track.SAMPLE){
            if(index == 0)
                audioClip.setResource(R.raw.sample1);
            else if(index == 1)
                audioClip.setResource(R.raw.sample2);
            else if(index == 2)
                audioClip.setResource(R.raw.sample3);
            else if(index == 3)
                audioClip.setResource(R.raw.sample4);
        }  if(index == 4){
            audioClip.setResource(R.raw.emptyaudio);
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

            HashMap<Integer, List<AudioClip>> buffer =  prepareBuffer(timeline,  startingPoint, endingPoint);

            playBuffer(this,buffer);

        } else if (validTrackId(v.getId())){
            TrackPressDialog(v);
        } else if(v.getId() == R.id.reset_start_end){
            resetStartEndPoints();
        } else if(v.getId() == R.id.export_button){
            exportTimelineToMp3();
        }
        else{
            int parentId = ((View) v.getParent().getParent()).getId();
            if(parentId == R.id.clip_Layout){
                Log.i("Debug", "Clip Selected");
                ClipPressDialog(v);
            }
        }
    }

    private void exportTimelineToMp3() {

        if(!timeline.getTracks().isEmpty() && timeline.getTracks().get(0).clips != null){
            int resourceId = timeline.getTracks().get(0).clips.peek().getResource();
            serializeFile(timeline.getProjectName(), resourceId);
        } else{
            AlertDialog.Builder message = new AlertDialog.Builder(this);
            message.setMessage("Cannot export empty timeline!");
            message.setCancelable(true);
            message.show();
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

    public HashMap<Integer, List<AudioClip>> prepareBuffer(Timeline timeline, int start, int end){

        HashMap<Integer, List<AudioClip>> bufferMap = new HashMap();

        for (Track track : timeline.getTracks()) {
            if(track.isMute() == false) {
                int count = start;
                while (count < track.clips.size() && count <= end) {

                    AudioClip clip = (AudioClip) track.clips.toArray()[count];

                    if (bufferMap.containsKey(count))
                        bufferMap.get(count).add(clip);
                    else {
                        bufferMap.put(count, new ArrayList());
                        bufferMap.get(count).add(clip);
                    }
                    count++;
                }
            }
        }

        return bufferMap;
    }

    public void playBuffer(Context inContext, HashMap<Integer, List<AudioClip>> buffer){

        final ArrayList<Integer> positions = new ArrayList<>();
        final SoundPool spool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        spool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                Log.i("Debug", "Playing sound : " + sampleId);
                soundPool.play(sampleId, 100f, 100f, 1, 0, 1);
            }
        });

        final HashMap<Integer, List<AudioClip>> bufferMap = buffer;
        final Context context = inContext;
        positions.addAll(bufferMap.keySet());
        Collections.sort(positions);
        Collections.reverse(positions);

        Handler handler = new Handler();;
        for (Integer i : positions) {

            final int position = i;
            final List<AudioClip> toPlay = bufferMap.get(position);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Load the sample IDs
                    for (AudioClip clip : toPlay) {
                        spool.load(context, clip.getResource(), position);
                    }
                }
            }, 2820*position);
        }
    }

    public void serializeFile(String fileName, int rawId) {

        try {

            File myDir;
            myDir = new File(getFilesDir().getPath());
            myDir.mkdirs();

            // create a new file, to save the downloaded file

            String mFileName = fileName+".mp3";
            File file = new File(myDir, mFileName);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = getResources().openRawResource(rawId);


            // create a buffer...
            byte[] buffer = new byte[8*1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            // close the output stream when complete //
            fileOutput.close();

        } catch (final MalformedURLException e) {
            // showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            // showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            // showError("Error : Please check your internet connection " + e);
        }

        Log.i("Debug", "File: " + fileName + " written to disk");
    }
    @Override
    public void onBackPressed() {

        try { serializeTimeline();
        } catch (IOException e) { e.printStackTrace();}
        Intent entryIntent = new Intent();
        entryIntent.setClass(this, EntryActivity.class);
        startActivity(entryIntent);
        finish();
    }
}
