package dalker.cmtruong.com.app.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;


/**
 * Date Converter
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
