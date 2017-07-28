package com.a19etweinstock.theprowler;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {

    private SplashActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getName(), "Started");
        activity = this;
        new JSONParser().execute();
    }

    private class JSONParser extends AsyncTask<Void, Void, Bundle> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(getClass().getName(), "starting parsing");
        }

        @Override
        protected Bundle doInBackground(Void... params) {
            String json = loadJSONFromAsset();
            Bundle info = null;
            try {
                JSONObject paper = new JSONObject(json);
                info = Parser.parseJson(paper);
            }
            catch(JSONException e){
                e.printStackTrace();
                return info;
            }
            return info;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);
            startActivity(new Intent(activity, MainActivity.class).putExtra("info", bundle));
            activity.finish();
        }

        public String loadJSONFromAsset() {
            String json = null;
            try {
                InputStream is = getApplicationContext().getAssets().open("theprowler.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    }
}
