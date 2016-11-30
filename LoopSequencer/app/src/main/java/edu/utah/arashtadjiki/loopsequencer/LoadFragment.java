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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Arash on 11/29/2016.
 */

public class LoadFragment extends Fragment implements View.OnClickListener, ListAdapter {

    private LinearLayout _rootLayout;
    private Button openButton;
    private ListView timelineList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setBackgroundColor(Color.DKGRAY);

        timelineList = new ListView(getContext());
        openButton = new Button(getContext());
        openButton.setText("Open");
        openButton.setOnClickListener(this);

        _rootLayout.addView(timelineList, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4));
        _rootLayout.addView(openButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        return _rootLayout;
    }

    @Override
    public void onClick(View v) {

        Log.i("Debug", "Opening Game");

    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public static LoadFragment newInstance() {

        return  new LoadFragment();
    }
}
