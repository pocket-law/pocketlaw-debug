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

    EditText et_name, et_college, et_place, et_userid, et_number;
    Button btn_next, btn_db;
    TestDbHelper testDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_input_activity);


        testDbHelper = TestDbHelper.getInstance(getApplicationContext());

        et_name = (EditText) findViewById(R.id.et_name);
        et_college = (EditText) findViewById(R.id.et_college);
        et_place = (EditText) findViewById(R.id.et_place);
        et_userid = (EditText) findViewById(R.id.et_userid);
        et_number = (EditText) findViewById(R.id.et_number);
        btn_next = (Button) findViewById(R.id.btn_add);
        btn_db = (Button) findViewById(R.id.btn_db);



        // add to db
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUserData userData = new TestUserData();

                if (!et_name.getText().toString().isEmpty()) {
                    userData.name = et_name.getText().toString();
                } else {
                    userData.name = "";
                }
                if (!et_college.getText().toString().isEmpty()) {
                    userData.college = et_college.getText().toString();
                } else {
                    userData.college = "";
                }
                if (!et_userid.getText().toString().isEmpty()) {
                    userData.user_id = et_userid.getText().toString();
                } else {
                    userData.user_id = "";
                }
                if (!et_number.getText().toString().isEmpty()) {
                    userData.number = et_number.getText().toString();
                } else {
                    userData.number = "";
                }

                if (!et_place.getText().toString().isEmpty()) {
                    userData.place = et_place.getText().toString();
                } else {
                    userData.place = "";
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