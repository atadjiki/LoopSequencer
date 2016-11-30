package edu.utah.arashtadjiki.loopsequencer;

import android.content.Context;
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

public class SendMp3Fragment extends Fragment implements View.OnClickListener {

    private LinearLayout _rootLayout;
    private TextView label;
    private EditText emailView;
    private Button sendButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        _rootLayout = new LinearLayout(getContext());
        _rootLayout.setOrientation(LinearLayout.VERTICAL);
        _rootLayout.setDividerPadding(100);
        _rootLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        _rootLayout.setBackgroundColor(Color.BLACK);

        label = new TextView(getContext());
        label.setText("Enter Email Address To Send File To:");
        label.setTextColor(Color.GREEN);
        label.setTextSize(24f);
        label.setEnabled(false);
        label.setClickable(false);
        _rootLayout.addView(label, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        emailView = new EditText(getContext());
        emailView.setEnabled(true);
        emailView.setText("_@_.com");
        _rootLayout.addView(emailView, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        sendButton = new Button(getContext());
        sendButton.setText("Send MP3");
        sendButton.setOnClickListener(this);

        _rootLayout.addView(sendButton, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        return _rootLayout;
    }

    @Override
    public void onClick(View v) {

        Log.i("Debug", "Sending MP3");
    }

    public static SendMp3Fragment newInstance() {
        return new SendMp3Fragment();
    }
}
