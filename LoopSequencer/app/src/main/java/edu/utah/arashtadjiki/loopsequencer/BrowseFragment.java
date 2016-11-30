package edu.utah.arashtadjiki.loopsequencer;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
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

public class BrowseFragment extends Fragment implements ListAdapter, View.OnClickListener {

    private LinearLayout _rootLayout;
    private ListView _listView;
    private LinearLayout _buttonLayout;
    private Button _playButton;
    private Button _sendButton;

    private SendMp3Fragment _sendMp3Fragment;

    public interface OnSendMP3Listener{void OnSendMP3();}
    private OnSendMP3Listener onSendMP3Listener;
    public void setOnSendMP3Listener(OnSendMP3Listener _onSendMP3Listener){onSendMP3Listener = _onSendMP3Listener;}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initialize();
        return _rootLayout;
    }

    public void initialize(){

        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setBackgroundColor(Color.BLACK);

        _listView = new ListView(getContext());

        _buttonLayout = new LinearLayout(getContext());
        _buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        _playButton = new Button(getContext());
        _playButton.setText("Play");
        _playButton.setTextColor(Color.GREEN);

        _sendButton = new Button(getContext());
        _sendButton.setText("Send Mp3");
        _sendButton.setTextColor(Color.GREEN);
        _sendButton.setOnClickListener(this);

        _rootLayout.addView(_listView, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5));
        _buttonLayout.addView(_playButton);
        _buttonLayout.addView(_sendButton);
        _rootLayout.addView(_buttonLayout, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
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

    public static BrowseFragment newInstance(){
        return new BrowseFragment();
    }

    @Override
    public void onClick(View v) {

        if( v==_sendButton){
            onSendMP3Listener.OnSendMP3();
        }
    }
}
