package ca.ggolda.reference_criminal_code;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by gcgol on 01/26/2017.
 */




public class ActivityImportDB extends Activity {


    public static String DB_FILEPATH = "/data/data/ca.ggolda.reference_criminal_code/databases/CriminalCode.db";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        try {
            importDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     */
    public void importDatabase() throws IOException {

        Log.e("SUCCESSIO","importDatabase called");
        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        String dbPath = "android.resource://ca.ggolda.reference_criminal_code/" + R.raw.criminal_code_db;;

        Log.e("SUCCESSIO",""+dbPath);

        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            CopyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.

            Log.e("SUCCESSIO","DbHELPER copied DB");

            Intent intent = new Intent(ActivityImportDB.this, ActivityMain.class);
            startActivity(intent);
        }
    }



    public static void CopyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

}