package training;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.lang.*;

public class Trial_class {
    int len = 50000;
    int totalDishes = 17;
    int orderThreshold = 5;
    int orderPriority = 10;
    int Y[][];
    int R[][];
    double priorityuser[][];
    int priorityusercount;
    int user[][];
    int usercount;
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
            for(int j=0;j<(totalDishes+priorityusercount)*features;j++){
                X_1[j] = X_1[j] + z1*s[j];
            }

            //System.out.println("cost_1 = "+f2 + " and z1 "+z1);
            f2 = costFunction(X_1);
            //System.out.println("cost = "+f2);
            df2 = gradFunction(X_1);
            d2 = 0;
            for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
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
                    for(int j=0;j<(totalDishes+priorityusercount)*features;j++){
                        X_1[j] = X_1[j] + z2*s[j];
                    }
                    f2 = costFunction(X_1);
                    df2 = gradFunction(X_1);
                    M = M-1;
                    d2 = 0;
                    for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
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
                for(int j=0;j<(totalDishes+priorityusercount)*features;j++){
                    X_1[j] = X_1[j] + z2*s[j];
                }
                f2 = costFunction(X_1);
                df2 = gradFunction(X_1);
                M = M-1;
                d2 = 0;
                for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
                    d2 = d2 + df2[j]*s[j];
                }
            }
            if(success>0){
                //System.out.println("For iteration "+i+": Cost function "+f1);
                double tot = 0;
                double tot1 = 0;
                f1 = f2;
                for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
                    tot = tot + (df2[j]-df1[j])*df2[j];
                    tot1 = tot1+df1[j]*df1[j];
                }
                tot = tot/tot1;
                for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
                    s[j] = tot*s[j] - df2[j];
                }
                double tmp[] = df1;
                df1 = df2;
                df2 = tmp;
                d2 = 0;
                for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
                    d2 = d2 + df1[j]*s[j];
                }
                if(d2>0){
                    for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
                        s[j] = -1*df1[j];
                    }
                    d2 = 0;
                    for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
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
                for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
                    s[j] = -1*df1[j];
                }
                d1 = 0;
                for (int j=0;j<(totalDishes+priorityusercount)*features;j++){
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

    public void dataArranging(int[][] order,int[][] orderdetail, int orderNum,int detailNum){
        //	public void dataArranging(int[][] order, int orderNum){
        int users = 0;
        int user_id[][] = new int[len][2]; // col1 for user id, col2 for number of orders
        int temp = 0;

        /********************************************************************************
         Block for finding the unique users and total order placed for order list
         *********************************************************************************/
        for(int i=0;i<orderNum;i++){
            for(int j=0;j<users;j++){
                if(user_id[j][0] == order[i][1]){ // as order[][1] contains user order id
                    temp = 1;
                    user_id[j][1] = user_id[j][1] + 1;
                    break;
                }
            }
            if(temp == 0){
                user_id[users][0] = order[i][1];
                user_id[users][1] = 1;
                users++;
            }
            temp = 0;
        }

        //Copying data from local variable to a class variable
        user = new int[users][2];
        usercount = users;
        for(int i=0;i<users;i++){
            user[i] = user_id[i];
        }

        // Removing free products from order details file
        int neworderdetail[][] = new int[len][6];
        int detailcount=0;
        for (int i=0;i<detailNum;i++){
            if(orderdetail[i][4] != orderdetail[i][5]){
                neworderdetail[detailcount] = orderdetail[i];
                detailcount++;
            }
        }

        // Adding user id with every order detail
        int user_id_order[] = new int[detailcount];
        for (int i=0;i<detailcount;i++){
            for (int j=0;j<orderNum;j++){
                if(order[j][0] == neworderdetail[i][1]){
                    user_id_order[i] = order[j][1];
                    break;
                }
            }
        }

        // For every customer, fill the number of every items eaten by the user
        // taking 17 items whose details are given in menu_items.txt (where in the brackets, the items are clubbed)
        int userstats[][] = new int[users][totalDishes];
        for (int i=0;i<users;i++){//assigning zero values to the above variable
            for(int j=0;j<totalDishes;j++){
                userstats[i][j] = 0;
            }
        }
        for (int i=0;i<detailcount;i++){
            for(int j=0;j<users;j++){
                if(user_id_order[i]==user_id[j][0]){
                    switch (neworderdetail[i][2]){
                        case 5:userstats[j][0] = userstats[j][0] + neworderdetail[i][3];
                            break;
                        case 7:userstats[j][1] = userstats[j][1] + neworderdetail[i][3];
                            break;
                        case 9:userstats[j][2] = userstats[j][2] + neworderdetail[i][3];
                            break;
                        case 10:
                        case 13:
                        case 18:userstats[j][3] = userstats[j][3] + neworderdetail[i][3];
                            break;
                        case 11:
                        case 14:
                        case 19:userstats[j][4] = userstats[j][4] + neworderdetail[i][3];
                            break;
                        case 30:userstats[j][5] = userstats[j][5] + neworderdetail[i][3];
                            break;
                        case 35:userstats[j][6] = userstats[j][6] + neworderdetail[i][3];
                            break;
                        case 39:
                        case 131:
                        case 148:userstats[j][7] = userstats[j][7] + neworderdetail[i][3];
                            break;
                        case 49:
                        case 107:
                        case 112:
                        case 120:userstats[j][8] = userstats[j][8] + neworderdetail[i][3];
                            break;
                        case 53:
                        case 54:
                        case 143:
                        case 144:
                        case 145:userstats[j][9] = userstats[j][9] + neworderdetail[i][3];
                            break;
                        case 55:userstats[j][10] = userstats[j][10] + neworderdetail[i][3];
                            break;
                        case 50:
                        case 58:
                        case 138:
                        case 182:userstats[j][11] = userstats[j][11] + neworderdetail[i][3];
                            break;
                        case 61:
                        case 129:
                        case 147:userstats[j][12] = userstats[j][12] + neworderdetail[i][3];
                            break;
                        case 63:userstats[j][13] = userstats[j][13] + neworderdetail[i][3];
                            break;
                        case 68:
                        case 130:
                        case 146:userstats[j][14] = userstats[j][14] + neworderdetail[i][3];
                            break;
                        case 97:
                        case 179:userstats[j][15] = userstats[j][15] + neworderdetail[i][3];
                            break;
                        case 119:userstats[j][16] = userstats[j][16] + neworderdetail[i][3];
                            break;
                    }
                    break;
                }
            }
        }

		/*
		for(int i=0;i<users;i++){
			if(user_id[i][0]==2720){
				System.out.print("User Id:"+user_id[i][0]);
				for(int j=0;j<totalDishes;j++){
					System.out.print("  "+userstats[i][j]);
				}
			}
		}*/

        // Segregating users into retained and fresh users
        int newuserstats[][] = new int[users][totalDishes];
        int newuserid[][] = new int[users][2];
        int countstats = 0;
        for (int i=0;i<users;i++){
            if(user_id[i][1]>=orderThreshold){
                newuserstats[countstats]=userstats[i];
                newuserid[countstats]=user_id[i];
                countstats++;
            }
        }
        // Copying data from local variable to class variable
        priorityusercount = countstats;
        System.out.println("The value of priority user is "+priorityusercount);
        priorityuser = new double[priorityusercount][2];
        for (int i=0;i<countstats;i++){
            priorityuser[i][0] = (double)newuserid[i][0];
            priorityuser[i][1] = (double)newuserid[i][1];
            //System.out.println(priorityuser[i][0]+"   "+priorityuser());
        }
		/*
		// Variable check
		System.out.println("The Value of number of orders are ");
	    for(int i=0;i<priorityusercount;i++){
	    	for(int j=0;j<totalDishes;j++){
	    		System.out.print("  "+ newuserstats[i][j]);
	    	}
	    	System.out.println();
	    }
		// end
		*/
        // Creating the priority list for retained user
		/* Here we are creating two files, variable Y and variable R
		 * Variable R is the boolean file which shows whether the user
		 * has rated a particular dish or not
		 * Variable Y is the rating for a particular dish of a user from 1-10
		 * */
        Y = new int[priorityusercount][totalDishes];
        R = new int[priorityusercount][totalDishes];
        int maxvalue = 0;
        temp = 0;
        for (int i = 0;i<priorityusercount;i++)
        {	//for R[i][j], define it properly
            maxvalue = 1;
            for (int j =0;j<totalDishes;j++)
                maxvalue = Math.max(maxvalue,newuserstats[i][j]);
            for (int j =0;j<totalDishes;j++){
                temp = (int) newuserstats[i][j]*9/maxvalue;
                Y[i][j] = temp + 1;
                if(newuserid[i][1] >= orderPriority)
                    R[i][j] = 1;
                else if(newuserstats[i][j]>0){
                    R[i][j] = 1;
                }
                else
                    R[i][j] = 0;
            }
        }

        /****************************************************************************************
         * Block for preparing the data for training the system:
         *
         * We will create a training vector matrix of dimensions = No. of users * No. of dishes
         * where every cell contains the no. of times that particular dish is ordered
         ***************************************************************************************/
    }

    public double[][] user_data(){
        return priorityuser;
    }

    public int user_data_count(){
        return priorityusercount;
    }

    public void meanRating(){
        // Finding the normalize ratings for the dataset
	/* NORMALIZERATINGS Preprocess data by subtracting mean rating for every
       movie (every row)
       [Ynorm, Ymean] = NORMALIZERATINGS(Y, R) normalized Y so that each movie
       has a rating of 0 on average, and returns the mean rating in Ymean.
	   */
        meanRating = new double[priorityusercount];
        double total=0;
        for (int i=0;i<priorityusercount;i++){
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
        double X[] = new double[(totalDishes+priorityusercount)*features];

        // Assigning random values to X and theta
        for (int i=0;i<(totalDishes+priorityusercount)*features;i++){
            X[i] = Math.random();
        }
        return X;
		/*  Check for parameter initialization

		for (int i=0;i<priorityusercount;i++){
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
        double theta_11[][] = new double[priorityusercount][features];

        // Assigning the values to X_11 and theta_11
        int ia=0;
        for(int k=0;k<features;k++){
            for (int j=0;j<totalDishes;j++){
                X_11[j][k] = x[ia];
                ia++;
            }
        }
        for(int k=0;k<features;k++){
            for (int j=0;j<priorityusercount;j++){
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
		for(int k=0;k<priorityusercount;k++){
			for (int j=0;j<features;j++){
				System.out.print(theta_11[k][j]  + "   ");
				//theta_11[k][j] = x[(k+totalDishes)*features + j];
			}
			System.out.println();
		}
		*/

        // Computing cost function
        double beta = 0;
        for(int i=0;i<priorityusercount;i++){
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
        for(int i=0;i<priorityusercount;i++){
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
        double theta_grad[][] = new double[priorityusercount][features];
        double cost_grad[][] = new double[priorityusercount][totalDishes];
        double final_var[] = new double[(totalDishes+priorityusercount)*features];
        double beta = 0;

        double X_11[][] = new double[totalDishes][features];
        double theta_11[][] = new double[priorityusercount][features];

        // Assigning the values to X_11 and theta_11
        int ia = 0;
        for(int k=0;k<features;k++){
            for (int j=0;j<totalDishes;j++){
                X_11[j][k] = x[ia];
                ia++;
            }
        }

        for(int k=0;k<features;k++){
            for (int j=0;j<priorityusercount;j++){
                theta_11[j][k] = x[ia];
                ia++;
            }
        }

        // computing the gradient
        for(int i=0;i<priorityusercount;i++){
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
        for(int i=0;i<priorityusercount;i++){
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
                for(int k=0;k<priorityusercount;k++){
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
            for (int j=0;j<priorityusercount;j++){
                int temp = (i+totalDishes)*features + j;
                //System.out.println("Users = "+priorityusercount+"  features = "+features+" Total dishes = "+totalDishes+"  temp"+temp);
                final_var[ia1] = theta_grad[j][i];
                ia1++;
            }
        }
        return final_var;
    }
}
