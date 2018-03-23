package apps.gligerglg.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/23/2018.
 */

public class DailyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "BeaconDB";
    private static final String TABLE = "BeaconTBL_Daily";
    private static final String ID = "id";
    private static final String READING = "reading";
    private static final String UNITS = "units";
    private static final String DATE = "date";

    public DailyDBHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE
                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + READING + " INT,"
                + UNITS + " INT,"
                + DATE + " TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    public List<DailyRecord> getAllRecords()
    {
        List<DailyRecord> records = new ArrayList<>();
        DailyRecord record = new DailyRecord();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE,null);
        if(cursor.moveToFirst()){
            do{
                record.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                record.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                record.setReading(Integer.parseInt(cursor.getString(cursor.getColumnIndex(READING))));
                record.setUnits(Integer.parseInt(cursor.getString(cursor.getColumnIndex(UNITS))));
                records.add(record);
            }while (cursor.moveToNext());
        }

        db.close();
        return records;
    }

    public DailyRecord getRecord(String date){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE + " WHERE " + DATE + "=" + date,null);
        DailyRecord record = new DailyRecord();
        if(cursor.moveToFirst()){
            record.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
            record.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
            record.setReading(Integer.parseInt(cursor.getString(cursor.getColumnIndex(READING))));
            record.setUnits(Integer.parseInt(cursor.getString(cursor.getColumnIndex(UNITS))));
        }
        database.close();
        return record;
    }

    public void addNewRecord(DailyRecord record){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(READING,record.getReading());
        values.put(UNITS,record.getUnits());
        values.put(DATE,record.getDate());
        database.insert(TABLE,null,values);
        database.close();
    }

    public void deleteAllRecords(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.close();
    }

    public int getRecordCount(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE,null);
        cursor.close();
        return cursor.getCount();
    }
}
