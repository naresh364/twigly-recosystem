package models;

/**
 * Created by naresh on 30/08/16.
 */

import java.lang.*;
public class UserParams {
    public int count = 0;
    public int frequency = 0;
    public float averageRating = 0;
    public long lastOrderTime = 0;
    public double meanRating = 5;
    public double stdDev = 1;

    public UserParams(int count, float rating, long lastOrderTime) {
        this.count = count;
        if (rating==0)
            rating = 5;
        if (rating > 0) {
            this.frequency = 1;
            rating = (float)Math.exp(-1*(Math.pow(rating-meanRating,2))/(2*Math.pow(stdDev,2)));
            this.averageRating = rating;
        }
        this.lastOrderTime = lastOrderTime;
    }

    public void update(int count, float averageRating, long lastOrderTime) {
        if (averageRating > 0) {
            averageRating = (float)Math.exp(-1*(Math.pow(averageRating-meanRating,2))/(2*Math.pow(stdDev,2)));
            this.averageRating = (this.averageRating * frequency + averageRating) / (frequency + 1);
            this.frequency++;
        }
        this.count += count;
        if (this.lastOrderTime < lastOrderTime) this.lastOrderTime = lastOrderTime;
    }
}
