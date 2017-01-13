package ca.ggolda.reference_criminal_code;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by gcgol on 01/06/2017.
 */

public class ActivityDummy extends AppCompatActivity {

    private ArrayList<Provision> provisions;
    private AdapterProvision mAdapterProvision;
    private ListView mListView;

    private LinearLayout mDummy;
    private LinearLayout mLocal;
    private LinearLayout mOnline;

    private ImageView mBtnParts;
    private LinearLayout mParts;
    private ImageView mBtnComments;
    private LinearLayout mComments;

    private int commentsVisible = 0;
    private int partsVisible = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDummy = (LinearLayout) findViewById(R.id.dummy);
        mLocal = (LinearLayout) findViewById(R.id.local_html);
        mOnline = (LinearLayout) findViewById(R.id.online);
        mDummy.setVisibility(View.VISIBLE);
        mLocal.setVisibility(View.GONE);
        mOnline.setVisibility(View.GONE);


        // bring comments up or down
        mBtnComments = (ImageView) findViewById(R.id.btn_comments);
        mComments = (LinearLayout) findViewById(R.id.comments);
        mBtnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("OOOU", "comments visible!" + commentsVisible);

                if (commentsVisible == 0) {
                    mComments.setVisibility(View.VISIBLE);
                    commentsVisible = 1;
                } else if (commentsVisible == 1) {
                    mComments.setVisibility(View.GONE);
                    commentsVisible = 0;
                }

            }
        });

        // bring parts up or down
        mBtnParts = (ImageView) findViewById(R.id.btn_parts);
        mParts = (LinearLayout) findViewById(R.id.parts);
        mBtnParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("OOOU", "parts visible!" + partsVisible);

                if (partsVisible == 0) {
                    mParts.setVisibility(View.VISIBLE);
                    partsVisible = 1;
                } else if (partsVisible == 1) {
                    mParts.setVisibility(View.GONE);
                    partsVisible = 0;
                }

            }
        });


        provisions = new ArrayList<>();


        //TODO: get provisions from a source
        Provision tempProv1 = new Provision(1, "Possession of weapon for dangerous purpose", "88(1)", "Every person commits an offence who carries or possesses a weapon, an imitation of a weapon, a prohibited device or any ammunition or prohibited ammunition for a purpose dangerous to the public peace or for the purpose of committing an offence.");
        Provision tempProv2 = new Provision(1, "Punishment", "(2)", "Every person who commits an offence under subsection (1)");
        Provision tempSub1 = new Provision(2, "(a)", "is guilty of an indictable offence and liable to imprisonment for a term not exceeding ten years; or");
        Provision tempSub2 = new Provision(2, "(b)", "is guilty of an offence punishable on summary conviction.");
        Provision tempCite = new Provision(3, "R.S., 1985, c. C-46, s. 89; 1995, c. 39, s. 139.");

        provisions.add(tempProv1);
        provisions.add(tempProv2);
        provisions.add(tempSub1);
        provisions.add(tempSub2);
        provisions.add(tempCite);

        provisions.add(tempProv1);
        provisions.add(tempProv2);
        provisions.add(tempSub1);
        provisions.add(tempSub2);
        provisions.add(tempCite);

        provisions.add(tempProv1);
        provisions.add(tempProv2);
        provisions.add(tempSub1);
        provisions.add(tempSub2);
        provisions.add(tempCite);
        // TODO: remove above...

        // Webview from web
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://laws-lois.justice.gc.ca/eng/acts/C-46/FullText.html");

        // Webview from local
        WebView myWebViewLocal = (WebView) findViewById(R.id.webview_local);
        myWebViewLocal.loadUrl("file:///android_res/raw/criminal_code_english.html");


        mAdapterProvision = new AdapterProvision(ActivityDummy.this, R.layout.card_provision, provisions);
        mListView = (ListView) findViewById(R.id.listview_section);
        mListView.setAdapter(mAdapterProvision);

    }


}
