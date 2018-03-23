package apps.gligerglg.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/23/2018.
 */

public class MonthlyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "BeaconDB";
    private static final String TABLE = "BeaconTBL_Monthly";
    private static final String ID = "id";
    private static final String MONTH = "month";
    private static final String UNITS = "units";
    private static final String CHARGE = "charge";

    public MonthlyDBHelper(Context context) {
        super(context, DATABASE,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE
                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MONTH + " TEXT,"
                + UNITS + " INT,"
                + CHARGE + " DOUBLE)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    public List<MonthlyRecord> getAllRecords()
    {
        List<MonthlyRecord> records = new ArrayList<>();
        MonthlyRecord record = new MonthlyRecord();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE,null);
        if(cursor.moveToFirst()){
            do{
                record.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                record.setMonth(cursor.getString(cursor.getColumnIndex(MONTH)));
                record.setCharge(Double.parseDouble(cursor.getString(cursor.getColumnIndex(CHARGE))));
                record.setUnits(Integer.parseInt(cursor.getString(cursor.getColumnIndex(UNITS))));
                records.add(record);
            }while (cursor.moveToNext());
        }

        db.close();
        return records;
    }

    public MonthlyRecord getRecord(String month){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE + " WHERE " + MONTH + "=" + month,null);
        MonthlyRecord record = new MonthlyRecord();
        if(cursor.moveToFirst()){
            record.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
            record.setMonth(cursor.getString(cursor.getColumnIndex(MONTH)));
            record.setCharge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CHARGE))));
            record.setUnits(Integer.parseInt(cursor.getString(cursor.getColumnIndex(UNITS))));
        }
        database.close();
        return record;
    }

    public void addNewRecord(MonthlyRecord record){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONTH,record.getMonth());
        values.put(UNITS,record.getUnits());
        values.put(CHARGE,record.getCharge());
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
