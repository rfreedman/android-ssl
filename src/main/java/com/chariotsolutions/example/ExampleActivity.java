package com.chariotsolutions.example;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chariotsolutions.example.http.Api;
import com.chariotsolutions.example.http.AuthenticationParameters;
import com.chariotsolutions.example.util.IOUtil;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;

@ContentView(R.layout.main)
public class ExampleActivity extends RoboActivity {
    private static final String TAG = ExampleActivity.class.getSimpleName();

    private Api exampleApi;

    @InjectView(R.id.mainTextView)
    TextView mainTextView;

    @InjectView(R.id.mainTextScroller)
    ScrollView mainTextScroller;

    @InjectResource(R.string.server_cert_asset_name)
    String caCertificateName;

    @InjectResource(R.string.client_cert_file_name)
    String clientCertificateName;

    @InjectResource(R.string.client_cert_password)
    String clientCertificatePassword;

    @InjectResource(R.string.example_url)
    String exampleUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();

        doRequest();
    }

    private void updateOutput(String text) {
        mainTextView.setText(mainTextView.getText() + "\n\n" + text);
    }

    private void doRequest() {

        try {
            AuthenticationParameters authParams = new AuthenticationParameters();
            authParams.setClientCertificate(getClientCertFile());
            authParams.setClientCertificatePassword(clientCertificatePassword);
            authParams.setCaCertificate(readCaCert());

            exampleApi = new Api(authParams);
            updateOutput("Connecting to " + exampleUrl);

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... objects) {

                    try {
                        String result = exampleApi.doGet(exampleUrl);
                        int responseCode = exampleApi.getLastResponseCode();
                        if (responseCode == 200) {
                            publishProgress(result);
                        } else {
                            publishProgress("HTTP Response Code: " + responseCode);
                        }

                    } catch (Throwable ex) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PrintWriter writer = new PrintWriter(baos);
                        ex.printStackTrace(writer);
                        writer.flush();
                        writer.close();
                        publishProgress(ex.toString() + " : " + baos.toString());
                    }

                    return null;
                }

                @Override
                protected void onProgressUpdate(final Object... values) {
                    StringBuilder buf = new StringBuilder();
                    for (final Object value : values) {
                        buf.append(value.toString());
                    }
                    updateOutput(buf.toString());
                }

                @Override
                protected void onPostExecute(final Object result) {
                    updateOutput("Done!");
                }
            }.execute();

        } catch (Exception ex) {
            Log.e(TAG, "failed to create timeApi", ex);
            updateOutput(ex.toString());
        }
    }

    private File getClientCertFile() {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        return new File(externalStorageDir, clientCertificateName);
    }

    private String readCaCert() throws Exception {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open(caCertificateName);
        return IOUtil.readFully(inputStream);
    }
}
