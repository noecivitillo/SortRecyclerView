package com.example.test.sortrecyclerview;

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
 * Created by Noe on 18/2/2017.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    private static final String DATABASE_NAME = "UserDatab";
    private static final int DATABASE_VERSION = 1;
    private static DbHelper mDbHelper;

    public static String TABLE_USERdETAIL = "userdetail";

    private static final String _ID = "_id";
    private static final String SORT_ID = "sort_id";
    private static final String NAME = "name";
    private static final String QUANTITY = "quantity";
    private static final String DESCRIPTION = "description";

    public static synchronized DbHelper getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DbHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERDETAIL_TABLE = "CREATE TABLE " + TABLE_USERdETAIL +
                "(" +
                _ID + " INTEGER PRIMARY KEY , " +
                SORT_ID + " INTEGER," +
                NAME + " TEXT," +
                QUANTITY + " INTEGER," +
                DESCRIPTION + " TEXT" +
                ")";


        db.execSQL(CREATE_USERDETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERdETAIL);

            onCreate(db);
        }
    }
    /**
     * Insert a  user detail into database
     */
    public void insertUserDetail(UserData userData) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(SORT_ID, userData.sort_id);
            values.put(NAME, userData.name);
            values.put(QUANTITY, userData.quantity);
            values.put(DESCRIPTION, userData.description);


            db.insertOrThrow(TABLE_USERdETAIL, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void updateUserDetail(int id, String v1, String v2, String v3) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, v1);
        values.put(QUANTITY, v2);
        values.put(DESCRIPTION, v3);
        db.update(TABLE_USERdETAIL, values, "_id=" + id, null);


    }

    public void updateUserData(UserData userData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME, userData.getName());
        values.put(QUANTITY, userData.getQuantity());
        values.put(DESCRIPTION, userData.getDescription());
        values.put(SORT_ID, userData.getSort());

        Log.i(TAG, "USER UPDATED = " + userData.getName());

        db.update(TABLE_USERdETAIL, values, _ID + "=?", new String[]{String.valueOf(userData.getId())});

    }

    /**
     * fetch all data from UserTable
     */
    public List<UserData> getAllUser() {

        List<UserData> usersdetail = new ArrayList<>();

        String USER_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_USERdETAIL + " ORDER BY " + SORT_ID + " COLLATE NOCASE;";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    UserData userData = new UserData();
                    userData.id = cursor.getString(cursor.getColumnIndex(_ID));
                    userData.sort_id = cursor.getInt(cursor.getColumnIndex(SORT_ID));
                    userData.name = cursor.getString(cursor.getColumnIndex(NAME));
                    userData.quantity = "Quantity: " + cursor.getString(cursor.getColumnIndex(QUANTITY));
                    userData.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));


                    usersdetail.add(userData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return usersdetail;

    }

    /**
     * Delete single row from UserTable
     */
    void deleteRow(String name) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("delete from " + TABLE_USERdETAIL + " where name ='" + name + "'");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    void deleteRowItem(String id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("delete from " + TABLE_USERdETAIL + " where _id ='" + id + "'");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public UserData getSingleUserDetail(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        UserData userData = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERdETAIL + " WHERE " + _ID + "= ?", new String[]{userId});

        try {
            while (cursor.moveToNext()) {
                userData = new UserData();
                userData.name = cursor.getString(cursor.getColumnIndex(NAME));
                userData.quantity = cursor.getString(cursor.getColumnIndex(QUANTITY));
                userData.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return userData;
    }
}