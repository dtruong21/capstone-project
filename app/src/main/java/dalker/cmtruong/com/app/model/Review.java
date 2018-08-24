package dalker.cmtruong.com.app.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author davidetruong
 * @version 1.0
 * @since 02 August 2018
 */
public class Review implements Parcelable {

    private int id;

    private int rate;

    private String comment;

    public Review(int rate, String comment) {
        this.rate = rate;
        this.comment = comment;
    }

    protected Review(Parcel in) {
        id = in.readInt();
        rate = in.readInt();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
