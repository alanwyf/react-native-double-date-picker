package com.alanwyf.doubleDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.util.Log;

public class DateUtils {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String HHMMSS = "HH:mm:ss";

    public static final int MODE_MILLISECOND = 0;
    public static final int MODE_SECOND = 1;
	
	/**
	 * 获取当前时间转化成字符串类型 yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentDateString(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
		return sdf.format(new Date());
	}
	
	/**
	 * 将java.util.Date 类型日期转化成字符串类型
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
		return sdf.format(date);
	}
	
	/**
	 * 将当前时间增加或者减少指定的天数后转化为字符串型表示
	 * @param addDay 增加或者减少的天数，+ 表示增加  - 表示减少
	 * @return
	 */
	public static String getAddDayString(int addDay){
		Date date = getAddDay(addDay);
		return getDateString(date);
	}
	
	/**
	 * 将当前时间增加或者减少指定的天数
	 * @return
	 */
	public static Date getAddDay(int addDay){
		Calendar cal = Calendar.getInstance();  
        int day = cal.get(Calendar.DAY_OF_MONTH);  
        cal.set(Calendar.DAY_OF_MONTH, day + addDay);
        Date date = cal.getTime();
        return date;
	}
	
	/**
	 * 获取当前时间 秒
	 * @return
	 */
	public static long getCurrentTimeLong(){
		return new Date().getTime() / 1000;
	}
	
	/**
	 * 获取当前时间 毫秒
	 */
	public static long getCurrentTimeMillisecondLong(){
		return new Date().getTime();
	}
	
	/**
	 * 秒时间转化
	 * @param seconds
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

    //字符串转时间戳
	public static long strToTime(String string, String format, int mode){
        if (string == null || string.isEmpty() || string.equals("null")) {
            return -1;
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        long rs = -1;
        try {
            int ink = mode == MODE_SECOND ? 1 : 1000;
            rs = sdf.parse(string).getTime();
            rs /= 1000;
        }catch (ParseException e){
            e.printStackTrace();
            Log.e("Ray", e.getMessage());
        }
        return rs;
    }
	

}
