package dalker.cmtruong.com.app.model;


/**
 * Address model
 *
 * @author davidetruong
 * @version 1.0
 * @since July 30th, 2018
 */
public class Address {

    private int num;

    private String street;

    private int codePostal;

    private String commun;

    private String country;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getCommun() {
        return commun;
    }

    public void setCommun(String commun) {
        this.commun = commun;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "num=" + num +
                ", street='" + street + '\'' +
                ", codePostal=" + codePostal +
                ", commun='" + commun + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
