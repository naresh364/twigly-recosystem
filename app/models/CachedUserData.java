package models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import play.Logger;
import play.cache.CacheApi;
import redis.clients.jedis.exceptions.JedisConnectionException;

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

    public CachedUserData(){
    };

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

    public static String key_training_data = "KEY_TRAINING_DATA:";

    public static void saveDataToCache(CacheApi cacheApi, CachedUserData cachedUserData) {
        for (int i =0; i < cachedUserData.getUsers().size();i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode objectNode = objectMapper.valueToTree(cachedUserData.ratings[i]);
            System.out.println("Data saved to cache of :"+cachedUserData.getUsers().get(i));
            try {
                cacheApi.set(key_training_data + cachedUserData.getUsers().get(i), objectNode.toString());
            } catch (JedisConnectionException ex) {
                try {
                    Logger.debug("GOing to sleep for some time");
                    Thread.sleep(300l);//try again after some time
                } catch (Exception ex1) {
                    Logger.debug("not able to sleep");
                }
                cacheApi.set(key_training_data + cachedUserData.getUsers().get(i), objectNode.toString());
            }
        }
    }

    public static double[] retrieveDataFromCache(CacheApi cacheApi, long userId) {
        String dataStr = cacheApi.get(key_training_data+userId);
        if (dataStr == null) return null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            double[] rating = objectMapper.readValue(dataStr, new TypeReference<double[]>(){});
            if (rating == null) {
                System.out.println("retrieve failed data for :" + userId);
            } else {
                System.out.println("retrieved data for :" + userId);
            }
            return rating;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
