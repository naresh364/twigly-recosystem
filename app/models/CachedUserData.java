package models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.util.Pair;
import play.Logger;
import play.cache.CacheApi;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;

/**
 * Created by naresh on 30/08/16.
 */
public class CachedUserData {
    public Map<Long, List<Long>> useridVsMenuPrefMap;

    public CachedUserData(double ratings[][], List<User> users) {
        useridVsMenuPrefMap = new HashMap<>();
        for (int i=0; i < users.size(); i++) {

            List<Long> itemPreference = getSortedMenuItemList(ratings[i]);
            useridVsMenuPrefMap.put(users.get(i).user_id, itemPreference);
        }
    }

    public CachedUserData(){
    };

    public List<Long> getSortedMenuItemList(double[] userRating) {
        List<Pair<MenuItemBundle, Double>> bundleDoubleRating = new ArrayList<>();
        int j = 0;
        for (double rating : userRating) {
            bundleDoubleRating.add(new Pair<>(MenuItemBundle.values()[j], rating));
            j++;
        }
        Collections.sort(bundleDoubleRating, (a, b) -> ((a.getValue() - b.getValue() == 0 ? 0 :
                a.getValue() - b.getValue() > 0 ? -1 : 1)));

        List<MenuItemBundle> sortedMenuItemBundles = new ArrayList<>();
        List<Long> sortedMenuItems = new ArrayList<>(userRating.length*3);
        for (Pair<MenuItemBundle, Double> pair : bundleDoubleRating) {
            MenuItemBundle menuItemBundle = pair.getKey();
            sortedMenuItemBundles.add(menuItemBundle);
        }

        for (MenuItemBundle menuItemBundle:MenuItemBundle.values()) {
            float factor = menuItemBundle.getMinPositionFactor();

            if (factor == 0) continue;
            if (factor > 1) factor = 1;

            int minPosition = (int)(sortedMenuItemBundles.size()*factor);
            int currentPosition = sortedMenuItemBundles.indexOf(menuItemBundle);
            if (currentPosition < minPosition) {
                MenuItemBundle bundle = sortedMenuItemBundles.remove(currentPosition);
                sortedMenuItemBundles.add(minPosition, bundle);
            }
        }

        for (MenuItemBundle menuItemBundle : sortedMenuItemBundles) {
            sortedMenuItems.addAll(menuItemBundle.getMenu_item_ids());
        }

        return sortedMenuItems;
    }

    public static String key_training_data = "KEY_TRAINING_DATA:";

    public static void saveDataToCache(CacheApi cacheApi, CachedUserData cachedUserData) {
        for (Map.Entry<Long, List<Long>> entry : cachedUserData.useridVsMenuPrefMap.entrySet()) {
            Long userId = entry.getKey();
            List<Long> sortedMenuItems = entry.getValue();
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode objectNode = objectMapper.valueToTree(sortedMenuItems);
            System.out.println("Data saved to cache of :"+userId);
            try {
                cacheApi.set(key_training_data + userId, objectNode.toString());
            } catch (JedisConnectionException ex) {
                try {
                    Logger.debug("GOing to sleep for some time");
                    Thread.sleep(300l);//try again after some time
                } catch (Exception ex1) {
                    Logger.debug("not able to sleep");
                }
                cacheApi.set(key_training_data + userId, objectNode.toString());
            }
        }
    }

    public static List<Long> retrieveDataFromCache(CacheApi cacheApi, long userId) {
        String dataStr = cacheApi.get(key_training_data+userId);
        if (dataStr == null) return null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> menuItemIds = objectMapper.readValue(dataStr, new TypeReference<List<Long>>(){});
            if (menuItemIds == null) {
                System.out.println("retrieve failed data for :" + userId);
            } else {
                System.out.println("retrieved data for :" + userId);
            }
            return menuItemIds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
