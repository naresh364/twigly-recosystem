package models;

/**
 * Created by naresh on 30/08/16.
 */
public class UserParams {
    public int count = 0;
    public int frequency = 0;
    public float averageRating = 0;
    public long lastOrderTime = 0;

    public UserParams(int count, float rating, long lastOrderTime) {
        this.count = count;
        if (rating > 0) {
            this.frequency = 1;
            this.averageRating = rating;
        }
        this.lastOrderTime = lastOrderTime;
    }

    public void update(int count, float averageRating, long lastOrderTime) {
        if (averageRating > 0) {
            this.averageRating = (this.averageRating * frequency + averageRating) / (frequency + 1);
            this.frequency++;
        }
        this.count += count;
        if (this.lastOrderTime < lastOrderTime) this.lastOrderTime = lastOrderTime;
    }
}
