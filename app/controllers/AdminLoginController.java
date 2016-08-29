package controllers;

import Utils.AppUtils;
import authenticators.AdminAuthenticator;
import com.google.inject.Inject;
import play.Logger;
import play.cache.CacheApi;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.UUID;

/**
 * Created by naresh on 28/08/16.
 */
public class AdminLoginController extends Controller {

    @Inject
    CacheApi cacheApi;

    public Result signUp() {
        String email = "admin@twigly.in";
        String password = "***REMOVED***";
        try {
            cacheApi.set(email, AppUtils.md5(email + password));
            return ok("credentials created");
        } catch (Exception ex) {
            ex.printStackTrace();
            return badRequest("Unable to signup");
        }
    }

    public Result index() {
        Form<AdminLogin> loginForm = Form.form(AdminLogin.class);
        return ok(adminlogin.render(loginForm));
    }

    public Result login() {
        Form<AdminLogin> loginForm = Form.form(AdminLogin.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(adminlogin.render(loginForm));
        }

        AdminLogin login = loginForm.get();

        String user = cacheApi.get(login.emailAddress);

        if (user == null) {
            //AdminUser.addNewUser("admin@twigly.in", "admin", "Admin", AdminRole.ADMIN);
            flash("error", "Invalid username or password");
            return redirect(routes.AdminLoginController.index());
        } else {
            try {
                String verifier = AppUtils.md5(login.emailAddress + login.password);
                if (verifier != null && verifier.equals(user)) {
                    String authToken = UUID.randomUUID().toString().substring(0, 31);
                    session(AdminAuthenticator.ADMIN_AUTH_TOKEN, authToken);
                    cacheApi.set(AdminAuthenticator.ADMIN_AUTH_TOKEN+authToken, login.emailAddress);
                    return ok("Success");
                } else {
                    flash("error", "Invalid username or password");
                    return redirect(routes.AdminLoginController.index());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return badRequest("Failure");
            }
        }
    }


    /*
        The below order of annotation is important and should not be changed else an exception occurs
        see https://github.com/playframework/playframework/issues/4361 for more info
     */
    @Security.Authenticated(AdminAuthenticator.class)
    public Result logout() {
        Logger.info("Trying log out");
        session().remove(AdminAuthenticator.ADMIN_AUTH_TOKEN);
        //TODO: different admin users
        //getAdminUser().deleteAuthToken();
        flash("error", "Logged out successfully");
        return redirect(routes.AdminLoginController.index());
    }

    public static class AdminLogin {

        @Constraints.Required
        @Constraints.Email (message = "Please enter a valid email")
        public String emailAddress;

        @Constraints.Required (message = "Please enter a valid password")
        public String password;

    }


}
