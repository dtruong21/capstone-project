package dalker.cmtruong.com.app.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

/**
 * User model
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int idUser;

    private String gender;

    @Embedded
    private Login login;

    @Embedded
    private Name name;

    @Embedded
    private Location location;

    private String email;

    private String dob;

    private String registered;

    private int phone;

    @ColumnInfo(name = "picture_url")
    private String pictureURL;

    private String nat;

    private String description;

    private double rate;

    private int price;
    @Embedded
    private ArrayList<Review> reviews;

    @Ignore
    public User(String gender, Login login, Name name, Location location, String email, String dob, String registered, int phone, String pictureURL, String nat, String description, double rate, int price, ArrayList<Review> reviews) {
        this.gender = gender;
        this.login = login;
        this.name = name;
        this.location = location;
        this.email = email;
        this.dob = dob;
        this.registered = registered;
        this.phone = phone;
        this.pictureURL = pictureURL;
        this.nat = nat;
        this.description = description;
        this.rate = rate;
        this.price = price;
        this.reviews = reviews;
    }

    public User(int idUser, String gender, Login login, Name name, Location location, String email, String dob, String registered, int phone, String pictureURL, String nat, String description, double rate, int price, ArrayList<Review> reviews) {
        this.idUser = idUser;
        this.gender = gender;
        this.login = login;
        this.name = name;
        this.location = location;
        this.email = email;
        this.dob = dob;
        this.registered = registered;
        this.phone = phone;
        this.pictureURL = pictureURL;
        this.nat = nat;
        this.description = description;
        this.rate = rate;
        this.price = price;
        this.reviews = reviews;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "User{" +
                "gender='" + gender + '\'' +
                ", login=" + login +
                ", name=" + name +
                ", location=" + location +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", registered='" + registered + '\'' +
                ", phone=" + phone +
                ", pictureURL='" + pictureURL + '\'' +
                ", nat='" + nat + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", price=" + price +
                ", reviews=" + reviews +
                '}';
    }
}
