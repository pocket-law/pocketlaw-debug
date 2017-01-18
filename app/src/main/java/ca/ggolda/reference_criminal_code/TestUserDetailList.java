package ca.ggolda.reference_criminal_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by gcgol on 01/18/2017.
 */

public class TestUserDetailList extends AppCompatActivity implements TestListener {
    RecyclerView recyclerView;
    TestDbHelper testDbHelper;
    TestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_database_recyclerview_activity);


        testDbHelper = TestDbHelper.getInstance(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.rv_contactlist);
        adapter = new TestListAdapter(this, testDbHelper.getAllUser());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TestActivityInfo.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void nameToChange(String name) {
        testDbHelper.deleteRow(name);

        adapter = new TestListAdapter(this, testDbHelper.getAllUser());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}