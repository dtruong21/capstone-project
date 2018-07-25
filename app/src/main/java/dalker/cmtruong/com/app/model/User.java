package dalker.cmtruong.com.app.model;

/**
 * Main model of dalker
 *
 * @author davidetruong
 * @version 1.0
 * @since JUly 25th, 2018
 */
public class User {

    private long id;
    private String login;
    private String password;
    private String name;
    private String gender;
    private String dateOfBirth;
    private String address;
    private int telephone;
    private boolean isDalker;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public boolean isDalker() {
        return isDalker;
    }

    public void setDalker(boolean dalker) {
        isDalker = dalker;
    }
}
