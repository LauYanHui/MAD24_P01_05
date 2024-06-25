package sg.edu.np.mad.cookbuddy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "grocerylist.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GROCERIES = "groceries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_GROCERIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERIES);
        onCreate(db);
    }

    // Open the database
    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }

    // Add a grocery item
    public void addGrocery(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        db.insert(TABLE_GROCERIES, null, values);
    }

    // Delete a grocery item by name
    public void deleteGrocery(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROCERIES, COLUMN_NAME + " = ?", new String[]{name});
    }

    // Get all grocery items
    public List<String> getAllGroceries() {
        List<String> groceries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GROCERIES, new String[]{COLUMN_ID, COLUMN_NAME}, null, null, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex(COLUMN_NAME);
            if (columnIndex >= 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String grocery = cursor.getString(columnIndex);
                    groceries.add(grocery);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return groceries;
    }
}
