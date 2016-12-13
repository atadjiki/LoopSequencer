package edu.utah.arashtadjiki.loopsequencer;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String BROWSE_FRAGMENT_TAG = "BrowseMP3Fragment";
    public static final String SEND_MP3_FRAGMENT_TAG = "SendMP3Fragment";
    public static final String NEW_TIMELINE_FRAGMENT_TAG = "NewTimelineFragment";
    public static final String LOAD_FRAGMENT_TAG = "LoadTimelineFragment";

    private NewTimelineFragment _newTimelineFragment;
    private SendMp3Fragment _sendMp3Fragment;
    private LoadTimelineFragment _loadTimelineFragment;

    private FrameLayout _browseLayout;
    private FrameLayout _newTimelineLayout;
    private FrameLayout _loadLayout;
    private FrameLayout _sendMp3Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Loop Sequencer");
        setContentView(R.layout.entry_layout);
        initialize();
    }

    private void initialize() {

        findViewById(R.id.new_project_button).setOnClickListener(this);

        findViewById(R.id.load_project_button).setOnClickListener(this);

        findViewById(R.id.browse_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.new_project_button){
            //launch timeline activity -- new
            Log.i("Debug", "new");
           launchNew();
        } else if(v.getId() == R.id.load_project_button){
            //goto timeline browse screen
            Log.i("Debug", "load");
            launchLoad();
        } else if(v.getId() == R.id.browse_button){
            //browse mp3 files recorded to device
            Log.i("Debug", "browse");
            launchBrowse();
        }
    }

    private void launchLoad() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        LoadTimelineFragment _loadTimelineFragment = (LoadTimelineFragment) getSupportFragmentManager().findFragmentByTag(LOAD_FRAGMENT_TAG);

        _loadLayout = new FrameLayout(this);
        _loadLayout.setId(12);

        if(_loadTimelineFragment == null) {
            _loadTimelineFragment = LoadTimelineFragment.newInstance();
            transaction.add(_loadLayout.getId(), _loadTimelineFragment, "LoadTimelineFragment");
        }
        else{

            _loadTimelineFragment = LoadTimelineFragment.newInstance();
            transaction.replace(_loadLayout.getId(), _loadTimelineFragment);
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
        BrowseMP3Fragment _browseMP3Fragment = (BrowseMP3Fragment) getSupportFragmentManager().findFragmentByTag(BROWSE_FRAGMENT_TAG);

        _browseLayout = new FrameLayout(this);
        _browseLayout.setId(10);

        if(_browseMP3Fragment == null) {
            _browseMP3Fragment = BrowseMP3Fragment.newInstance();
            transaction.add(_browseLayout.getId(), _browseMP3Fragment, "BrowseMP3Fragment");
        }
        else{

            _browseMP3Fragment = BrowseMP3Fragment.newInstance();
            transaction.replace(_browseLayout.getId(), _browseMP3Fragment);
        }
        _browseMP3Fragment.setOnSendMP3Listener(new BrowseMP3Fragment.OnSendMP3Listener() {
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
        setContentView(R.layout.entry_layout);
        initialize();
    }
}
