package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naresh on 30/08/16.
 */
public class MenuItemBundleReverseMap {
    public static Map<Long, MenuItemBundle> menuItemBundleMap;;
    public static MenuItemBundleReverseMap instance;

    public static MenuItemBundleReverseMap getInstance() {
        if (instance == null) instance = new MenuItemBundleReverseMap();
        return instance;
    }

    private Map<Long, MenuItemBundle> createMapForMenuItemtoBundle() {
        Map<Long, MenuItemBundle> itemToBundleMap = new HashMap<>();
        for (MenuItemBundle bundle: MenuItemBundle.values()) {
            List<Long> ids = bundle.getMenu_item_ids();
            for (long id : ids) {
                itemToBundleMap.put(id, bundle);
            }
        }

        return itemToBundleMap;
    }

    public Map<Long, MenuItemBundle> getMenuItemBundleMap(){
        if (menuItemBundleMap == null) {
            menuItemBundleMap = createMapForMenuItemtoBundle();
        }

        return menuItemBundleMap;
    }
}
