package training;

import models.MenuItemBundle;
import models.User;
import models.UserParams;

import java.util.*;
import java.lang.*;

public class Trial_class {
    int len = 50000;
    int totalDishes = 17;
    int orderThreshold = 5;
    int orderPriority = 10;
    int Y[][];
    int R[][];
    List<User> priorityUsers;
    double meanRating[];
    int features = 15;
    int lambda = 11;
    //int lambda = 0;
    double alpha = 0.001;
    double error = 0.01;
    // For vector training
    //double X[][];
    //double theta[][];
    int numberOfIterations = 100;

    public double[] optimization(){
        double rho = 0.01;
        double sig = 0.5;
        double internal = 0.1;
        double external = 3.0;
        double MAX = 20;
        double RATIO = 100;
        int red = 1;

        System.out.println(" Inside the optimization function");

        // Getting initial values of X and theta
        double X_1[] = parameterInitialization();
        double f1 = costFunction(X_1);
        System.out.println("Cost is "+f1);
        double df1[] = gradFunction(X_1);
        double i=0;
        double ls_failed = 0;
        int l = df1.length;
        double s[] = new double[l];
        for(int j=0;j<l;j++){
            s[j] = -1*df1[j];
        }
        double d1 = 0;
        for(int j=0;j<l;j++){
            d1 = d1 + s[j]*s[j];
        }
        d1 = -1*d1;
        double z1 = 1/(1-d1);
        double X0[];
        double f0;
        double df0[];
        double d2 = 0;
        double f2 = 0;
        double df2[];
        Double z2= new Double(0.0);
        double f3;
        double d3;
        double z3;
        double M=0;
        while (i<numberOfIterations){
            i = i+1;
            X0 = X_1;
            f0 = f1;
            df0 = df1;
            //System.out.println("i = "+i);
            for(int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                X_1[j] = X_1[j] + z1*s[j];
            }

            //System.out.println("cost_1 = "+f2 + " and z1 "+z1);
            f2 = costFunction(X_1);
            //System.out.println("cost = "+f2);
            df2 = gradFunction(X_1);
            d2 = 0;
            for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                d2 = d2 + df2[j]*s[j];
            }
            f3 = f1;
            d3 = d1;
            z3 = -1*z1;
            M = MAX;
            double success = 0;
            double limit = -1;
            double A = 0;
            double B = 0;
            z2 = 0.0;
            while(true){
                double temp = f1 + z1*rho*d1;
                // System.out.println("value of temp "+ temp+" f2 "+f2 + " f1 "+f1+"z1 :"+z1 + "d1 "+d1 + " M "+M);
                while(((f2>f1+z1*rho*d1)||(d2> -1*sig*d1)) && (M>0)){
                    limit = z1;
                    if(f2>f1)
                        z2 = z3 - (0.5*d3*z3*z3)/(d3*z3+f2-f3);
                    else{
                        A = 6*(f2-f3)/z3 + 3*(d2+d3);
                        B = 3*(f3-f2)-z3*(d3+2*d2);
                        z2 = (Math.sqrt(B*B-A*d2*z3*z3)-B)/A;
                    }
                    if (z2.isNaN()||z2.isInfinite())
                        z2 = z3/2;
                    z2 = Math.max(Math.min(z2, internal*z3),(1-internal)*z3);
                    z1 = z1+z2;
                    for(int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                        X_1[j] = X_1[j] + z2*s[j];
                    }
                    f2 = costFunction(X_1);
                    df2 = gradFunction(X_1);
                    M = M-1;
                    d2 = 0;
                    for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                        d2 = d2 + df2[j]*s[j];
                    }
                    z3 = z3-z2;
                }
                if(f2>f1+z1*rho*d1 ||d2>-1*sig*d1){
                    //System.out.println("break in 1");
                    break;
                }
                else if(d2>sig*d1){
                    //System.out.println("break in 2");
                    success=1;
                    break;
                }
                else if(M==0){
                    //System.out.println("break in 3");
                    break;
                }
                A = 6*(f2-f3)/z3+3*(d2+d3);
                B = 3*(f3-f2)-z3*(d3+2*d2);
                z2 = -d2*z3*z3/(B+Math.sqrt(B*B-A*d2*z3*z3));
                if (z2.isNaN()||z2.isInfinite()||z2<0){
                    if(limit<-0.5)
                        z2 = z1 *(external-1);
                    else
                        z2 = (limit-z1)/2;
                }
                else if((limit>-0.5)&&(z1+z2>limit))
                    z2 = (limit-z1)/2;
                else if((limit<-0.5)&&(z1+z2>z1*external))
                    z2 = z1*(external-1.0);
                else if(z2<-z3*internal)
                    z2 = -z3*internal;
                else if((limit >-0.5)&&(z2<(limit-z1)*(1.0-internal)))
                    z2 = (limit-z1)*(1.0-internal);
                f3=f2;
                d3=d2;
                z3 = -z2;
                z1 = z1+z2;
                for(int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    X_1[j] = X_1[j] + z2*s[j];
                }
                f2 = costFunction(X_1);
                df2 = gradFunction(X_1);
                M = M-1;
                d2 = 0;
                for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    d2 = d2 + df2[j]*s[j];
                }
            }
            if(success>0){
                //System.out.println("For iteration "+i+": Cost function "+f1);
                double tot = 0;
                double tot1 = 0;
                f1 = f2;
                for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    tot = tot + (df2[j]-df1[j])*df2[j];
                    tot1 = tot1+df1[j]*df1[j];
                }
                tot = tot/tot1;
                for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    s[j] = tot*s[j] - df2[j];
                }
                double tmp[] = df1;
                df1 = df2;
                df2 = tmp;
                d2 = 0;
                for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    d2 = d2 + df1[j]*s[j];
                }
                if(d2>0){
                    for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                        s[j] = -1*df1[j];
                    }
                    d2 = 0;
                    for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                        d2 = d2 + s[j]*s[j];
                    }
                    d2 = -1*d2;
                }
                z1 = z1*Math.min(RATIO, d1/d2);
                d1=d2;
                ls_failed = 0;
                //System.out.println("Success");
            }
            else{
                //System.out.println("Failed");
                X_1 = X0;
                f1 = f0;
                df1 = df0;
                if((ls_failed>0)||(i>numberOfIterations))
                    break;
                double tmp[] = df1;
                df1 = df2;
                df2 = tmp;
                for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    s[j] = -1*df1[j];
                }
                d1 = 0;
                for (int j=0;j<(totalDishes+priorityUsers.size())*features;j++){
                    d1 = d1 + s[j]*s[j];
                }
                d1 = -1*d1;
                z1 = 1/(1-d1);
                ls_failed = 1;
            }
        }
        f1 = costFunction(X_1);
        System.out.println("final Cost is "+f1);
        return X_1;
    }

    public void dataArranging(Map<Long, User> userData){
        //	public void dataArranging(int[][] order, int orderNum){
        int temp = 0;

        priorityUsers = new ArrayList<>();

        for (Map.Entry<Long, User> entry : userData.entrySet()) {
            long orderCount = entry.getValue().orders.size();
            if (orderCount >= orderThreshold) {
                priorityUsers.add(entry.getValue());
            }
        }

        int totalBundleCount = MenuItemBundle.values().length;
        Y = new int[priorityUsers.size()][totalBundleCount];
        R = new int[priorityUsers.size()][totalBundleCount];
        int maxvalue = 0;
        int i=0;
        for (User user : priorityUsers) {
            maxvalue = user.getMaxDishCount();
            int j = 0;
            for (MenuItemBundle bundle : MenuItemBundle.values()) {
                UserParams userParams = user.itemBundleCountMap.get(bundle);
                Y[i][j] = (userParams == null ? 1 : ((9*userParams.count)/maxvalue)+1);
                int tempo = (userParams == null)?0:userParams.count;
                R[i][j] = (user.orders.size() > orderPriority)?1: (tempo > 0 ? 1 : 0);
                j++;
            }
            i++;
            System.out.println("User order size is "+user.orders.size());
        }

        System.out.println("The value of order frequency is ");
        i=0;
        for (User user : priorityUsers) {
            int j = 0;
            for (MenuItemBundle bundle : MenuItemBundle.values()) {
                UserParams userParams = user.itemBundleCountMap.get(bundle);
                Integer c1 = (userParams == null) ? 0 : userParams.count;
                System.out.print(c1+"   ");
                j++;
            }
            System.out.println();
            i++;
        }

        System.out.println("The value of Y[i][j] is ");
        i=0;
        for (User user : priorityUsers) {
            int j = 0;
            for (MenuItemBundle bundle : MenuItemBundle.values()) {
                System.out.print(Y[i][j]+"   ");
                j++;
            }
            System.out.println();
            i++;
        }
        System.out.println("The value of R[i][j] is ");
        i=0;
        for (User user : priorityUsers) {
            int j = 0;
            for (MenuItemBundle bundle : MenuItemBundle.values()) {
                System.out.print(R[i][j]+"   ");
                j++;
            }
            System.out.println();
            i++;
        }
    }

    public int user_data_count(){
        return priorityUsers.size();
    }

    public void meanRating(){
        // Finding the normalize ratings for the dataset
	/* NORMALIZERATINGS Preprocess data by subtracting mean rating for every
       movie (every row)
       [Ynorm, Ymean] = NORMALIZERATINGS(Y, R) normalized Y so that each movie
       has a rating of 0 on average, and returns the mean rating in Ymean.
	   */
        meanRating = new double[priorityUsers.size()];
        double total=0;
        for (int i=0;i<priorityUsers.size();i++){
            total = 0;
            for(int j=0;j<totalDishes;j++){
                total = total+Y[i][j];
            }
            meanRating[i] = total/totalDishes;
        }
    }

    public double[] parameterInitialization(){
        // Here we define the two parameters used for training:
		/* X : used to train dishes (dishes * features)
		 * theta : used to train user parameters (users * features)
		 * */
        double X[] = new double[(totalDishes+priorityUsers.size())*features];

        // Assigning random values to X and theta
        for (int i=0;i<(totalDishes+priorityUsers.size())*features;i++){
            X[i] = Math.random();
        }
        return X;
		/*  Check for parameter initialization

		for (int i=0;i<priorityUsers.size();i++){
			for(int j=0;j<features;j++){
				System.out.print(theta[i][j] + "   ");
			}
			System.out.println();
		}*/
    }

    public double costFunction(double x[]){
        double costFunction = 0;
        // X_grad : Gradient for X function
        // theta_grad : Gradient for theta function

        double X_11[][] = new double[totalDishes][features];
        double theta_11[][] = new double[priorityUsers.size()][features];

        // Assigning the values to X_11 and theta_11
        int ia=0;
        for(int k=0;k<features;k++){
            for (int j=0;j<totalDishes;j++){
                X_11[j][k] = x[ia];
                ia++;
            }
        }
        for(int k=0;k<features;k++){
            for (int j=0;j<priorityUsers.size();j++){
                theta_11[j][k] = x[ia];
                ia++;
            }
        }
		/*
		// Printing the values of X and theta
		System.out.println("Printing the value of X");
		for(int k=0;k<totalDishes;k++){
			for (int j=0;j<features;j++){
				System.out.print(X_11[k][j]  + "   ");
				//X_11[k][j] = x[k*features + j];
			}
			System.out.println();
		}
		System.out.println("Printing the value of Theta");
		for(int k=0;k<priorityUsers.size();k++){
			for (int j=0;j<features;j++){
				System.out.print(theta_11[k][j]  + "   ");
				//theta_11[k][j] = x[(k+totalDishes)*features + j];
			}
			System.out.println();
		}
		*/

        // Computing cost function
        double beta = 0;
        for(int i=0;i<priorityUsers.size();i++){
            for(int j=0;j<totalDishes;j++){
                beta = 0;
                for(int k=0;k<features;k++){
                    beta = beta+ theta_11[i][k]*X_11[j][k];
                }
                beta = beta - Y[i][j];
                beta = Math.pow(beta, 2);
                costFunction = costFunction + beta*R[i][j];
            }
        }
        beta = 0;
        for(int i=0;i<priorityUsers.size();i++){
            for(int j=0;j<features;j++){
                beta = beta + Math.pow(theta_11[i][j],2);
            }
        }
        for(int i=0;i<totalDishes;i++){
            for(int j=0;j<features;j++){
                beta = beta + Math.pow(X_11[i][j],2);
            }
        }
        beta = lambda*beta;
        costFunction = costFunction+beta;
        costFunction = costFunction/2;
        return costFunction;
    }

    public double[] gradFunction(double x[]){
        double X_grad[][] = new double[totalDishes][features];
        double theta_grad[][] = new double[priorityUsers.size()][features];
        double cost_grad[][] = new double[priorityUsers.size()][totalDishes];
        double final_var[] = new double[(totalDishes+priorityUsers.size())*features];
        double beta = 0;

        double X_11[][] = new double[totalDishes][features];
        double theta_11[][] = new double[priorityUsers.size()][features];

        // Assigning the values to X_11 and theta_11
        int ia = 0;
        for(int k=0;k<features;k++){
            for (int j=0;j<totalDishes;j++){
                X_11[j][k] = x[ia];
                ia++;
            }
        }

        for(int k=0;k<features;k++){
            for (int j=0;j<priorityUsers.size();j++){
                theta_11[j][k] = x[ia];
                ia++;
            }
        }

        // computing the gradient
        for(int i=0;i<priorityUsers.size();i++){
            for(int j=0;j<totalDishes;j++){
                beta = 0;
                for(int k=0;k<features;k++){
                    beta = beta+ theta_11[i][k]*X_11[j][k];
                }
                beta = beta - Y[i][j];
                cost_grad[i][j] = beta*R[i][j];
            }
        }
        // differential definition
        // defining for theta_grad
        for(int i=0;i<priorityUsers.size();i++){
            for(int j=0;j<features;j++){
                beta = 0;
                for(int k=0;k<totalDishes;k++){
                    beta = beta+cost_grad[i][k]*X_11[k][j];
                }
                theta_grad[i][j] = beta+lambda*theta_11[i][j];
            }
        }

        // defining for X_grad
        for(int i=0;i<totalDishes;i++){
            for(int j=0;j<features;j++){
                beta = 0;
                for(int k=0;k<priorityUsers.size();k++){
                    beta = beta+cost_grad[k][i]*theta_11[k][j];
                }
                X_grad[i][j] = beta+lambda*X_11[i][j];
            }
        }
        int ia1 = 0;
        for(int i=0;i<features;i++){
            for (int j=0;j<totalDishes;j++){
                final_var[ia1] = X_grad[j][i];
                ia1++;
            }
        }
        for(int i=0;i<features;i++){
            for (int j=0;j<priorityUsers.size();j++){
                int temp = (i+totalDishes)*features + j;
                //System.out.println("Users = "+priorityUsers.size()+"  features = "+features+" Total dishes = "+totalDishes+"  temp"+temp);
                final_var[ia1] = theta_grad[j][i];
                ia1++;
            }
        }
        return final_var;
    }

    public List<User> getPriorityUsers() {
        return priorityUsers;
    }
}
