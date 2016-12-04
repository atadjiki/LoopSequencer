package edu.utah.arashtadjiki.loopsequencer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Arash on 11/29/2016.
 */

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timeline_layout);

        AudioClip sampleClip = new AudioClip();
        sampleClip.setAudioType(AudioClip.AUDIO);
        sampleClip.setResource(R.raw.sample1);

        AudioClip drumClip = new AudioClip();
        drumClip.setAudioType(AudioClip.AUDIO);
        drumClip.setResource(R.raw.drum1);

        Timeline timeline = new Timeline("default");
        timeline.getTracks().add(new Track(Track.SAMPLE));
        timeline.getTracks().get(0).clips.add(sampleClip);
        timeline.getTracks().add(new Track(Track.DRUM));
        timeline.getTracks().get(0).clips.add(drumClip);

        HashMap<Integer, List<AudioClip>> buffer =  TimelineController.prepareBuffer(timeline);
        TimelineController.playBuffer(this,buffer);

    }
}
