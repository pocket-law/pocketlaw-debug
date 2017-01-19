package ca.ggolda.reference_criminal_code;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by gcgol on 01/18/2017.
 */

public class TestActivityInfo extends AppCompatActivity {

    EditText et_fulltext;
    Button btn_next, btn_db;
    TestDbHelper testDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_input_activity);


        testDbHelper = TestDbHelper.getInstance(getApplicationContext());

        et_fulltext = (EditText) findViewById(R.id.et_fulltext);
        btn_next = (Button) findViewById(R.id.btn_add);
        btn_db = (Button) findViewById(R.id.btn_db);


        // add to db
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUserData userData = new TestUserData();

                if (!et_fulltext.getText().toString().isEmpty()) {
                    userData.fulltext = et_fulltext.getText().toString();
                } else {
                    userData.fulltext = "";
                }


                testDbHelper.insertUserDetail(userData);

                Intent intent=new Intent(TestActivityInfo.this,TestUserDetailList.class);
                startActivity(intent);
            }
        });


        // skip to db
        btn_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestActivityInfo.this,TestUserDetailList.class);
                startActivity(intent);
            }
        });


    }
}