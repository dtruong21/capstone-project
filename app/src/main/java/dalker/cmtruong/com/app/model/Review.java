package dalker.cmtruong.com.app.model;


import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author davidetruong
 * @version 1.0
 * @since 02 August 2018
 */

public class Review implements Parcelable {

    private String id;

    private int rate;

    private String comment;

    @Ignore
    public Review() {
    }

    public Review(String id, int rate, String comment) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
    }

    protected Review(Parcel in) {
        id = in.readString();
        rate = in.readInt();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(rate);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", rate=" + rate +
                ", comment='" + comment + '\'' +
                '}';
    }
}
