package Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by naresh on 28/08/16.
 */
public class AppUtils {
    public static String md5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] hash;

        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    public static String timeFormatStringParse = "yyyy-MM-dd";
    public static String timeFormatStringSet = "yyyy-MM-dd HH:mm";

    public static Date getDate(String from) {
        Date dateF = null;
        try {
            dateF = new SimpleDateFormat(timeFormatStringParse).parse(from);
        }catch (Exception e){
            return null;
        }
        return dateF;

    }

    public static Date addToDate(Date date, int hour, int minutes) {
        Calendar currentCal = Calendar.getInstance();

        currentCal.setTime(date);
        currentCal.add(Calendar.HOUR, hour);
        currentCal.add(Calendar.MINUTE, minutes);
        date = currentCal.getTime();
        return date;

    }
}
