package edu.utah.arashtadjiki.loopsequencer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.path;
import static java.lang.System.out;

/**
 * Created by Arash on 11/29/2016.
 */

public class BrowseMP3Fragment extends Fragment implements ListAdapter, AdapterView.OnItemClickListener {

    private LinearLayout _rootLayout;
    private ListView _listView;
    private LinearLayout _buttonLayout;

    private List<String> pathList;

    private SendMp3Fragment _sendMp3Fragment;

    public interface OnSendMP3Listener{void OnSendMP3();}
    private OnSendMP3Listener onSendMP3Listener;
    public void setOnSendMP3Listener(OnSendMP3Listener _onSendMP3Listener){onSendMP3Listener = _onSendMP3Listener;}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            initialize();
            serializeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _rootLayout;
    }

    public void initialize() throws IOException {

        getActivity().setTitle("Browse MP3s");
        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setBackgroundColor(Color.DKGRAY);
        pathList = new ArrayList<>();
        populateList();

        _listView = new ListView(getContext());
        _listView.setOnItemClickListener(this);
        _listView.setBackgroundColor(Color.DKGRAY);
        _listView.setAdapter(this);

        _rootLayout.addView(_listView, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5));
    }

    public static BrowseMP3Fragment newInstance(){
        return new BrowseMP3Fragment();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        //show dialog
        final String path = pathList.get(position);

        final CharSequence Clips[] = new CharSequence[]{"Play", "Email"};
        AlertDialog.Builder clipDialog = new AlertDialog.Builder(getContext());
        clipDialog.setTitle("Select Actions");
        clipDialog.setItems(Clips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0){
                  //play mp3
                    playFile(path);
                } else if(which == 1){
                    //open send mp3 fragment
                    emailFile(path);
                }
            }
        });
        clipDialog.show();

    }

    private void emailFile(String path){

//        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse(path)));
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        email.putExtra(Intent.EXTRA_SUBJECT, "Loop Sequencer");
        email.putExtra(Intent.EXTRA_TEXT,"Exported by Loop Sequencer!");
        email.setType("audio/*");
        email.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        try  {startActivity(Intent.createChooser(email, "Send MP3"));}
        catch (android.content.ActivityNotFoundException ex) {}
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public Object getItem(int position) {

        return pathList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String path = pathList.get(position);
        TextView drawingSummaryView = new TextView(getContext());
        drawingSummaryView.setTextColor(Color.GREEN);
        drawingSummaryView.setTextSize(14f);
        drawingSummaryView.setText(path);
        return drawingSummaryView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    public void playFile(String path){

        MediaPlayer mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("AUDIO PLAYBACK", "prepare() failed");
        }
    }

    public void populateList() {

        pathList.clear();
        Log.i("File", "Attempting to populate List");
        String[] fileList = null;

        fileList = getActivity().getFilesDir().list();
        Log.i("File", "Opening mp3 folder: " + getActivity().getFilesDir().getPath());
        Log.i("File", "Counting " + (fileList.length-1) + " files: ");
        for (String file : fileList)
            Log.i("File", file);

        for (String filename : fileList) {

            if(filename.contains(".mp3")) {
                Log.v("serialization", filename);
                pathList.add(getActivity().getFilesDir().getPath()+File.separator+filename);
            }

        }
    }

    public void serializeFile() {

        try {

            File myDir;
            myDir = new File(getActivity().getFilesDir().getPath());
            myDir.mkdirs();

            // create a new file, to save the downloaded file

            String mFileName = "test.mp3";
            File file = new File(myDir, mFileName);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = getActivity().getResources().openRawResource(R.raw.sample1);


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
    }

}
