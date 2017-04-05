package Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

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

    public static String getDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormatStringParse);
        return sdf.format(date);
    }

    public static Date addToDate(Date date, int hour, int minutes) {
        Calendar currentCal = Calendar.getInstance();

        currentCal.setTime(date);
        currentCal.add(Calendar.HOUR, hour);
        currentCal.add(Calendar.MINUTE, minutes);
        date = currentCal.getTime();
        return date;
    }

    public static long numberOfDays(Date date1, Date date2) {
        long diff = date2.getTime()-date1.getTime();
        int day = 24*60*60*1000;
        diff = diff/day;
        return diff;
    }

    public static byte[] compress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        try {
            ByteArrayOutputStream baostream = new ByteArrayOutputStream();
            GZIPOutputStream outStream = new GZIPOutputStream(baostream);
            outStream.write(str.getBytes("UTF-8"));
            outStream.close();
            byte[] compressedBytes = baostream.toByteArray();
            return compressedBytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String decompress(byte[] compressedBytes) {
        if (compressedBytes == null) {
            return null;
        }

        try {
            GZIPInputStream inStream = new GZIPInputStream(
                    new ByteArrayInputStream(compressedBytes));
            ByteArrayOutputStream baoStream2 = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inStream.read(buffer)) > 0) {
                baoStream2.write(buffer, 0, len);
            }
            String uncompressedStr = baoStream2.toString("UTF-8");
            return uncompressedStr;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean isLunchTime(long time) {
        Date date = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        return (hourOfDay <=15);//sun or sat
    }

}
