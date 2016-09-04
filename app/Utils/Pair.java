package Utils;

/**
 * Created by naresh on 04/09/16.
 */
public class Pair <T, U>{
    public T key;
    public U val;

    public Pair(T t, U u) {
        this.key = t;
        this.val = u;
    }

    public U getValue() {
        return val;
    }

    public T getKey() {
        return key;
    }

}
