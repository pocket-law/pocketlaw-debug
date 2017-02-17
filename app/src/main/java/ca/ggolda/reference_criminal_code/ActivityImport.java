package ca.ggolda.reference_criminal_code;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by gcgol on 01/10/2017.
 */

public class ActivityImport extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate);

        try {
            importDB();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void checkImportStatus() {

        String destPath = getApplicationContext().getDatabasePath("CriminalCode").getPath();

        // Create empty file at destination path
        boolean test = new File(destPath).exists();

        if (!test) {

            Log.e("EEEEP", "but how?");

        } else {
            Intent intent = new Intent(ActivityImport.this, ActivityMain.class);
            startActivity(intent);
        }

    }


    private void importDB() throws IOException {

        //Open your assets db as the input stream
        InputStream in = getApplicationContext().getAssets().open("CriminalCode");

        String destPath = getApplicationContext().getDatabasePath("CriminalCode").getPath();

        // Create empty file at destination path
        File f = new File(destPath);

        //Open the empty db as the output stream
        try {
            OutputStream out = new FileOutputStream(new File(destPath));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();

            checkImportStatus();

        } catch (FileNotFoundException e) {
            Log.e("DB Import", "File not foound" + e);
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        finish();
    }

}