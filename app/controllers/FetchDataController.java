package controllers;

import Utils.AppUtils;
import authenticators.AdminAuthenticator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import models.OrderData;
import play.Configuration;
import play.Logger;
import play.cache.CacheApi;
import play.libs.F;
import play.libs.ws.WSAPI;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by naresh on 28/08/16.
 */
@Security.Authenticated(AdminAuthenticator.class)
public class FetchDataController extends Controller {

    private static final String key_prod = "***REMOVED***";
    private static final String key_dev = "215db357-66f9-4578-b5d7-a87f8ef";
    private static final String userId = "analytics";

    @Inject
    CacheApi cacheApi;

    @Inject
    WSClient ws;

    @Inject
    Configuration configuration;

    public final String KEY_ORDER_DATA = "KEY_ORDER_DATA:";
    public final String host_prod = "https://twigly.in";
    public final String host_dev = "http://dev2.twigly.in";
    public final String fetch_url = "/extapi/analytics/data";


    public Result fetch(String from, String to) {
        Date fromDate = AppUtils.getDate(from);
        Date toDate = AppUtils.getDate(to);
        int env = configuration.getInt("server.id",0);
        String key = (env == 0)?key_prod : key_dev;
        String host = (env == 0)?host_prod:host_dev;
        host = host+fetch_url;

        Date currentDate = fromDate;

        if (fromDate.after(toDate)) {
            return badRequest("From should be before To");
        }

        while (!currentDate.after(toDate)) {
            String currentDateStr = AppUtils.getDateStr(currentDate);
            String orderDataStr = cacheApi.get(KEY_ORDER_DATA+currentDateStr);
            List<OrderData> orderDataList = getOrderDataFromJson(orderDataStr);
            if (orderDataList == null || orderDataList.isEmpty()) {
                //fetch from the server
                WSRequest wsRequest = ws.url(host+"?date="+currentDateStr);
                CompletionStage<JsonNode> promise = null;
                promise = wsRequest
                        .setContentType("application/x-www-form-urlencoded")
                        .setRequestTimeout(60*1000)
                        .setHeader("AuthKey", key)
                        .get().thenApply(WSResponse::asJson);

                CompletableFuture<JsonNode> completableFuture = promise.toCompletableFuture();
                try {
                    JsonNode result = completableFuture.get().get("data");
                    Logger.debug("result =" + result.toString());
                    cacheApi.set(KEY_ORDER_DATA+currentDateStr, result.toString());
                    orderDataList = getOrderDataFromJson(result.toString());
                    Logger.debug("test ="+orderDataList.size());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return badRequest("Unable to parse data");
                }
            }
            currentDate = AppUtils.addToDate(currentDate, 24, 0);//next day
        }
        return ok("Done");

    }

    public Result clear(String from, String to) {
        Date fromDate = AppUtils.getDate(from);
        Date toDate = AppUtils.getDate(to);
        int env = configuration.getInt("server.id",0);
        String key = (env == 0)?key_prod : key_dev;
        String host = (env == 0)?host_prod:host_dev;
        host = host+fetch_url;

        Date currentDate = fromDate;

        if (fromDate.after(toDate)) {
            return badRequest("From should be before To");
        }

        while (!currentDate.after(toDate)) {
            String currentDateStr = AppUtils.getDateStr(currentDate);
            cacheApi.remove(KEY_ORDER_DATA+currentDateStr);
            currentDate = AppUtils.addToDate(currentDate, 24, 0);//next day
        }
        return ok("Done");
    }

    private List<OrderData> getOrderDataFromJson(String nodeStr) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<OrderData> dataList = objectMapper.readValue(nodeStr, new TypeReference<List<OrderData>>(){});
            return dataList;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private class Data {
        public List<OrderData> data;
        public Data(){};
    }

}
