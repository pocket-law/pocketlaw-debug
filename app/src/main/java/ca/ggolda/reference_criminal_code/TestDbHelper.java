package ca.ggolda.reference_criminal_code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcgol on 01/18/2017.
 */

public class TestDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    // Database Info
    private static final String DATABASE_NAME = "CriminalCode";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_CRIMINAL_CODE = "criminalcode";


    // Criminal Code Table Columns
    private static final String _ID = "_id";
    private static final String FULLTEXT = "fulltext";

    private static TestDbHelper mTestDbHelper;


    public static synchronized TestDbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (mTestDbHelper == null) {
            mTestDbHelper = new TestDbHelper(context.getApplicationContext());
        }
        return mTestDbHelper;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

   /*
    Called when the database is created for the FIRST time.
    If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    */

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERDETAIL_TABLE = "CREATE TABLE " + TABLE_CRIMINAL_CODE +
                "(" +
                _ID + " INTEGER PRIMARY KEY ," +
                FULLTEXT + " TEXT" +
                ")";
        db.execSQL(CREATE_USERDETAIL_TABLE);
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

            onCreate(db);
        }
    }

    /*
   Insert a  user detail into database
   */

    public void insertSectionDetail(Section userData) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FULLTEXT, userData.getFulltext());

            db.insertOrThrow(TABLE_CRIMINAL_CODE, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to add post to database");
        } finally {

            db.endTransaction();
        }


    }

   /*
   fetch all data from UserTable
    */

    public List<Section> getAllUser() {

        List<Section> usersdetail = new ArrayList<>();

        String USER_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_CRIMINAL_CODE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Section userData = new Section(0,"","","");
                    userData.setFulltext(cursor.getString(cursor.getColumnIndex(FULLTEXT)));


                    usersdetail.add(userData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return usersdetail;

    }

    /*
   Delete single row from UserTable
     */
    void deleteRow(String fulltext) {
        SQLiteDatabase db = getWritableDatabase();


        try {
            db.beginTransaction();
            db.execSQL("delete from " + TABLE_CRIMINAL_CODE + " where fulltext ='" + fulltext + "'");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d(TAG, "Error while trying to delete  users detail");
        } finally {
            db.endTransaction();
        }


    }

}