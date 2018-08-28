package dalker.cmtruong.com.app.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dalker.cmtruong.com.app.model.Review;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 28th
 */
public class ListConverter {


    @TypeConverter
    public static ArrayList<Review> stringToReviewList(String data) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Review>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someReviewsToString(ArrayList<Review> reviews) {
        Gson gson = new Gson();
        return gson.toJson(reviews);
    }
}
