package ca.ggolda.reference_criminal_code;


import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by gcgol on 01/18/2017.
 */

public class ActivityDebug extends AppCompatActivity {

    Button btn_next, btn_db, btn_exp, btn_imp_two;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);


        dbHelper = DbHelper.getInstance(getApplicationContext());

        btn_next = (Button) findViewById(R.id.btn_add);
        btn_db = (Button) findViewById(R.id.btn_view);
        btn_imp_two = (Button) findViewById(R.id.btn_imp_two);
        btn_exp = (Button) findViewById(R.id.btn_exp);


        // add to db
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tODO: something here where it goes straight to DB if already populated
//                if (!et_fulltext.getText().toString().isEmpty()) {
//                    Section userData = new Section(0, "", "", et_fulltext.getText().toString());
//                    dbHelper.insertSectionDetail(userData);
//                }

                Intent intent = new Intent(ActivityDebug.this, ActivityPopulate.class);
                startActivity(intent);
            }
        });


        // skip to db
        btn_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDebug.this, ActivityMain.class);
                startActivity(intent);
            }
        });


        // export db
        btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ActivityDebug.this, "CLICKED!", Toast.LENGTH_SHORT).show();


                exportDB();


            }
        });


        // import db
        btn_imp_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("EEEP", "trying... 0");

                Toast.makeText(ActivityDebug.this, "CLICKED!", Toast.LENGTH_SHORT).show();

                try {
                    importDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("EEEP", "trying... 999");
            }
        });


    }

    private void exportDB() {

        // SD card
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "ca.ggolda.reference_criminal_code" + "/databases/CriminalCode";
        String backupDBPath = "/tmp/CriminalCode";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {

            source = new FileInputStream(currentDB).getChannel();

            destination = new FileOutputStream(backupDB).getChannel();

            destination.transferFrom(source, 0, source.size());

            source.close();
            destination.close();

            Toast.makeText(ActivityDebug.this, "DB Exported!", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importDB() throws IOException {
        //Open your local db as the input stream
        InputStream in = getApplicationContext().getAssets().open("CriminalCode");

        String destPath = "/data/data/" + "ca.ggolda.reference_criminal_code" + "/databases/CriminalCode";

        // Create empty file at destination path
        // TODO: can in future put a check here
        File f = new File(destPath);

        //Open the empty db as the output stream
        OutputStream out = new FileOutputStream(destPath);

        Log.e("EEEP", "trying... dfafa 2");

        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();

        Toast.makeText(ActivityDebug.this, "DB imported!", Toast.LENGTH_SHORT).show();

        Log.e("EEEP", "horray... 7");
    }

}