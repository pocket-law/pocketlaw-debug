package ca.ggolda.reference_criminal_code;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.net.URL;

/**
 * Created by gcgol on 01/10/2017.
 */

public class ActivityOnline extends AppCompatActivity {
    public static final String ANY = "Any";
    private static final String URL = "http://laws-lois.justice.gc.ca/eng/XML/C-46.xml";

    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;



    private LinearLayout mOnline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        mOnline = (LinearLayout) findViewById(R.id.online);

        loadPage();
    }



    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "connection error";
            } catch (XmlPullParserException e) {
                return "xml error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            setContentView(R.layout.activity_network);
            // Displays the HTML string in the UI via a WebView
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadData(result, "text/html", null);

        }
    }

    // Uses AsyncTask to download the XML feed from laws-lois.justice.gc.ca.
    public void loadPage() {

            new DownloadXmlTask().execute(URL);

    }

    // Uploads XML from online source
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        XmlParser xmlParser = new XmlParser();
        List<XmlParser.Section> sections = null;
        String definedTermEn = null;



        try {
            stream = downloadUrl(urlString);
            sections = xmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }



        // XmlParser returns a List (called "sections") of Section objects.
        // Each Section object represents a single section in the XML feed.
        // Each entry is displayed in the UI.
//        StringBuilder definedTermString = new StringBuilder();
//        for (XmlParser.Section section : sections) {
//
//            Log.e("XML", "ActivityOnline" + section);
//
//            //TODO: stuff here
//
//        }

        Log.e("XML sections.get(0)", "" + sections.get(0));

        return ""+sections.size();
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}