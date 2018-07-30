package dalker.cmtruong.com.app.model;

/**
 * Main model of dalker
 *
 * @author davidetruong
 * @version 1.0
 * @since JUly 25th, 2018
 */
public class User {

    private String id;

    private String login;

    private String password;

    private Profile profile;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", profile=" + profile +
                '}';
    }
}
