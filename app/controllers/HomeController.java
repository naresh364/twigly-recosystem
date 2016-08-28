package controllers;

import play.mvc.*;

import java.io.File;
import java.util.Scanner;
import training.Trial_class;
import play.Logger;
import views.html.*;

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
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result train() {
        int len = 50000;
        int features = 15;
        int totalDishes = 17;
        Logger.info("A log message");
        try {
            File f = new File("orders_for_java.txt");
            //File f = new File("abc.txt");
            Scanner sc = new Scanner(f);
            int orders[][] = new int[len][3];
            int count_orders = 0;
            int temp = 0; // Temp variable for checking end of file
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < 3; j++) {
                    if (sc.hasNextLine() == false) {
                        temp = 1;
                        break;
                    }
                    orders[i][j] = sc.nextInt();
                }
                if (temp == 1)
                    break;
                count_orders = count_orders + 1;
            }
            // System.out.println(count_orders);
            sc.close();

            /***************************************************************************
             * Initialization of the variable containing the entries of orders_details.xlsx file
             * 1st column - order detail id
             * 2nd column - order id
             * 3rd column - menu item id
             * 4th column - quantity
             * 5th column - price
             * 6th column - discount
             ****************************************************************************/
            File f1 = new File("order_details.txt");
            Scanner sc1 = new Scanner(f1);
            int order_details[][] = new int[len][6];
            int count_order_details = 0;
            int temp1 = 0; // Temp variable for checking end of file
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < 6; j++) {
                    if (sc1.hasNextLine() == false) {
                        temp1 = 1;
                        break;
                    }
                    order_details[i][j] = sc1.nextInt();
                }
                if (temp1 == 1)
                    break;
                count_order_details = count_order_details + 1;
            }
            //System.out.println(count_order_details);
            sc1.close();
            Trial_class trial = new Trial_class();
            trial.dataArranging(orders, order_details, count_orders, count_order_details);
            double params[] = trial.optimization();
            double priority_user[][] = trial.user_data();
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
            for (int i = 0; i < 100; i++) {
                System.out.println("User id is "+priority_user[i][0] +"and orders "+priority_user[i][1]);
            }
        }
        catch (Exception e){
            // do stuff here..
            Logger.info("File not found");
        }

        return ok(index.render("Your new application is ready."));
    }

}
