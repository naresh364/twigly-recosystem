package controllers;

import Utils.AppUtils;
import authenticators.AdminAuthenticator;
import com.google.inject.Inject;
import models.OrderData;
import play.cache.CacheApi;
import play.libs.ws.WSAPI;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Date;
import java.util.List;

/**
 * Created by naresh on 28/08/16.
 */
@Security.Authenticated(AdminAuthenticator.class)
public class FetchDataController extends Controller {

    @Inject
    CacheApi cacheApi;

    @Inject
    WSClient ws;

    public final static String KEY_ORDER_DATA = "KEY_ORDER_DATA:";

    public Result fetchData(String from, String to) {
        Date fromDate = AppUtils.getDate(from);
        Date toDate = AppUtils.getDate(to);

        Date currentDate = fromDate;

        if (fromDate.after(toDate)) {
            return badRequest("From should be before To");
        }

        while (currentDate.before(toDate)) {
            List<OrderData> orderDatas = cacheApi.get(KEY_ORDER_DATA+from);


            AppUtils.addToDate(currentDate, 24, 0);//next day
        }
        return ok();

    }

}
