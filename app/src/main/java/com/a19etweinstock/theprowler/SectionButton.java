package com.a19etweinstock.theprowler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ethan on 7/20/2017.
 */

public class SectionButton extends Fragment implements ButtonGetter{
    private View view;
    private String text;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.section_button, container, false);
        return view;
    }

    public SectionButton setText(String string){
        text = string;
        return this;
    }

    public void updateText(){
        ((Button)view.findViewById(R.id.section_button)).setText(text);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateText();
    }

    @Override
    public Button getButton(){
        return ((Button)view.findViewById(R.id.section_button));
    }
}