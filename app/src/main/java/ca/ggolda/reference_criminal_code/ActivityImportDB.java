package ca.ggolda.reference_criminal_code;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by gcgol on 01/26/2017.
 */


public class ActivityImportDB extends Activity {


    public static String DB_PATH = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        DB_PATH = cw.getFilesDir().getAbsolutePath() + "/databases/";

        copyDataBase();

    }


    private void copyDataBase() {
        Log.i("Database",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = getApplicationContext().getAssets().open("criminal_code_db.db");
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput = new FileOutputStream(DB_PATH + "CriminalCode.db");
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "New database has been copied to device!");


        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(ActivityImportDB.this, ActivityMain.class);
        startActivity(intent);
    }
}
