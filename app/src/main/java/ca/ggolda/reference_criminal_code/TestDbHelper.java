package ca.ggolda.reference_criminal_code;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by gcgol on 02/01/2017.
 */



public class TestDbHelper extends SQLiteOpenHelper
{
    private static String TAG = "TestDbHelper"; // Tag just for the LogCat window



    //destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME ="CriminalCode";// Database name

    //Table Names
    private static final String TABLE_CRIMINAL_CODE = "criminalcode";


    // Criminal Code Table Columns
    private static final String _ID = "_id";
    private static final String FULLTEXT = "fulltext";
    private static final String TYPE = "type";
    private static final String SECTION = "section";
    private static final String PINPOINT = "pinpoint";

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    private static TestDbHelper mDbHelper;


    //TODO: find out if activitymain really needs this
    public static synchronized TestDbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (mDbHelper == null) {
            mDbHelper = new TestDbHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }


    public TestDbHelper(Context context)
    {
        super(context, DB_NAME, null, 1);// 1? Its database Version
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() throws IOException {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    // TODO: work this in with version control
     /*
   Insert a  user detail into database
   */

    public void insertSectionDetail(Section userData) {

        mDataBase.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FULLTEXT, userData.getFulltext());
            values.put(TYPE, userData.getType());
            values.put(SECTION, userData.getSection());
            values.put(PINPOINT, userData.getPinpoint());

            mDataBase.insertOrThrow(TABLE_CRIMINAL_CODE, null, values);
            mDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to add post to database");
        } finally {

            mDataBase.endTransaction();
        }


    }



   /*
   fetch all data from UserTable
    */

    public List<Section> getAllUser() {

        List<Section> sectionDetail = new ArrayList<>();

        String USER_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_CRIMINAL_CODE;

        Cursor cursor = mDataBase.rawQuery(USER_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Section sectionData = new Section(1, -777, "dbhelper", "dbhelper", "dbhelper");
                    sectionData.setID(Integer.valueOf(cursor.getString(cursor.getColumnIndex(_ID))));
                    sectionData.setFulltext(cursor.getString(cursor.getColumnIndex(FULLTEXT)));
                    sectionData.setType(Integer.valueOf(cursor.getString(cursor.getColumnIndex(TYPE))));
                    sectionData.setSection(cursor.getString(cursor.getColumnIndex(SECTION)));
                    sectionData.setPinpoint(cursor.getString(cursor.getColumnIndex(PINPOINT)));

                    sectionDetail.add(sectionData);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return sectionDetail;

    }


     /*
   fetch all headings from UserTable
    */

    public List<Section> getAllHeading() {

        List<Section> sectionDetail = new ArrayList<>();

        // TODO: Display section 849
        String USER_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_CRIMINAL_CODE + " WHERE type = '0'";


        Cursor cursor = mDataBase.rawQuery(USER_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Section sectionData = new Section(1, -777, "dbhelper", "dbhelper", "dbhelper");
                    sectionData.setID(Integer.valueOf(cursor.getString(cursor.getColumnIndex(_ID))));
                    sectionData.setType(Integer.valueOf(cursor.getString(cursor.getColumnIndex(TYPE))));
                    sectionData.setPinpoint(cursor.getString(cursor.getColumnIndex(PINPOINT)));
                    sectionData.setSection(cursor.getString(cursor.getColumnIndex(SECTION)));
                    sectionData.setFulltext(cursor.getString(cursor.getColumnIndex(FULLTEXT)));

                    sectionDetail.add(sectionData);


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        // Add schedules, forms, related provisions, and amendments not in force
        Section schedulesAdd = new Section(-1, 737, "schedules", "S", "Schedules");
        sectionDetail.add(schedulesAdd);
        Section formsAdd = new Section(-2, 737, "forms", "F", "Forms");
        sectionDetail.add(formsAdd);
        Section relatedProvsAdd = new Section(-3, 737, "related_provs", "RP", "Related Provisions");
        sectionDetail.add(relatedProvsAdd);
        Section amendmentsNIFAdd = new Section(-4, 737, "amendments_nif", "ANIF", "Amendments Not In Force");
        sectionDetail.add(amendmentsNIFAdd);


        return sectionDetail;

    }

}