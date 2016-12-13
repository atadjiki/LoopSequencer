package edu.utah.arashtadjiki.loopsequencer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arash on 12/3/2016.
 */

/*
 * An external class that manages the
 * timeline data model, such as playing tracks,
 * adding and removing clips and creating audio clips.
 */
public class TimelineController {

    public static HashMap<Integer, List<AudioClip>> prepareBuffer(Timeline timeline, int start, int end){

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

    public static void playBuffer(Context inContext, HashMap<Integer, List<AudioClip>> buffer){

        final ArrayList<Integer> positions = new ArrayList<>();
        final SoundPool spool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        final MediaPlayer mplayer = new MediaPlayer();

        final HashMap<Integer, List<AudioClip>> bufferMap = buffer;
        final Context context = inContext;
        positions.addAll(bufferMap.keySet());
        Collections.sort(positions);

        for (Integer position : positions) {

            List<AudioClip> toPlay = bufferMap.get(position);

            // Load the sample IDs
            for (AudioClip clip : toPlay) {
                spool.load(context, clip.getResource(), position);
            }

            spool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                    soundPool.play(sampleId, 100f, 100f, 1, 0, 1);
                }
            });
        }
    }

    public static void playFile(String path){

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
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

    public static String ResolveName(int resourceID){

        if(resourceID == R.raw.bass1)
            return "Bass 1";
        else if(resourceID == R.raw.bass2)
            return "Bass 2";
        else if(resourceID == R.raw.bass3)
            return "Bass 3";
        else if(resourceID == R.raw.bass4)
            return "Bass 4";

        else if(resourceID == R.raw.drum1)
            return "Drum 1";
        else if(resourceID == R.raw.drum2)
            return "Drum 2";
        else if(resourceID == R.raw.drum3)
            return "Drum 3";
        else if(resourceID == R.raw.drum4)
            return "Drum 4";

        else if(resourceID == R.raw.sample1)
            return "Sample 1";
        else if(resourceID == R.raw.sample2)
            return "Sample 2";
        else if(resourceID == R.raw.sample3)
            return "Sample 3";
        else if(resourceID == R.raw.sample4)
            return "Sample 4";
        else if(resourceID == R.raw.emptyaudio){
            return "Empty";
        }
        else
            return null;
    }

    public static String ResolveTrackType(int trackType){

        if (trackType == Track.DRUM)
            return "Drum";
        else if (trackType == Track.SAMPLE)
            return "Sample";
        else if(trackType == Track.BASS)
            return "Bass";

        return null;
    }
}
