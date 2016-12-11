package edu.utah.arashtadjiki.loopsequencer;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arash on 11/29/2016.
 */

public class LoadFragment extends Fragment implements View.OnClickListener, ListAdapter, AdapterView.OnItemClickListener {

    private LinearLayout _rootLayout;
    private Button openButton;
    private ListView timelineList;
    private List<Timeline> timelines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setBackgroundColor(Color.DKGRAY);

        timelines = new ArrayList<>();
        populateList();

        timelineList = new ListView(getContext());
        timelineList.setOnItemClickListener(this);
        timelineList.setBackgroundColor(Color.DKGRAY);
        timelineList.setAdapter(this);

        openButton = new Button(getContext());
        openButton.setText("Open");
        openButton.setOnClickListener(this);

        _rootLayout.addView(timelineList, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4));
      //  _rootLayout.addView(openButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        return _rootLayout;
    }

    public void populateList() {

        timelines.clear();
        Log.i("File", "Attempting to populate List");
        String[] fileList = null;

        fileList = getActivity().getFilesDir().list();
        Log.i("File", "Opening timeline folder: " + getActivity().getFilesDir().getPath());
        Log.i("File", "Counting " + (fileList.length-1) + " files: ");
        for (String file : fileList)
            Log.i("File", file);

        for (String filename : fileList) {

            if(filename.contains("timeline")) {
                ObjectInputStream input;

                try {
                    input = new ObjectInputStream(new FileInputStream(new File(getActivity().getFilesDir().getPath()+File.separator+filename)));
                    Timeline timeline = (Timeline) input.readObject();
                    Log.v("serialization", filename);
                    timelines.add(timeline);
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

    @Override
    public void onClick(View v) {

        Log.i("Debug", "Opening Game");

    }

    @Override
    public int getCount() {
        return timelines.size();
    }

    @Override
    public Object getItem(int position) {

        return timelines.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Timeline timeline = (Timeline) getItem(position);
        TextView drawingSummaryView = new TextView(getContext());
        drawingSummaryView.setTextColor(Color.GREEN);
        drawingSummaryView.setTextSize(14f);
        drawingSummaryView.setText("Timeline: " + timeline.getProjectName() + " with track count " + timeline.getTracks().size());
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

    public static LoadFragment newInstance() {

        return  new LoadFragment();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
