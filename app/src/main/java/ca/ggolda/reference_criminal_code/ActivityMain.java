package ca.ggolda.reference_criminal_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by gcgol on 01/18/2017.
 */

public class ActivityMain extends AppCompatActivity {

    ListView mListViewSections;
    AdapterSection mAdapterSection;

    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DbHelper.getInstance(getApplicationContext());

        mAdapterSection = new AdapterSection(ActivityMain.this, R.layout.card_heading, dbHelper.getAllUser());
        mListViewSections = (ListView) findViewById(R.id.listview_section);
        mListViewSections.setAdapter(mAdapterSection);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ActivityTestLaunch.class);
        startActivity(intent);
        finish();
    }

}