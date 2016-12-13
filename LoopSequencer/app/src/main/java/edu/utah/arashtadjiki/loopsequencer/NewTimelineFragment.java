package edu.utah.arashtadjiki.loopsequencer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Arash on 11/29/2016.
 */

public class NewTimelineFragment extends Fragment implements View.OnClickListener {

    private LinearLayout _rootLayout;
    private TextView label;
    private EditText nameField;
    private Button createButton;

    public interface OnNewGameListener{void OnNewGame(String projectName);}
    public void setOnNewGameListener(OnNewGameListener _newGameListener){newGameListener = _newGameListener;}
    private OnNewGameListener newGameListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setGravity(Gravity.CENTER);
        _rootLayout.setDividerPadding(100);
        _rootLayout.setBackgroundColor(Color.DKGRAY);

        label = new TextView(getContext());
        label.setGravity(Gravity.CENTER);
        label.setText("Enter Name For New Project");
        label.setTextColor(Color.GREEN);
        label.setTextSize(24f);
        label.setEnabled(false);
        label.setClickable(false);
        _rootLayout.addView(label, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        nameField = new EditText(getContext());
        nameField.setText("default");
        nameField.setBackgroundColor(Color.WHITE);
        nameField.setEnabled(true);
        _rootLayout.addView(nameField, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        createButton = new Button(getContext());
        createButton.setOnClickListener(this);
        createButton.setText("Create");
        _rootLayout.addView(createButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        return _rootLayout;
    }

    @Override
    public void onClick(View v) {

        Log.i("Debug", "Creating Game");
        String projectname = nameField.getText().toString().trim().toLowerCase();
        Intent showTimelineIntent = new Intent();
        Timeline timeline = new Timeline(projectname);
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("timeline", timeline);
        showTimelineIntent.putExtra("timeline", bundle);
        showTimelineIntent.setClass(getContext(), TimelineActivity.class);
        startActivity(showTimelineIntent);
        getActivity().finish();


    }

    public static NewTimelineFragment newInstance(){
        return new NewTimelineFragment();
    }
}
