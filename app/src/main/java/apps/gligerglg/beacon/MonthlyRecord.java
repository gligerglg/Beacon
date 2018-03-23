package apps.gligerglg.beacon;

/**
 * Created by Gayan Lakshitha on 3/23/2018.
 */

public class MonthlyRecord {
    private int id;
    private String month;
    private int units;
    private double charge;

    public MonthlyRecord() {
    }

    public MonthlyRecord(int id, String month, int units, double charge) {
        this.id = id;
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
