package apps.gligerglg.beacon;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Gayan Lakshitha on 3/26/2018.
 */
@Database(entities = {MonthlyRecord.class,DailyRecord.class}, version = 1, exportSchema = false)
public abstract class BeaconDB extends RoomDatabase {
    public abstract MonthlyDAO monthlyDAO();
    public abstract DailyDAO dailyDAO();
}
