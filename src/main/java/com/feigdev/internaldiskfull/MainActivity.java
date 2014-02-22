package com.feigdev.internaldiskfull;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;

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
        private Button stopBtn, startBtn;
        private TextView status;
        private static FileWriteTask fileWriteTask;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            status = (TextView) rootView.findViewById(R.id.status);
            status.setText("not running");

            startBtn = (Button) rootView.findViewById(R.id.start);
            stopBtn = (Button) rootView.findViewById(R.id.stop);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != fileWriteTask)
                        fileWriteTask.cancel(true);
                    fileWriteTask = new FileWriteTask();
                    fileWriteTask.execute();

                    status.setText("running");
                }
            });
            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != fileWriteTask)
                        fileWriteTask.cancel(true);
                    status.setText("not running");
                }
            });

            return rootView;
        }

        @Override
        public void onPause() {
            super.onPause();
            if (null != fileWriteTask)
                fileWriteTask.cancel(true);
        }

        private class FileWriteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                byte[] byteArray = generateByteArray();
                FileOutputStream fos = null;
                try {
                    fos = getActivity().openFileOutput("myBigDumbFile", Context.MODE_PRIVATE);
                    String filePath = getActivity().getFileStreamPath("myBigDumbFile").getPath();

                    Log.d(TAG, "filePath: " + filePath);
                    while (true) {
                        fos.write(byteArray);
                        fos.flush();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            public byte[] generateByteArray() {
                Bitmap bmp = ImgLoader.generatePic(getActivity());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            }

        }
    }

}
