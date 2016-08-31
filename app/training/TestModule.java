package training;

import javafx.util.Pair;
import models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestModule {
	public void dataTesting(CachedUserData cachedUserData, Map<Long, User> userData) {
		int notOrderingUsers = 0;
		int success[] = new int[4];
		int failure[] = new int[4];
		int total[] = new int[4];

		for (long userId: cachedUserData.getUsers()) {
			User user = userData.get(userId);
			if (user == null) {
				//System.out.print("User not found :"+userId);
			} else {
				notOrderingUsers++;
				int index = cachedUserData.getUsers().indexOf(userId);
				double userRating[] = cachedUserData.getRatings()[index];
				List<Pair<MenuItemBundle, Double>> bundleDoubleRating = new ArrayList<>();
				int i = 0;
				for (double rating : userRating) {
					bundleDoubleRating.add(new Pair<>(MenuItemBundle.values()[i], rating));
					i++;
				}
				Collections.sort(bundleDoubleRating, (a,b) -> ((a.getValue()-b.getValue() == 0?0:
																	a.getValue() - b.getValue() > 0?-1:1)));

				for (Order order : user.orders) {
					int size = 0;
					for (OrderDetail orderDetail : order.orderDetails) {
						MenuItemBundle menuItemBundle = MenuItemBundleReverseMap.getInstance()
								.getMenuItemBundleMap().get(orderDetail.menu_item_id);
						if (menuItemBundle == null) continue;
						size++;
					}

					if (size <= 0 || size > 4) continue;

					int shouldBeInTop = shouldBeInTop(size);
					int maxLimitForSuccess = failCase(size);
					int mysuccess=0, myfailure=0, mytotal=0;
					for (OrderDetail orderDetail : order.orderDetails) {
						MenuItemBundle menuItemBundle = MenuItemBundleReverseMap.getInstance()
								.getMenuItemBundleMap().get(orderDetail.menu_item_id);
						if (menuItemBundle == null) continue;
						if (isInTopNItems(bundleDoubleRating, menuItemBundle, shouldBeInTop)) {
							mysuccess++;
						} else if (!isInTopNItems(bundleDoubleRating, menuItemBundle, maxLimitForSuccess)) {
							myfailure++;
						}
						mytotal++;
					}

					if (myfailure > 0) {
						failure[size - 1]++;
					} else if (mysuccess == size){
						success[size - 1]++;
					}
					total[size - 1]++;
				}
			}
		}
		System.out.println("\nSuccess");
		printArray(success);
		System.out.println("Failure");
		printArray(failure);
		System.out.println("Total");
		printArray(total);
		int sumValue = userData.size() - notOrderingUsers;
		System.out.println("done for users :"+notOrderingUsers);
		System.out.println("not done for users :"+sumValue);


	}

	private void printArray(int[] vals){
		for (int val :vals) {
			System.out.print(val + ", ");
		}
		System.out.println("");
	}

	private int failCase(int size) {
		switch (size) {
			case 1: return  10;
			case 2: return  11;
			case 3: return  12;
			case 4: return  13;
		}

		return 0;
	}

	private int shouldBeInTop(int size) {
		switch (size) {
			case 1: return  4;
			case 2: return  6;
			case 3: return  8;
			case 4: return  10;
		}

		return 0;
	}

	public boolean isInTopNItems(List<Pair<MenuItemBundle, Double>> bundleRatingList, MenuItemBundle searchBundle, int n) {
		int i = 0;
		for (Pair<MenuItemBundle, Double> pair : bundleRatingList) {
			if (i > n) return false;
			i++;
			MenuItemBundle bundle = pair.getKey();
			if (bundle == searchBundle) {
				return true;
			}
		}
		return false;
	}

}
