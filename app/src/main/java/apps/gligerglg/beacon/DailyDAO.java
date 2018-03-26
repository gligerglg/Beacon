package apps.gligerglg.beacon;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/26/2018.
 */
@Dao
public interface DailyDAO {
    @Query("SELECT * FROM BeaconTBL_Daily")
    List<DailyRecord> getAllRecords();

    @Insert
    void addNewRecord(DailyRecord record);

    @Query("DELETE FROM BeaconTBL_Daily")
    void deleteAllRecords();

    @Query("SELECT COUNT(*) FROM BeaconTBL_Daily")
    int getRecordCount();
}
