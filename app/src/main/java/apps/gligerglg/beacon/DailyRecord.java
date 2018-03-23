package apps.gligerglg.beacon;

/**
 * Created by Gayan Lakshitha on 3/23/2018.
 */

public class DailyRecord {
    private int id;
    private int reading;
    private int units;
    private String date;

    public DailyRecord() {
    }

    public DailyRecord(int id, int reading, int units, String date) {
        this.id = id;
        this.reading = reading;
        this.units = units;
        this.date = date;
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
