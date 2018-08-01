package dalker.cmtruong.com.app.model;

public class Dob {

    private String date;

    private int age;

    public Dob(String date, int age) {
        this.date = date;
        this.age = age;
    }

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
        return "Dob{" +
                "date='" + date + '\'' +
                ", age=" + age +
                '}';
    }
}
