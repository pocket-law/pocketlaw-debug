package ca.ggolda.reference_criminal_code;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by gcgol on 01/18/2017.
 */

public class ActivityDebug extends AppCompatActivity {

    Button btn_next, btn_db, btn_exp, btn_imp;
    DbHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        dbHelper = DbHelper.getInstance(getApplicationContext());

        btn_next = (Button) findViewById(R.id.btn_add);
        btn_db = (Button) findViewById(R.id.btn_db);
        btn_exp = (Button) findViewById(R.id.btn_exp);
        btn_imp = (Button) findViewById(R.id.btn_imp);


        // add to db
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tODO: something here where it goes straight to DB if already populated
//                if (!et_fulltext.getText().toString().isEmpty()) {
//                    Section userData = new Section(0, "", "", et_fulltext.getText().toString());
//                    dbHelper.insertSectionDetail(userData);
//                }

                Intent intent=new Intent(ActivityDebug.this, ActivityPopulate.class);
                startActivity(intent);
            }
        });


        // skip to db
        btn_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDebug.this,ActivityMain.class);
                startActivity(intent);
            }
        });

        // export db
        btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDebug.this, TestActivityMain.class);
                startActivity(intent);
            }
        });



        // import db
        btn_imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}