package training;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class testing_class {
	static int totalDishes = 17;
	static int len = 50000;
	static int testing_data[][];
	static int testing_data_count;
	static int features = 15;
	
	public static void testingDataPreparation() throws FileNotFoundException{
		/* For testing data we need six data sets
		 * 1. Order id
		 * 2. User id
		 * 3. Menu item id
		 * 4. Quantity
		 * 5. Price
		 * 6. Discount 
		 * */
		/********************************************************************************
		 Block for finding the unique users and total order placed for order list
		 *********************************************************************************/
		File f = new File("abc1.txt");
		Scanner sc = new Scanner(f);
		int orders[][] = new int[len][6];
		int count_test = 0;
		int temp = 0;
		for(int i=0;i<len;i++){
			for(int j=0;j<6;j++){
				if(sc.hasNextLine()==false){
					temp = 1;
					break;
				}
				orders[i][j] = sc.nextInt();
			}
			if(temp == 1)
				break;
			count_test = count_test+1;
		}
		sc.close();	
		System.out.println("Total number of enteries "+count_test);
		int unique_orders[] = new int[count_test];
		int user_uniq[] = new int[count_test];
		int uniq = 0;
		temp=0;
		for(int i=0;i<count_test;i++){
			for(int j=0;j<uniq;j++){
				if(orders[i][0] == unique_orders[j]){
					temp = 1;
					break;
				}
			}
			if(temp == 0){
				unique_orders[uniq] = orders[i][0];
				user_uniq[uniq] = orders[i][1];
				uniq = uniq+1;
			}
			temp=0;
		}
		System.out.println("Total unique orders are "+uniq);
		testing_data = new int[uniq][totalDishes+3];
		/* 1st column : order id
		 * 2nd column : user id
		 * 3rd column : total quantities
		 * */
		
		for(int i=0;i<uniq;i++){
			testing_data[i][0] = unique_orders[i];
			testing_data[i][1] = user_uniq[i];
		}
		int add = 0;
		// Classifying unique orders
		for(int i=0;i<count_test;i++){
			add = 0;
			for(int j=0;j<uniq;j++){
				if(orders[i][0]==testing_data[j][0])
					add = j;
			}
			switch(orders[i][2]){
			case 5:
				testing_data[add][3] = testing_data[add][3]+1;
				break;
			case 7:
				testing_data[add][4] = testing_data[add][4]+1;
				break;
			case 9:
				testing_data[add][5] = testing_data[add][5]+1;
				break;
			case 10:
				testing_data[add][6] = testing_data[add][6]+1;
				break;
			case 11:
				testing_data[add][7] = testing_data[add][7]+1;
				break;
			case 30:
				testing_data[add][8] = testing_data[add][8]+1;
				break;
			case 35:
				testing_data[add][9] = testing_data[add][9]+1;
				break;
			case 39:
				testing_data[add][10] = testing_data[add][10]+1;
				break;
			case 49:
				testing_data[add][11]= testing_data[add][11]+1;
				break;
			case 54:
				testing_data[add][12] = testing_data[add][12]+1;
				break;
			case 55:
				testing_data[add][13] = testing_data[add][13]+1;
				break;
			case 58:
				testing_data[add][14] = testing_data[add][14]+1;
				break;
			case 61:
				testing_data[add][15] = testing_data[add][15]+1;
				break;
			case 63:
				testing_data[add][16] = testing_data[add][16]+1;
				break;
			case 68:
				testing_data[add][17] = testing_data[add][17]+1;
				break;
			case 97:
				testing_data[add][18] = testing_data[add][18]+1;
				break;
			case 119:
				testing_data[add][19] = testing_data[add][19]+1;
				break;
			}
		}
		for(int i=0;i<uniq;i++){
			for(int j=0;j<17;j++){
				testing_data[i][2] = testing_data[i][2]+testing_data[i][j+3];
			}
		}
		testing_data_count = uniq;
		System.out.println("User data");
		for(int i=0;i<uniq;i++){
			for(int j=0;j<totalDishes+3;j++){
				System.out.print(testing_data[i][j]+"  ");
			}
			System.out.println();
		}
	}
	
	public static void data_testing() throws FileNotFoundException{
		// reading testing parameters from file
		File f1 = new File("X.txt");
		File f2 = new File("theta.txt");
		File f3 = new File("priority_user.txt");
		Scanner sc3 = new Scanner(f3);
		Scanner sc2 = new Scanner(f2);
		Scanner sc1 = new Scanner(f1);
		int total_user = 0;
		int a[][] = new int[len][2];
		int temp1 = 0; // Temp variable for checking end of file
		for(int i=0;i<len;i++)
		{
			for(int j=0;j<2;j++)
			{
				if(sc3.hasNextLine()==false){
					temp1 = 1;
					break;
				}
				a[i][j] = sc3.nextInt();
			}
			if(temp1 == 1)
				break;
			total_user = total_user+1;
		}
	    //System.out.println(count_order_details);
		sc3.close();
		int priority_user[][] = new int[total_user][2];
		for(int i=0;i<total_user;i++)
			priority_user[i] = a[i];
		
		double theta[][] = new double[total_user][features];
		temp1 = 0; // Temp variable for checking end of file
		for(int i=0;i<total_user;i++)
		{
			for(int j=0;j<features;j++)
			{
				if(sc2.hasNextLine()==false){
					temp1 = 1;
					break;
				}
				theta[i][j] = sc2.nextDouble();
			}
			if(temp1 == 1)
				break;
		}
	    //System.out.println(count_order_details);
		sc2.close();
		
		double X[][] = new double[totalDishes][features];
		temp1 = 0; // Temp variable for checking end of file
		for(int i=0;i<totalDishes;i++)
		{
			for(int j=0;j<features;j++)
			{
				if(sc1.hasNextLine()==false){
					temp1 = 1;
					break;
				}
				X[i][j] = sc1.nextDouble();
			}
			if(temp1 == 1)
				break;
		}		
	    //System.out.println(count_order_details);
		sc1.close();
		
		
		// Defining rating and false rating patterns
		int total_score = 0;
		int score_achieved = 0;
		int score_failed = 0;
		
		int rating_for_1 = 4;
		int rating_for_2 = 6;
		int rating_for_3 = 8;
		int rating_for_4 = 10;
		
		int false_rating_for_1 = 10;
		int false_rating_for_2 = 11;
		int false_rating_for_3 = 12;
		int false_rating_for_4 = 13;
		
		int individual_total[] = new int[4];
		int individual_correct[] = new int[4];
		int individual_false[] = new int[4];
		int temp=0;
		double tempo=0;
		int index = 0;
		double my_prediction[] = new double[totalDishes];
		int user_rating[] = new int[totalDishes];
		double ratings[][] = new double[total_user][totalDishes];
		for(int i=0;i<total_user;i++){
			for(int j=0;j<totalDishes;j++){
				for(int k=0;k<features;k++){
					ratings[i][j] = ratings[i][j] + X[j][k]*theta[i][k];
				}
			}
		}
		int test_temp = 0;
		int false_test_temp = 0;
		for(int i=0;i<testing_data_count;i++){
			for(int j=0;j<total_user;j++){
				if(priority_user[j][0] == testing_data[i][1]){
					temp = 1;
					index = j;
				}
			}
			if(temp>0){
				temp = 0;
				// Enter the score criteria for testing
				for(int j=0;j<totalDishes;j++){
					my_prediction[j] = ratings[index][j];
					user_rating[j] = j;
				}
				// sorting order 
				for(int j=0;j<totalDishes;j++){
					for(int k=j+1;k<totalDishes;k++){
						if(my_prediction[j]<my_prediction[k]){
							tempo = my_prediction[j];
							my_prediction[j] = my_prediction[k];
							my_prediction[k] = tempo;
							temp = user_rating[j];
							user_rating[j] = user_rating[k];
							user_rating[k] = temp;
							temp = 0;
						}
					}
				}
				int count = 0;
				for(int j=0;j<totalDishes;j++){
					if(testing_data[i][j+3]>0)
						count = count+1;
				}
				test_temp = 0;
				false_test_temp = 0;
				switch (count){
				case 1:individual_total[0] = individual_total[0]+1;
					   for(int k = 0;k<totalDishes;k++){
						   if(testing_data[i][k+3]>0){
							   for(int l=0;l<rating_for_1;l++){
								   if(k==user_rating[l]){
									   test_temp = test_temp+1;
								   }
							   }
							   for(int l=false_rating_for_1;l<totalDishes;l++){
								   if(k==user_rating[l])
									   false_test_temp = false_test_temp+1;
							   }
						   }
					   }
					   if(test_temp ==1){
						   score_achieved = score_achieved+1;
						   individual_correct[0] = individual_correct[0]+1;
					   }
					   if(false_test_temp >=1){
						   score_failed = score_failed+1;
						   individual_false[0] = individual_false[0]+1;
					   }
					break;
				case 2:individual_total[1] = individual_total[1]+1;
				   for(int k = 0;k<totalDishes;k++){
					   if(testing_data[i][k+3]>0){
						   for(int l=0;l<rating_for_1;l++){
							   if(k==user_rating[l]){
								   test_temp = test_temp+1;
							   }
						   }
						   for(int l=false_rating_for_1;l<totalDishes;l++){
							   if(k==user_rating[l])
								   false_test_temp = false_test_temp+1;
						   }
					   }
				   }
				   if(test_temp ==2){
					   score_achieved = score_achieved+1;
					   individual_correct[1] = individual_correct[1]+1;
				   }
				   if(false_test_temp >=1){
					   score_failed = score_failed+1;
					   individual_false[1] = individual_false[1]+1;
				   }
					break;
				case 3:individual_total[2] = individual_total[2]+1;
				   for(int k = 0;k<totalDishes;k++){
					   if(testing_data[i][k+3]>0){
						   for(int l=0;l<rating_for_1;l++){
							   if(k==user_rating[l]){
								   test_temp = test_temp+1;
							   }
						   }
						   for(int l=false_rating_for_1;l<totalDishes;l++){
							   if(k==user_rating[l])
								   false_test_temp = false_test_temp+1;
						   }
					   }
				   }
				   if(test_temp ==1){
					   score_achieved = score_achieved+1;
					   individual_correct[2] = individual_correct[2]+1;
				   }
				   if(false_test_temp >=1){
					   score_failed = score_failed+1;
					   individual_false[2] = individual_false[2]+1;
				   }
					break;
				case 4:individual_total[3] = individual_total[3]+1;
				   for(int k = 0;k<totalDishes;k++){
					   if(testing_data[i][k+3]>0){
						   for(int l=0;l<rating_for_1;l++){
							   if(k==user_rating[l]){
								   test_temp = test_temp+1;
							   }
						   }
						   for(int l=false_rating_for_1;l<totalDishes;l++){
							   if(k==user_rating[l])
								   false_test_temp = false_test_temp+1;
						   }
					   }
				   }
				   if(test_temp ==1){
					   score_achieved = score_achieved+1;
					   individual_correct[3] = individual_correct[3]+1;
				   }
				   if(false_test_temp >=1){
					   score_failed = score_failed+1;
					   individual_false[3] = individual_false[3]+1;
				   }
					break;
				}
			}  // closing for temp
			temp = 0;
			index = 0;
			false_test_temp = 0;
		}	
		System.out.println("Total Score is "+total_score);
		System.out.println("Score Achieved is "+score_achieved);
		System.out.println("Score failed is "+score_failed);
		
		System.out.println("Score for user with one order is "+individual_total[0]);
		System.out.println("Score Achieved is "+individual_correct[0]);
		System.out.println("Score failed is "+individual_false[0]);
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		testingDataPreparation();
	}
}
