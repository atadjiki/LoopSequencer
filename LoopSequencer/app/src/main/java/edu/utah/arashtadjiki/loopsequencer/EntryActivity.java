package edu.utah.arashtadjiki.loopsequencer;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {


    //initial view
    private LinearLayout _rootLayout;
    private Button _newButton;
    private Button _loadButton;
    private Button _resumeButton;
    private Button _browseButton;

    //on mp3 browse
    public static final String BROWSE_FRAGMENT_TAG = "BrowseFragment";
    public static final String SEND_MP3_FRAGMENT_TAG = "SendMP3Fragment";
    public static final String NEW_TIMELINE_FRAGMENT_TAG = "NewTimelineFragment";
    public static final String LOAD_FRAGMENT_TAG = "LoadFragment";
    private NewTimelineFragment _newTimelineFragment;
    private SendMp3Fragment _sendMp3Fragment;
    private LoadFragment _loadFragment;

    private FrameLayout _browseLayout;
    private FrameLayout _newTimelineLayout;
    private FrameLayout _loadLayout;
    private FrameLayout _sendMp3Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        setContentView(_rootLayout);
    }

    private void initialize() {

        _rootLayout = new LinearLayout(this);
        _rootLayout.setDividerPadding(100);
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        //put logo/image here

        _newButton = new Button(this);
        _newButton.setText("New Timeline");
        _newButton.setOnClickListener(this);

        _loadButton = new Button(this);
        _loadButton.setText("Load Timeline");
        _loadButton.setOnClickListener(this);

        _resumeButton = new Button(this);
        _resumeButton.setText("Resume Timeline");
        _resumeButton.setOnClickListener(this);

        _browseButton = new Button(this);
        _browseButton.setText("Browse Recordings");
        _browseButton.setOnClickListener(this);

        _rootLayout.addView(_newButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        _rootLayout.addView(_loadButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        _rootLayout.addView(_resumeButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        _rootLayout.addView(_browseButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

    }

    @Override
    public void onClick(View v) {

        if(_newButton == v){
            //launch timeline activity -- new
            Log.i("Debug", "new");
           launchNew();
        } else if(_loadButton == v){
            //goto timeline browse screen
            Log.i("Debug", "load");
            launchLoad();
        } else if(_resumeButton == v){
            //go back to last opened activity
            Log.i("Debug", "resume");
            //launch timeline activity
        }else if(_browseButton == v){
            //browse mp3 files recorded to device
            Log.i("Debug", "browse");
            launchBrowse();
        }
    }

    private void launchLoad() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        LoadFragment _loadFragment = (LoadFragment) getSupportFragmentManager().findFragmentByTag(LOAD_FRAGMENT_TAG);

        _loadLayout = new FrameLayout(this);
        _loadLayout.setId(12);

        if(_loadFragment == null) {
            _loadFragment = edu.utah.arashtadjiki.loopsequencer.LoadFragment.newInstance();
            transaction.add(_loadLayout.getId(), _loadFragment, "LoadFragment");
        }
        else{

            _loadFragment = edu.utah.arashtadjiki.loopsequencer.LoadFragment.newInstance();
            transaction.replace(_loadLayout.getId(),_loadFragment);
        }
        transaction.commit();
        setContentView(_loadLayout);
    }

    private void launchNew() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        NewTimelineFragment _newTimelineFragment = (NewTimelineFragment) getSupportFragmentManager().findFragmentByTag(NEW_TIMELINE_FRAGMENT_TAG);

        _newTimelineLayout = new FrameLayout(this);
        _newTimelineLayout.setId(11);

        if(_newTimelineFragment == null) {
            _newTimelineFragment = NewTimelineFragment.newInstance();
            transaction.add(_newTimelineLayout.getId(), _newTimelineFragment, "NewTimelineFragment");
        }
        else{

            _newTimelineFragment = NewTimelineFragment.newInstance();
            transaction.replace(_newTimelineLayout.getId(),_newTimelineFragment);
        }
        transaction.commit();
        setContentView(_newTimelineLayout);
    }

    private void launchBrowse() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BrowseFragment _browseFragment = (BrowseFragment) getSupportFragmentManager().findFragmentByTag(BROWSE_FRAGMENT_TAG);

        _browseLayout = new FrameLayout(this);
        _browseLayout.setId(10);

        if(_browseFragment == null) {
            _browseFragment = BrowseFragment.newInstance();
            transaction.add(_browseLayout.getId(), _browseFragment, "BrowseFragment");
        }
        else{

            _browseFragment = BrowseFragment.newInstance();
            transaction.replace(_browseLayout.getId(), _browseFragment);
        }
        _browseFragment.setOnSendMP3Listener(new BrowseFragment.OnSendMP3Listener() {
            @Override public void OnSendMP3() {launchSendMP3();}});
        transaction.commit();
        setContentView(_browseLayout);
    }

    private void launchSendMP3() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SendMp3Fragment _sendMp3Fragment = (SendMp3Fragment) getSupportFragmentManager().findFragmentByTag(SEND_MP3_FRAGMENT_TAG);

        _sendMp3Layout = new FrameLayout(this);
        _sendMp3Layout.setId(10);

        if(_sendMp3Fragment == null) {
            _sendMp3Fragment = SendMp3Fragment.newInstance();
            transaction.add(_sendMp3Layout.getId(), _sendMp3Fragment, "SendMp3Fragment");
        }
        else{

            _sendMp3Fragment = SendMp3Fragment.newInstance();
            transaction.replace(_sendMp3Layout.getId(), _sendMp3Fragment);
        }
        transaction.commit();
        setContentView(_sendMp3Layout);

    }

    @Override
    public void onBackPressed() {
        if(_rootLayout != null)
            setContentView(_rootLayout);
    }


}
