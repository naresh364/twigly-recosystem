package authenticators;

import com.google.inject.Inject;
import play.Logger;
import play.cache.CacheApi;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

/**
 * Created by naresh on 28/08/16.
 */
public class AdminAuthenticator extends Security.Authenticator {
    public static final String ADMIN_AUTH_TOKEN = "adminAuthToken";

    private CacheApi cacheApi;

    @Inject
    public AdminAuthenticator(CacheApi cacheApi) {
        super();
        this.cacheApi = cacheApi;
    }

    @Override
    public String getUsername(Http.Context ctx) {
        String authToken = ctx.session().get(ADMIN_AUTH_TOKEN);
        Logger.info("AuthToken: " + authToken);
        if(authToken!=null && authToken.trim().length()>0) {
            String user = cacheApi.get(AdminAuthenticator.ADMIN_AUTH_TOKEN+authToken);
            Logger.debug("val ="+user);
            ctx.args.put("adminUser", user);
            return user;
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        ctx.flash().put("error", "Please Login to continue");
        return badRequest("Error");
        //return Results.redirect(routes.AdminLoginController.index());
    }
}
