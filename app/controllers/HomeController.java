package controllers;

import Utils.AppUtils;
import authenticators.AdminAuthenticator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.*;
import play.Configuration;
import play.Logger;
import play.cache.CacheApi;
import play.libs.Json;
import play.mvc.*;

import scala.concurrent.duration.Duration;
import scala.reflect.ClassTag;
import java.io.File;
import java.util.*;

import training.TestModule;
import training.Trial_class;
import play.Logger;
import views.html.*;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(AdminAuthenticator.class)
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    @Inject
    CacheApi cacheApi;

    @Inject
    Configuration configuration;

    private static CachedUserData mCachedUserData;

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public Result test(String from, String to) {
        CachedUserData cachedUserData = retrieveDataFromCache();
        if (cachedUserData == null) {
            return badRequest("data not found in the cache. Please train first");
        }

        Map<Long, User> testDatas = getDataFromCache(from, to);

        TestModule testModule = new TestModule();
        testModule.dataTesting(cachedUserData, testDatas);

        return ok("Done");
    }

    public Result train(String from, String to) {
        int features = 15;
        int totalDishes = MenuItemBundle.values().length;
        Logger.info("A log message");
        Trial_class trial = new Trial_class();
        Map<Long, User> userDatas = getDataFromCache(from, to);
        trial.dataArranging(userDatas);
        double params[] = trial.optimization();
        int priorityusercount = trial.user_data_count();


        double X[][] = new double[totalDishes][features];
        double theta[][] = new double[priorityusercount][features];
        int ia = 0;
        for (int i = 0; i < features; i++) {
            for (int j = 0; j < totalDishes; j++) {
                X[j][i] = params[ia];
                ia++;
            }
        }
        for (int i = 0; i < features; i++) {
            for (int j = 0; j < priorityusercount; j++) {
                theta[j][i] = params[ia];
                ia++;
            }
        }

        double ratings[][] = new double[priorityusercount][totalDishes];
        for(int i=0;i<priorityusercount;i++){
            for(int j=0;j<totalDishes;j++){
                for(int k=0;k<features;k++){
                    ratings[i][j] = ratings[i][j] + X[j][k]*theta[i][k];
                }
            }
        }

        //save calculated data to cache
        CachedUserData userData = new CachedUserData(ratings, trial.getPriorityUsers());
        saveDataToCache(userData);
        return ok(index.render("Your new application is ready."));
    }

    public static String key_training_data = "KEY_TRAINING_DATA:";
    public static String key_training_users = "KEY_TRAINING_USERS:";

    private void saveDataToCache(CachedUserData cachedUserData) {
        mCachedUserData = cachedUserData;
        if (true)return;
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode objectNode = objectMapper.valueToTree(cachedUserData.ratings);
        cacheApi.set(key_training_data, objectNode.toString());
        ArrayNode objectNode2 = objectMapper.valueToTree(cachedUserData.users);
        cacheApi.set(key_training_users, objectNode2.toString());
    }

    private CachedUserData retrieveDataFromCache() {
        if (true) return mCachedUserData;
        String dataStr = cacheApi.get(key_training_data);
        String userStr = cacheApi.get(key_training_users);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CachedUserData userData = new CachedUserData();
            userData.users = objectMapper.readValue(userStr , new TypeReference<List<Integer>>(){});
            userData.ratings = objectMapper.readValue(dataStr, new TypeReference<double[][]>(){});
            return userData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Result processTestCase() {
        Map<Long, User> completeData = new HashMap<>();
        OrderData orderData = new OrderData(21, 10, 5, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 10, 13, 3, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 10, 18, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 10, 12, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 11, 4, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 11, 5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 11, 11, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 11, 7, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 12, 19, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 12, 13, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 13, 18, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 13, 5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 14, 19, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 14, 11, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 15, 19, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 15, 18, 3, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 15, 5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 15, 7, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 16, 5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 16, 7, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 17, 9, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 17, 13, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 17, 18, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 18,9,1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 18, 14, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 19, 11, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 20, 18, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 20, 10, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(21, 20,5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 21, 5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 21, 13, 3, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 22, 5, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 22, 18, 4, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 22, 11, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 23,5, 3, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 24, 5, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 25, 19, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 26, 9, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 26, 14, 1, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 26, 18, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 26, 5, 2, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        orderData = new OrderData(22, 26, 13, 3, 150, 0, 5, 1);
        processOrderData(completeData, orderData);
        Trial_class trial = new Trial_class();
        trial.dataArranging(completeData);
        return ok("Done");
    }

    public Map<Long, User> getDataFromCache(String from, String to) {
        Date fromDate = AppUtils.getDate(from);
        Date toDate = AppUtils.getDate(to);

        Date currentDate = fromDate;

        if (fromDate.after(toDate)) {
            return null;
        }

        Map<Long, User> completeData = new HashMap<>();

        while (!currentDate.after(toDate)) {
            String currentDateStr = AppUtils.getDateStr(currentDate);
            String orderDataStr = cacheApi.get(FetchDataController.KEY_ORDER_DATA+currentDateStr);
            List<OrderData> orderDataList = FetchDataController.getOrderDataFromJson(orderDataStr);
            if (orderDataList != null && !orderDataList.isEmpty()) {
                for (OrderData orderData : orderDataList) {
                    processOrderData(completeData, orderData);
                }
            }
            currentDate = AppUtils.addToDate(currentDate, 24, 0);//next day
            Logger.debug("Data loaded for :"+currentDateStr+" : total = "+orderDataList.size());
        }
        return completeData;
    }

    private void processOrderData(Map<Long, User> users, OrderData orderData) {
        User user = users.get(orderData.user_id);
        if (user == null) {
            user = new User(orderData);
            users.put(user.user_id, user);
        } else {
            user.process(orderData);
        }

    }


}
