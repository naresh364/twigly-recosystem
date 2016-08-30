package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naresh on 30/08/16.
 */
public class CachedUserData {
    public double ratings[][];
    public List<Long> users;

    public CachedUserData(double ratings[][], List<User> users) {
        this.ratings = ratings;
        this.users = new ArrayList<>(users.size());
        for (User user : users) {
            this.users.add(user.user_id);
        }
    }

    public CachedUserData(){ };

    public double[][] getRatings() {
        return ratings;
    }

    public void setRatings(double[][] ratings) {
        this.ratings = ratings;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

}
