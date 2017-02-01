package ca.ggolda.reference_criminal_code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcgol on 01/18/2017.
 */

public class DbHelperImport extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    // Database Info
    private static final String DATABASE_NAME = "CriminalCode";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_CRIMINAL_CODE = "criminalcode";


    // Criminal Code Table Columns
    private static final String _ID = "_id";
    private static final String FULLTEXT = "fulltext";
    private static final String TYPE = "type";
    private static final String SECTION = "section";
    private static final String PINPOINT = "pinpoint";

    private static DbHelperImport mDbHelper;


    public static synchronized DbHelperImport getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (mDbHelper == null) {
            mDbHelper = new DbHelperImport(context.getApplicationContext());
        }
        return mDbHelper;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DbHelperImport(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

   /*
    Called when the database is created for the FIRST time.
    If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    */

    @Override
    public void onCreate(SQLiteDatabase db) {


//        String CREATE_USERDETAIL_TABLE = "CREATE TABLE " + TABLE_CRIMINAL_CODE +
//                "(" +
//                _ID + " INTEGER PRIMARY KEY ," +
//                FULLTEXT + " TEXT, " +
//                TYPE + " TEXT, " +
//                SECTION + " TEXT, " +
//                PINPOINT + " TEXT" +
//                ")";
//        db.execSQL(CREATE_USERDETAIL_TABLE);

        try {
            importDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
       Called when the database needs to be upgraded.
       This method will only be called if a database already exists on disk with the same DATABASE_NAME,
       but the DATABASE_VERSION is different than the version of the database that exists on disk.
       */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRIMINAL_CODE);

            try {
                importDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
   Insert a  user detail into database
   */



    public static String DB_FILEPATH = "/data/data/ca.ggolda.reference_criminal_code/databases/CriminalCode.db";

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     */
    public void importDatabase() throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        String dbPath = "android.resource://ca.ggolda.reference_criminal_code/" + R.raw.criminal_code_db;;

        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            CopyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();

            Log.e("SUCCESS","DbHELPER copied DB");
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