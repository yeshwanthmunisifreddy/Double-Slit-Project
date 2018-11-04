package technology.nine.doubleslitproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static technology.nine.doubleslitproject.data.Contract.ImageRefrence.IMAGEREFERENCE_TABLE_NAME;
import static technology.nine.doubleslitproject.data.Contract.ImageRefrence.URI;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Image.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String SQL_CREATE_IMAGE_REFERENCE = " CREATE TABLE " + IMAGEREFERENCE_TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY, " +
            URI + " Text);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_IMAGE_REFERENCE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertImage(String uri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(URI, uri);
        return (db.insert(IMAGEREFERENCE_TABLE_NAME, null, values) != -1);
    }

    public boolean existImage(String uri) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean returnValue = false;
        String selection = URI + " = ?";
        String[] selectionArgs = new String[]{uri};
        Cursor cursor = db.query(IMAGEREFERENCE_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.getCount() != 0) {
            returnValue = true;
        }
        return returnValue;
    }
}
