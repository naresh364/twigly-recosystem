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

import redis.clients.jedis.JedisPool;
import scala.concurrent.duration.Duration;
import scala.reflect.ClassTag;
import java.io.File;
import java.util.*;

import training.Trial_class;
import training.TestModule;
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
        Map<Long, User> testDatas = getDataFromCache(from, to);

        TestModule testModule = new TestModule();
        String result = testModule.dataTesting(cacheApi, testDatas, mCachedUserData);

        return ok(result);
    }

    public Result train(String from, String to, int features) {
        if (features > 0) {
            User.features = features;
        } else {
            features = User.features;
        }
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

        //printMatrix(X);

        //save calculated data to cache
        CachedUserData userData = new CachedUserData(ratings, trial.getPriorityUsers());
        mCachedUserData = userData;
        CachedUserData.saveDataToCache(cacheApi, userData);
        //return test("2016-08-01", "2016-08-31");
        return ok(index.render("Your new application is ready."));
    }

    public Result save() {
        CachedUserData.saveDataToCache(cacheApi, mCachedUserData);
        return ok("Done");
    }

    private void printMatrix(double rating[][]) {
        for (int i = 0; i < rating.length; i++) {
            for (int j =0; j < rating[i].length; j++) {
                System.out.print(rating[i][j]+",");
            }
            System.out.println();
        }

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
            String orderDataStr = AppUtils.decompress(cacheApi.get(FetchDataController.KEY_ORDER_DATA+currentDateStr));
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
