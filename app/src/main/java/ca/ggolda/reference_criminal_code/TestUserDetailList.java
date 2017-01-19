package ca.ggolda.reference_criminal_code;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

/**
 * Created by gcgol on 01/18/2017.
 */

public class TestUserDetailList extends AppCompatActivity implements TestListener {

    // TODO: remove recyclerview and dependency
    RecyclerView recyclerView;
    TestListAdapter recyclerAdapter;

    ListView mListViewSections;
    AdapterSection mAdapterSection;

    TestDbHelper testDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_database_activity);


        testDbHelper = TestDbHelper.getInstance(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.rv_contactlist);
        recyclerAdapter = new TestListAdapter(this, testDbHelper.getAllUser());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapterSection = new AdapterSection(TestUserDetailList.this, R.layout.card_heading, testDbHelper.getAllUser());
        mListViewSections = (ListView) findViewById(R.id.listview_section);
        mListViewSections.setAdapter(mAdapterSection);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TestActivityInfo.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void fulltextToChange(String name) {
        testDbHelper.deleteRow(name);

        recyclerAdapter = new TestListAdapter(this, testDbHelper.getAllUser());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}