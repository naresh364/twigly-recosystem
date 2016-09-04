package training;

import javafx.util.Pair;
import models.*;
import play.cache.CacheApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestModule {

	private List<Long> getDataForUser(CachedUserData cachedUserData, long searchUser) {
		if (cachedUserData != null) {
			List<Long> menuPref = cachedUserData.useridVsMenuPrefMap.get(searchUser);
			if (menuPref == null) {
				notFoundUsers++;
				menuPref = cachedUserData.useridVsMenuPrefMap.get(0);
				return null;
			}
			return menuPref;
		}
		return null;
	}

	private List<Long> getDataForUser(CacheApi cacheApi, long searchUser) {
		List<Long> menuItemId = CachedUserData.retrieveDataFromCache(cacheApi, searchUser);
		if (menuItemId == null) {
			menuItemId = CachedUserData.retrieveDataFromCache(cacheApi, 0);
			notFoundUsers++;
		}
		return menuItemId;
	}

	int notFoundUsers = 0;
	public String dataTesting(CacheApi cacheApi, Map<Long, User> userData, CachedUserData cachedUserData) {
		int foundUsers = 0;
		int success[] = new int[4];
		int failure[] = new int[4];
		int total[] = new int[4];

		for (Map.Entry<Long, User> entry: userData.entrySet()) {
			User user = entry.getValue();
			List<Long> menuItemIds = getDataForUser(cachedUserData, user.user_id);
			if (menuItemIds == null) continue;
			foundUsers++;

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
					if (isInTopNItems(menuItemIds, orderDetail.menu_item_id, shouldBeInTop)) {
						mysuccess++;
					} else if (!isInTopNItems(menuItemIds, orderDetail.menu_item_id, maxLimitForSuccess)) {
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
		int sumValue = userData.size() - foundUsers;
		String val = "\nSuccess\n";
		val += printArray(success);
		val += "\nFailure\n";
		val += printArray(failure);
		val += "\nTotal\n";
		val += printArray(total);
		val += "\n Accuracy = "+(100*success[0]/total[0])+" %, "+(100*failure[0]/total[0])+" %";
		val += "\ndone for users :"+foundUsers +
			"\nusers entry not found:"+notFoundUsers+
			"\nnot done for users :"+sumValue;

		return val;
	}

	private String printArray(int[] vals){
		String out = "";
		for (int val :vals) {
			out += val + ", ";
		}
		out += "\n";
		return out;
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

	public boolean isInTopNItems(List<Long> menuItemIds, long searchId, int n) {
		int i = 0, j=0;
		if (menuItemIds == null || menuItemIds.isEmpty()) return false;
		MenuItemBundle bundle = MenuItemBundleReverseMap.getInstance().getMenuItemBundleMap().get(menuItemIds.get(0));
		for (Long menuItemId : menuItemIds) {
			if (j > n) return false;
			MenuItemBundle newbundle
					= MenuItemBundleReverseMap.getInstance().getMenuItemBundleMap().get(menuItemIds.get(i));
			if (searchId == menuItemId) {
				return true;
			}
			if (newbundle != bundle) {
				j++;
				bundle = newbundle;
			}
			i++;
		}
		return false;
	}

}
