package ca.ggolda.reference_criminal_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

/**
 * Created by gcgol on 01/18/2017.
 */

public class ActivityMain extends AppCompatActivity {

    private ListView mListViewSections;
    private AdapterSection mAdapterSection;

    private AdapterHeading mAdapterHeading;
    private ListView mListViewHeadings;

    private ImageView mBtnParts;
    public static LinearLayout mParts;

    DbHelper dbHelper;

    public static int partsVisible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DbHelper.getInstance(getApplicationContext());

        mBtnParts = (ImageView) findViewById(R.id.btn_parts);
        mParts = (LinearLayout) findViewById(R.id.parts);

        mAdapterSection = new AdapterSection(ActivityMain.this, R.layout.card_section, dbHelper.getAllUser());
        mListViewSections = (ListView) findViewById(R.id.listview_section);
        mListViewSections.setAdapter(mAdapterSection);

        mAdapterHeading = new AdapterHeading(ActivityMain.this, R.layout.card_heading, dbHelper.getAllHeading());
        mListViewHeadings = (ListView) findViewById(R.id.listview_heading);
        mListViewHeadings.setAdapter(mAdapterHeading);

        // bring parts up or down
        mBtnParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                partsHideShow();
            }
        });

    }

    public static void partsHideShow() {
        if (partsVisible == 0) {
            mParts.setVisibility(View.VISIBLE);
            partsVisible = 1;
        } else if (partsVisible == 1) {
            mParts.setVisibility(View.GONE);
            partsVisible = 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ActivityTestLaunch.class);
        startActivity(intent);
        finish();
    }

}