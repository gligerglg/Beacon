package apps.gligerglg.beacon;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Gayan Lakshitha on 3/23/2018.
 */
@Entity(tableName = "BeaconTBL_Monthly")
public class MonthlyRecord {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "month")
    private String month;
    @ColumnInfo(name = "units")
    private int units;
    @ColumnInfo(name = "charge")
    private double charge;

    public MonthlyRecord() {
    }

    @Ignore
    public MonthlyRecord(int id, String month, int units, double charge) {
        this.id = id;
        this.month = month;
        this.units = units;
        this.charge = charge;
    }

    @Ignore
    public MonthlyRecord(String month, int units, double charge) {
        this.month = month;
        this.units = units;
        this.charge = charge;
    }

    public String getMonth() {
        return month;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }
}
