package apps.gligerglg.beacon;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/26/2018.
 */
@Dao
public interface MonthlyDAO {

    @Query("SELECT * FROM BeaconTBL_Monthly")
    List<MonthlyRecord> getAllRecords();

    @Insert
    void addNewRecord(MonthlyRecord record);

    @Query("DELETE FROM BeaconTBL_Monthly")
    void deleteAllRecords();

    @Query("SELECT COUNT(*) FROM BeaconTBL_Monthly")
    int getRecordCount();
}
