package com.a19etweinstock.theprowler;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by ethan on 7/22/2017.
 */

public class ArticleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Bundle bundle, info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_main);

        bundle = getIntent().getExtras();
        info = bundle.getBundle("category");

        //set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(bundle.getString("categoryTitle"));



        //Create side menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();

        navMenu.clear();
        for (String s : info.getStringArrayList("articleTitles")) {
            if (s.equals(bundle.getString("title"))) {
                navMenu.add(0, Menu.FIRST, Menu.NONE, s).setChecked(true);
            } else {
                navMenu.add(0, Menu.FIRST, Menu.NONE, s);
            }
        }

        navMenu.setGroupCheckable(0, true, true);
        navigationView.setNavigationItemSelectedListener(this);

        //set title as category name in side view
        ((TextView)((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.name)).setText(bundle.getString("categoryTitle"));

        //set article text
        ((TextView)findViewById(R.id.article_title)).setText(getIntent().getExtras().getString("title","FAIL"));
        ((TextView)findViewById(R.id.article)).setText(getIntent().getExtras().getString("content","FAIL"));
        ((TextView)findViewById(R.id.article_author)).setText(getIntent().getExtras().getString("author","FAIL"));
    }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        String title = (String) item.getTitle();

        Bundle article = info.getBundle(title);

        ((TextView)findViewById(R.id.article_title)).setText(article.getString("title","FAIL"));
        ((TextView)findViewById(R.id.article)).setText(article.getString("article","FAIL"));
        ((TextView)findViewById(R.id.article_author)).setText(article.getString("author","FAIL"));

        (findViewById(R.id.scrollView)).scrollTo(0,0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
