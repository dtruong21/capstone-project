package dalker.cmtruong.com.app.model;

public class Review {

    private int id;

    private int rate;

    private String comment;

    public Review(int rate, String comment) {
        this.rate = rate;
        this.comment = comment;
    }

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
        return "Review{" +
                "id=" + id +
                ", rate=" + rate +
                ", comment='" + comment + '\'' +
                '}';
    }
}
