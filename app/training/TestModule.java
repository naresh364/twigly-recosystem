package training;

import javafx.util.Pair;
import models.*;
import play.cache.CacheApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestModule {

	private double[] getDataForUser(CacheApi cacheApi, CachedUserData cachedUserData, long searchUser) {
		if (cachedUserData != null) {
			int i = cachedUserData.getUsers().indexOf(searchUser);
			if (i < 0) {
				i =0;
				notFoundUsers++;
				//return null;
			}
			return cachedUserData.getRatings()[i];
		} else {
			double userRating[] = CachedUserData.retrieveDataFromCache(cacheApi, searchUser);
			if (userRating == null) {
				userRating = CachedUserData.retrieveDataFromCache(cacheApi, 0);
				notFoundUsers++;
			}
			return userRating;
		}
	}

	int notFoundUsers = 0;
	public void dataTesting(CacheApi cacheApi, Map<Long, User> userData, CachedUserData cachedUserData) {
		int foundUsers = 0;
		int success[] = new int[4];
		int failure[] = new int[4];
		int total[] = new int[4];

		for (Map.Entry<Long, User> entry: userData.entrySet()) {
			User user = entry.getValue();
			double userRating[] = getDataForUser(cacheApi, cachedUserData, user.user_id);
			if (userRating == null) continue;
			foundUsers++;
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
		System.out.println("\nSuccess");
		printArray(success);
		System.out.println("Failure");
		printArray(failure);
		System.out.println("Total");
		printArray(total);
		int sumValue = userData.size() - foundUsers;
		System.out.println("done for users :"+foundUsers);
		System.out.println("users entry not found:"+notFoundUsers);
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
