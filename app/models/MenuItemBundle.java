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

    PERI(0, Arrays.asList(5l)),
    PSTC(0, Arrays.asList(35l)),
    GREEKSW(0, Arrays.asList(285l)),
    AMRITSARISW(0, Arrays.asList(293l)),
    FISHY_SW(0, Arrays.asList(36l,297l)),
    PATTY_SW(0, Arrays.asList(262l,304l)),
    SALAMI_SW(0, Arrays.asList(86l,98l,105l)),
    OTHER_SW(0, Arrays.asList(7l,12l,119l,188l)),
    HPP(0, Arrays.asList(9l)),
    PSCC(0, Arrays.asList(63l)),
    FHSW(0, Arrays.asList(284l)),
    PANEER_VEG(0, Arrays.asList(8l,287l,294l,316l)),
    MUSHROOM_VEG(0, Arrays.asList(55l,324l)),
    POTATO_VEG(0, Arrays.asList(114l,301l)),
//    EXP_VEG(0, Arrays.asList(103l,261l)),
//    OTHER_SANDWICH_VEG(0, Arrays.asList(22l,289l)),
    VEG_PASTA(0, Arrays.asList(10l,19l,62l,194l)),
    NVEG_PASTA(0, Arrays.asList(11l,18l,195l,286l)),
    VEG_CURRY(0, Arrays.asList(68l,130l,146l)),
    NVEG_CURRY(0, Arrays.asList(39l,61l,129l,131l,147l)),
    VEG_GRILLED(0, Arrays.asList(28l,102l,251l,288l)),
    NVEG_GRILLED(0, Arrays.asList(29l,40l,97l,101l,179l,190l,253l)),
    VEG_PIZZA (0, Arrays.asList(266l,270l,271l,272l,278l)),
    NVEG_PIZZA (0, Arrays.asList(268l,269l,281l)),
    OREO(0.3f, Arrays.asList(107l)),
    BLUEBERRY(0.3f, Arrays.asList(49l)),
    SHAKES(0.3f,Arrays.asList(112l,120l,135l,314l)),
    SMOOTHIES(0.3f,Arrays.asList(50l,58l,187l)),
    DRINKS(0.3f,Arrays.asList(233l,236l,237l)),
    SWEET_CAKE(.5f,Arrays.asList(30l,280l,311l)),
    BROWNIE(.5f, Arrays.asList(176l,193l,227l,282l));


//    PERI(0, Arrays.asList(5l)),
//    PSTC(0, Arrays.asList(35l)),
//    HPP(0, Arrays.asList(9l)),
//    PSCC(0, Arrays.asList(63l)),
//    NVWG_SW(0, Arrays.asList(7l,12l,36l,86l,98l,105l,119l,188l,262l,285l,293l,297l,304l)),
//    VEG_SW(0, Arrays.asList(8l,103l,235l,261l,284l,287l,294l,316l,22l,55l,114l,289l,301l)),
//    VEG_HOT(0, Arrays.asList(10l,19l,62l,194l,68l,130l,146l)),
//    NVEG_HOT(0, Arrays.asList(11l,18l,195l,286l,39l,61l,129l,131l,147l)),
//    VEG_GRILLED(0, Arrays.asList(28l,102l,251l,288l)),
//    NVEG_GRILLED(0, Arrays.asList(29l,40l,97l,101l,179l,190l,253l)),
//    VEG_PIZZA (0, Arrays.asList(266l,270l,271l,272l,278l)),
//    NVEG_PIZZA (0, Arrays.asList(268l,269l,281l)),
//    OREO(0.3f, Arrays.asList(107l)),
//    BLUEBERRY(0.3f, Arrays.asList(49l)),
//    SHAKES_N_SMOOTHIES(0.3f,Arrays.asList(50l,58l,112l,120l,135l,187l,314l)),
//    DRINKS(0.3f,Arrays.asList(233l,236l,237l,276l,277l,283l)),
//    BROWNIE(.5f, Arrays.asList(176l,193l,227l,282l)),
//    SWEET_CAKE(.5f, Arrays.asList(30l,189l,201l,280l,290l,298l,311l,317l,318l,319l,325l));

    private List<Long> menu_item_ids;
    //A bundle cannot appear before this min position. for example if value = 0.5
    // this bundle has to be after n/2 items
    private float minPositionFactor;

    private MenuItemBundle(float minPositionFactor, List<Long> menu_item_ids) {
        this.menu_item_ids = menu_item_ids;
        this.minPositionFactor = minPositionFactor;
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

    public float getMinPositionFactor() {
        return minPositionFactor;
    }
}
