package dalker.cmtruong.com.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author davidetruong
 * @version 1.0
 * @since 02 August 2018
 */
public class Dob implements Parcelable {

    private String date;

    private int age;

    public Dob(int age) {
        this.age = age;
    }

    protected Dob(Parcel in) {
        date = in.readString();
        age = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(age);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dob> CREATOR = new Creator<Dob>() {
        @Override
        public Dob createFromParcel(Parcel in) {
            return new Dob(in);
        }

        @Override
        public Dob[] newArray(int size) {
            return new Dob[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "{" +
                "date='" + date + '\'' +
                ", age=" + age +
                '}';
    }
}
