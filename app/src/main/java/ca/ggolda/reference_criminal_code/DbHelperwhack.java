package ca.ggolda.reference_criminal_code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcgol on 02/01/2017.
 */

public class DbHelperwhack extends SQLiteAssetHelper {

    private String TAG = this.getClass().getName();

    private static final String DATABASE_NAME = "criminal_code";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_CRIMINAL_CODE = "criminalcode";

    // Criminal Code Table Columns
    private static final String _ID = "_id";
    private static final String FULLTEXT = "fulltext";
    private static final String TYPE = "type";
    private static final String SECTION = "section";
    private static final String PINPOINT = "pinpoint";


    public DbHelperwhack(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //TODO: find out if activitymain really needs this
    public static synchronized DbHelperwhack getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        DbHelperwhack mDbHelper = new DbHelperwhack(context.getApplicationContext());

        return mDbHelper;
    }


    // TODO: use this query builder !
//    public Cursor getEmployees() {
//
//        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//        String [] sqlSelect = {"0 _id", "FirstName", "LastName"};
//        String sqlTables = "Employees";
//
//        qb.setTables(sqlTables);
//        Cursor c = qb.query(db, sqlSelect, null, null,
//                null, null, null);
//
//        c.moveToFirst();
//        return c;
//
//    }



    // TODO: work this in with version control
     /*
   Insert a  user detail into database
   */

    public void insertSectionDetail(Section userData) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FULLTEXT, userData.getFulltext());
            values.put(TYPE, userData.getType());
            values.put(SECTION, userData.getSection());
            values.put(PINPOINT, userData.getPinpoint());

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

        List<Section> sectionDetail = new ArrayList<>();

        String USER_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_CRIMINAL_CODE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_DETAIL_SELECT_QUERY, null);

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

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_DETAIL_SELECT_QUERY, null);

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