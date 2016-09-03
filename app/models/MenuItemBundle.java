package models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naresh on 30/08/16.
 */
public enum MenuItemBundle {
    //BUNDLE1(Arrays.asList(5l)),
    //BUNDLE2(Arrays.asList(7l)),
    //BUNDLE3(Arrays.asList(9l)),
    //BUNDLE4(Arrays.asList(10l, 13l, 18l)),
    //BUNDLE5(Arrays.asList(11l, 14l, 19l)),
    //BUNDLE6(Arrays.asList(30l)),
    //BUNDLE7(Arrays.asList(35l)),
    //BUNDLE8(Arrays.asList(39l, 131l, 148l)),
    //BUNDLE9(Arrays.asList(49l, 107l, 112l, 120l)),
    //BUNDLE10(Arrays.asList(53l, 54l, 143l, 144l, 145l)),
    //BUNDLE11(Arrays.asList(55l)),
    //BUNDLE12(Arrays.asList(50l,58l, 138l, 182l)),
    //BUNDLE13(Arrays.asList(61l, 129l, 147l)),
    //BUNDLE14(Arrays.asList(63l)),
    //BUNDLE15(Arrays.asList(68l, 130l, 146l)),
    //BUNDLE16(Arrays.asList(97l, 179l)),
    //BUNDLE17(Arrays.asList(119l));

    Peri(Arrays.asList(5l)),
    BBQ(Arrays.asList(7l)),
    HPP(Arrays.asList(9l)),
    PSTC(Arrays.asList(35l)),
    PSTCC(Arrays.asList(63l)),
    IYWIY(Arrays.asList(211l)),
    VEG_PASTA(Arrays.asList(10l,13l,194l,19l,199l)),
    NVEG_PASTA(Arrays.asList(11l,14l,195l,18l,200l)),
    VEG_CURRY(Arrays.asList(68l,130l,146l)),
    PRAWN_CURRY(Arrays.asList(39l,131l,148l)),
    CHICKEN_CURRY(Arrays.asList(61l,129l,147l)),
    GRILLED(Arrays.asList(97l,179l,190l,40l,92l)),
    //RARE_SANDWICH(Arrays.asList(55l,114l,8l,22l)),
    //RARE_SANDWICH_NVEG(Arrays.asList(12l,119l,36l)),
    //EXP_NVEG(Arrays.asList(98l,105l,188l)),
    //EXP_VEG(Arrays.asList(103l,104l)),
    SHAKE(Arrays.asList(49l,107l,120l,112l,135l)),
    SMOOTHIE(Arrays.asList(50l,58l,187l)),
    SWEET_CHOCOLATE(Arrays.asList(176l,193l)),
    SWEET_CAKE(Arrays.asList(51l,189l,198l,201l));

    private List<Long> menu_item_ids;

    private MenuItemBundle(List<Long> menu_item_ids) {
        this.menu_item_ids = menu_item_ids;
    }

    public boolean hasMenuItem(long menu_item_id) {
        for (long id : menu_item_ids) {
            if (menu_item_id == id) return true;
        }
        return false;
    }

    public List<Long> getMenu_item_ids() {
        return menu_item_ids;
    }

}
