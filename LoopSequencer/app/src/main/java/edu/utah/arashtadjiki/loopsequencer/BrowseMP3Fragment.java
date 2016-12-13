package edu.utah.arashtadjiki.loopsequencer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.StreamCorruptedException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _rootLayout;
    }

    public void initialize() throws IOException {

        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setBackgroundColor(Color.DKGRAY);
        serializeMp3(R.raw.sample1);
        pathList = new ArrayList<>();
        populateList();

        _listView = new ListView(getContext());
        _listView.setOnItemClickListener(this);
        _listView.setBackgroundColor(Color.DKGRAY);
        _listView.setAdapter(this);

        _rootLayout.addView(_listView, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5));
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
                ObjectInputStream input;

                try {
                    input = new ObjectInputStream(new FileInputStream(new File(getActivity().getFilesDir().getPath()+File.separator+filename)));
                    File file = (File) input.readObject();
                    Log.v("serialization", filename);
                    pathList.add(file.getAbsolutePath());
                    input.close();

                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
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
                    TimelineController.playFile(path);
                } else if(which == 1){
                    //open send mp3 fragment
                }
            }
        });
        clipDialog.show();

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

    public void serializeMp3(int id) throws IOException {

        String filename =  R.raw.bass1 + ".mp3";
        Uri path = Uri.parse("android.resource:///edu.utah.arashtadjiki.loopsequencer/"+id);
        File file = new File(path.toString());
        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(getActivity().getFilesDir(),"")+ File.separator+filename));
            out.writeObject(file);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] listOfFiles = getActivity().getFilesDir().list();
        Log.i("File", "Counting "+ (listOfFiles.length-1) + " files");
        Log.i("File", "File written: " + filename + " to " + getActivity().getFilesDir());
    }

}
