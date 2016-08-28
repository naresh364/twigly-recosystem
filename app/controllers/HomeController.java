package controllers;

import authenticators.AdminAuthenticator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import play.Logger;
import play.cache.CacheApi;
import play.libs.Json;
import play.mvc.*;

import scala.concurrent.duration.Duration;
import scala.reflect.ClassTag;
import views.html.*;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    @Inject
    CacheApi cacheApi;

    @Security.Authenticated(AdminAuthenticator.class)
    public Result index() {
        Test test = new Test("naresh", "kumar");
        ObjectNode node = Json.newObject();
        node.putPOJO("node", Json.toJson(test));
        String nodeStr = node.toString();
        try {
            cacheApi.set("Naresh", nodeStr);
        } catch (Exception ex) {
            Logger.debug("Unable to set cache");
        }

        String val = cacheApi.get("Naresh");
        Logger.debug("Val = "+val);
        return ok(index.render("Your new application is ready."));
    }

    public class Test implements Serializable {
        public String name;
        public String value;
        public Test(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }


}
