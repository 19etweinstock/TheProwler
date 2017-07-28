package com.a19etweinstock.theprowler;

import android.app.Application;
import android.os.AsyncTask;

/**
 * Created by ethan on 7/28/2017.
 */

public class ProwlerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new JSONGetter().execute();
    }

    public class JSONGetter extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
