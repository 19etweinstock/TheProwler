package com.a19etweinstock.theprowler;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ethan on 7/20/2017.
 */

public class SingleHeadline extends Fragment implements ButtonGetter{
    private View view;
    private String title;
    private String author;
    private String summary;
    private Bitmap pic;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.heading, container, false);
        return view;
    }

    public SingleHeadline setTitle(String string) {
        title = string;
        return this;
    }

    public String getTitle(){
        return title;
    }

    public SingleHeadline setAuthor(String string) {
        author = string;
        return this;
    }

    public SingleHeadline setSummary(String string) {
        summary = string;
        return this;
    }

    public SingleHeadline setPic(Bitmap bitmap) {
        pic = bitmap;
        return this;
    }

    public void update() {
        if(title != null)
        ((TextView) view.findViewById(R.id.heading_title)).setText(title);
        if(author != null)
        ((TextView) view.findViewById(R.id.heading_author)).setText(author);
        if(summary != null)
        ((TextView) view.findViewById(R.id.heading_summary)).setText(summary);
        if(pic != null)
        ((ImageView) view.findViewById(R.id.heading_pic)).setImageBitmap(pic);
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    @Override
    public Button getButton(){
        return (Button) view.findViewById(R.id.heading_button);
    }
}