package com.a19etweinstock.theprowler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener{

    private Button pressedButton = null;
    private Bundle info = null;

    private String visibleEdition = null;
    private String visibleCategory = null;

    private List<SectionButton> buttons;
    private List<SingleHeadline> headlines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = getIntent().getBundleExtra("info");
        visibleEdition = info.getString("currentEdition");
        buttons = new ArrayList<>();
        headlines = new ArrayList<>();

        //init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create side menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();

        navMenu.clear();
        boolean checked = false;
        for (String s : info.getStringArrayList("editionTitles")) {
            if (!checked) {
                checked = true;
                navMenu.add(0, Menu.FIRST, Menu.NONE, s).setChecked(true);
            } else {
                navMenu.add(0, Menu.FIRST, Menu.NONE, s);
            }
        }

        navMenu.setGroupCheckable(0, true, true);
        navigationView.setNavigationItemSelectedListener(this);

        //Create headlines and category buttons
        if(savedInstanceState == null) {
            for (String s : info.getBundle(visibleEdition).getStringArrayList("categoryTitles")) {
                if(visibleCategory == null){
                    visibleCategory = s;
                }
                SectionButton sectionButton = new SectionButton().setText(s);
                buttons.add(sectionButton);
                getSupportFragmentManager().beginTransaction().add(R.id.button_container, sectionButton).commitNow();
            }

            for(String s : info.getBundle(visibleEdition).getBundle(visibleCategory).getStringArrayList("articleTitles")){
                Bundle article = info.getBundle(visibleEdition).getBundle(visibleCategory).getBundle(s);
                SingleHeadline singleHeadline = new SingleHeadline().setTitle(article.getString("title"))
                        .setAuthor(article.getString("author")).setSummary(article.getString("summary"));
                headlines.add(singleHeadline);
                getSupportFragmentManager().beginTransaction().add(R.id.title_container, singleHeadline).commitNow();
            }
        }

        //Set edition info
        ((TextView)findViewById(R.id.edition_display)).setText(info.getBundle(visibleEdition).getString("title"));
        ((TextView)findViewById(R.id.time_display)).setText(info.getBundle(visibleEdition).getString("time"));
    }

    //Set up buttons and headlines for clicking and set text for headline buttons
    @Override
    protected void onStart() {
        super.onStart();
        for(Fragment f : getSupportFragmentManager().getFragments()){
            if(f instanceof SectionButton) {
                if (pressedButton == null)
                    pressedButton = ((SectionButton) f).getButton();
                ((SectionButton) f).getButton().setOnClickListener(this);
            }
            if(f instanceof SingleHeadline) {
                ((SingleHeadline)f).getButton().setOnClickListener(this);
                ((SingleHeadline)f).getButton().setText(((SingleHeadline)f).getTitle());
            }
        }
        pressedButton.setBackgroundResource(R.drawable.selected_background);
    }

    //handle drawer layout
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v){
        if(v instanceof Button) {
            //if sectionbutton
            if(v.getId() == R.id.section_button) {
                //update Button
                if (pressedButton != null)
                    pressedButton.setBackgroundResource(R.drawable.unselected_background);
                pressedButton = (Button) v;
                pressedButton.setBackgroundResource(R.drawable.selected_background);
                visibleCategory = (String)pressedButton.getText();
                //remove headlines
                for(int i = headlines.size()-1; i >=0; i--) {
                    getSupportFragmentManager().beginTransaction().detach(headlines.get(i)).commitNow();
                    headlines.remove(i);

                }
                //create new headlines
                headlines = new ArrayList<>();
                for(String s : info.getBundle(visibleEdition).getBundle(visibleCategory).getStringArrayList("articleTitles")){
                    Bundle article = info.getBundle(visibleEdition).getBundle(visibleCategory).getBundle(s);
                    SingleHeadline singleHeadline = new SingleHeadline().setTitle(article.getString("title"))
                            .setAuthor(article.getString("author")).setSummary(article.getString("summary"));
                    headlines.add(singleHeadline);
                    getSupportFragmentManager().beginTransaction().add(R.id.title_container, singleHeadline).commitNow();
                    singleHeadline.getButton().setOnClickListener(this);
                }

                for(Fragment f : getSupportFragmentManager().getFragments()){
                    if(f instanceof SingleHeadline) {
                        ((SingleHeadline)f).getButton().setOnClickListener(this);
                        ((SingleHeadline)f).getButton().setText(((SingleHeadline)f).getTitle());
                    }
                }
            }
            //if headline
            else if (v.getId() == R.id.heading_button){
                Bundle article = info.getBundle(visibleEdition).getBundle(visibleCategory).getBundle((String)((Button) v).getText());
                startActivity(new Intent(this, ArticleActivity.class)
                        .putExtra("title",article.getString("title"))
                        .putExtra("content", article.getString("article"))
                        .putExtra("author", article.getString("author"))
                        .putExtra("category", info.getBundle(visibleEdition).getBundle(visibleCategory))
                        .putExtra("categoryTitle", visibleCategory)
                );
            }
        }
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String edition = (String)item.getTitle();

        visibleEdition = edition;
        visibleCategory = null;
        pressedButton = null;

        //remove existing buttons and headlines
        for(int i = headlines.size()-1; i >=0; i--) {
            getSupportFragmentManager().beginTransaction().detach(headlines.get(i)).commitNow();
            headlines.remove(i);

        }
        for(int i = buttons.size()-1; i >=0; i--) {
            getSupportFragmentManager().beginTransaction().detach(buttons.get(i)).commitNow();
            buttons.remove(i);

        }
        //create new buttons
        for (String s : info.getBundle(visibleEdition).getStringArrayList("categoryTitles")) {
            if(visibleCategory == null){
                visibleCategory = s;
            }
            SectionButton sectionButton = new SectionButton().setText(s);
            getSupportFragmentManager().beginTransaction().add(R.id.button_container, sectionButton).commitNow();
            if(pressedButton == null){
                pressedButton = sectionButton.getButton();
                pressedButton.setBackgroundResource(R.drawable.selected_background);
            }
            sectionButton.getButton().setOnClickListener(this);
            buttons.add(sectionButton);
        }
        //create new headlines
        for(String s : info.getBundle(visibleEdition).getBundle(visibleCategory).getStringArrayList("articleTitles")){
            Bundle article = info.getBundle(visibleEdition).getBundle(visibleCategory).getBundle(s);
            SingleHeadline singleHeadline = new SingleHeadline()
                    .setTitle(article.getString("title"))
                    .setAuthor(article.getString("author"))
                    .setSummary(article.getString("summary"));
            getSupportFragmentManager().beginTransaction().add(R.id.title_container, singleHeadline).commitNow();

            singleHeadline.getButton().setText(singleHeadline.getTitle());
            singleHeadline.getButton().setOnClickListener(this);
            headlines.add(singleHeadline);
        }

        //Set edition info
        ((TextView)findViewById(R.id.edition_display)).setText(info.getBundle(visibleEdition).getString("title"));
        ((TextView)findViewById(R.id.time_display)).setText(info.getBundle(visibleEdition).getString("time"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
