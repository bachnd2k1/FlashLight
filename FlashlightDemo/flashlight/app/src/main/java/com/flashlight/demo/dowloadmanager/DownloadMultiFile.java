package com.flashlight.demo.dowloadmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.flashlight.demo.R;
import com.flashlight.demo.util.MyUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadMultiFile extends AsyncTask<String, Integer, String> {

    private static final String TAG = "DOWNLOADFILE";

    private final PostDownload callback;
    private  Context context;
    private FileDescriptor fd;
    private  List<String> listLocation;
    private ProgressDialog mProgressDialog;


    public DownloadMultiFile(List<String> listLocation, Context context, boolean showDialog, PostDownload callback) {
        this.context = context;
        this.callback = callback;
        this.listLocation = listLocation;
        if (showDialog) {

            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(MyUtils.getString(R.string.text_downloading));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setIndeterminate(false);
            if (this.mProgressDialog != null) {
                this.mProgressDialog.show();
            }

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;
        int step = 100/aurl.length;
        try {
            for (int i=0; i<aurl.length; i++) {
                File file;

                URL url = new URL(aurl[i]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghtOfFile = connection.getContentLength();
                Log.d(TAG, "Length of the file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                file = new File(listLocation.get(i));
                FileOutputStream output = new FileOutputStream(file); //context.openFileOutput("content.zip", Context.MODE_PRIVATE);
                Log.d(TAG, "file saved at " + file.getAbsolutePath());
                fd = output.getFD();

                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * step / lenghtOfFile) + step*i));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            }
        } catch (Exception e) {
            Log.e("ErrorDownloadMulti: ", e.getMessage());
        }
        return null;

    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        if (this.mProgressDialog != null) {
            if (progress[0]>=0 && progress[0]<=100) {
                mProgressDialog.setProgress(progress[0]);
            }
        }
    }

    @Override
    protected void onPostExecute(String unused) {
        if (this.mProgressDialog != null) {
            mProgressDialog.setProgress(0);
            mProgressDialog.dismiss();
        }
        if (callback != null) callback.downloadDone();
    }

    public interface PostDownload {
        void downloadDone();
    }
}

