package apps.gligerglg.beacon;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Gayan Lakshitha on 3/23/2018.
 */
@Entity(tableName = "BeaconTBL_Daily")
public class DailyRecord {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "reading")
    private int reading;
    @ColumnInfo(name = "units")
    private int units;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "days")
    private int days;

    public DailyRecord() {
    }

    @Ignore
    public DailyRecord(int id, int reading, int units, String date, int days) {
        this.id = id;
        this.reading = reading;
        this.units = units;
        this.date = date;
        this.days = days;
    }

    @Ignore
    public DailyRecord(int reading, int units, String date, int days) {
        this.reading = reading;
        this.units = units;
        this.date = date;
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getReading() {
        return reading;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
