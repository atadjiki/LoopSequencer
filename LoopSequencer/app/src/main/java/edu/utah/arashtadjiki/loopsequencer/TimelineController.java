package edu.utah.arashtadjiki.loopsequencer;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Adler32;

/**
 * Created by Arash on 12/3/2016.
 */

/*
 * An external class that manages the
 * timeline data model, such as playing tracks,
 * adding and removing clips and creating audio clips.
 */
public class TimelineController {

    public static void addTrack(Timeline timeline, int trackType){
        timeline.getTracks().add(new Track(trackType));
    }

    public static HashMap<Integer, List<AudioClip>> prepareBuffer(Timeline timeline){

        HashMap<Integer, List<AudioClip>> bufferMap = new HashMap();

        for (Track track : timeline.getTracks()) {
            int count = 0;
            while(count < track.clips.size()){

                AudioClip clip = (AudioClip) track.clips.toArray()[count];

                if(bufferMap.containsKey(count))
                    bufferMap.get(count).add(clip);
                else {
                    bufferMap.put(count, new ArrayList());
                    bufferMap.get(count).add(clip);
                }


                count++;
            }
        }

        return bufferMap;
    }

    public static void playBuffer(Context context, HashMap<Integer, List<AudioClip>> bufferMap){

        ArrayList<Integer> positions = new ArrayList<>();
        SoundPool spool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        positions.addAll(bufferMap.keySet());
        Collections.sort(positions);

        for (Integer position : positions) {

            List<AudioClip> toPlay = bufferMap.get(position);

            // Sound pool new instance

            // Sound pool on load complete listener
            spool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    Log.i("OnLoadCompleteListener","Sound "+sampleId+" loaded.");
                    soundPool.play(sampleId, 200f, 200f, 1, 0, 1);
                }});
            // Load the sample IDs
            for (AudioClip clip : toPlay) {
                spool.load(context, clip.getResource(), position);
            }
        }
    }

    public static CharSequence[] getClipOptions(int TrackType){

        if(TrackType == Track.SAMPLE){

            return new CharSequence[]{"Sample 1", "Sample 2", "Sample 3", "Sample 4", "Empty"};

        }else if (TrackType == Track.DRUM){

            return new CharSequence[]{"Drum 1", "Drum 2", "Drum 3", "Drum 4", "Empty"};

        } else if (TrackType == Track.BASS){

            return new CharSequence[]{"Bass 1", "Bass 2", "Bass 3", "Bass 4", "Empty"};

        } else
            return null;
    }

    public static CharSequence[] getTrackOptions(){



        return new CharSequence[]{"Drum", "Bass", "Sample"};
    }
}
