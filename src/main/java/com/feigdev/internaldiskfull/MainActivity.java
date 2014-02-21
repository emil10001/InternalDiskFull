package com.feigdev.internaldiskfull;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "PlaceholderFragment";
        private TextView tv;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            tv = (TextView) rootView.findViewById(R.id.tv);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            new FileWriteTask().execute();
        }

        private class FileWriteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                int count = 7000;
                byte[] byteArray = generateByteArray();
                while (true) {
                    try {
                        FileOutputStream fos = getActivity().openFileOutput("myBigDumbFile" + count, Context.MODE_PRIVATE);
                        String filePath = getActivity().getFileStreamPath("myBigDumbFile" + count).getPath();

                        Log.d(TAG, "filePath: " + filePath);
                        count++;

                        fos.write(byteArray);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public byte[] generateByteArray(){
                Bitmap bmp = ImgLoader.generatePic(getActivity());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            }

        }
    }

}
