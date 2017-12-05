package com.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	private static SimpleDateFormat sNormalDataFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static SimpleDateFormat sFullDataFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm a");

	private static SimpleDateFormat sH_M_A_DataFormat_12 = new SimpleDateFormat(
			"hh:mm a");
	private static SimpleDateFormat sH_M_A_DataFormat_24 = new SimpleDateFormat(
			"HH:mm");

	private static SimpleDateFormat sM_D_Y_DataFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	private static SimpleDateFormat MMdd = new SimpleDateFormat("MM月dd日");
	private static SimpleDateFormat curYYMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//年
    private final static long yearLevelValue = 365*24*60*60*1000 ; 
    //月
    private final static long monthLevelValue = 30*24*60*60*1000 ; 
    //周
    private final static long weekLevelValue = 7*24*60*60*1000 ; 
    //日
    private final static long dayLevelValue = 24*60*60*1000 ; 
    //时
    private final static long hourLevelValue = 60*60*1000 ;  
    //分
    private final static long minuteLevelValue = 60*1000 ;   
    //秒  1s=1000ms
    private final static long secondLevelValue = 1000 ; 
    
    // 短日期格式
    public static String DATE_FORMAT = "yyyy-MM-dd";
    
    // 长日期格式
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String getDate(Context mContext, long time) {

		boolean is24 = DateFormat.is24HourFormat(mContext);
		Date paramDate = new Date(time);
		// 得到今天零时零分零秒这一时刻
		Date today = null;
		try {
			today = sNormalDataFormat.parse(sNormalDataFormat
					.format(new Date()));

		} catch (ParseException e) {
			throw new org.apache.http.ParseException(e.toString());
		}
		if ((today.getTime() - paramDate.getTime()) > 0
				&& (today.getTime() - paramDate.getTime()) < 86400000) {
			if (is24) {
				sH_M_A_DataFormat_24.setTimeZone(TimeZone.getDefault());
				return "昨天 " + sH_M_A_DataFormat_24.format(paramDate);
			} else {
				sH_M_A_DataFormat_12.setTimeZone(TimeZone.getDefault());
				return "昨天 " + sH_M_A_DataFormat_12.format(paramDate);
			}
		} else if ((paramDate.getTime() - today.getTime()) >= 0) {

			if (is24) {
				sH_M_A_DataFormat_24.setTimeZone(TimeZone.getDefault());
				return "今天 " + sH_M_A_DataFormat_24.format(paramDate);
			} else {
				sH_M_A_DataFormat_12.setTimeZone(TimeZone.getDefault());
				return "今天 " + sH_M_A_DataFormat_12.format(paramDate);
			}

		} else {
			return sNormalDataFormat.format(paramDate);
		}

	}

	public static String getDate(Context mContext, long time,
			SimpleDateFormat format) {

		boolean is24 = DateFormat.is24HourFormat(mContext);
		Date paramDate = new Date(time);
		// 得到今天零时零分零秒这一时刻
		Date today = null;
		try {
			today = sNormalDataFormat.parse(sNormalDataFormat
					.format(new Date()));

		} catch (ParseException e) {
			throw new org.apache.http.ParseException(e.toString());
		}
		if ((today.getTime() - paramDate.getTime()) > 0
				&& (today.getTime() - paramDate.getTime()) < 86400000) {
			if (is24) {
				sH_M_A_DataFormat_24.setTimeZone(TimeZone.getDefault());
				return "昨天 " + sH_M_A_DataFormat_24.format(paramDate);
			} else {
				sH_M_A_DataFormat_12.setTimeZone(TimeZone.getDefault());
				return "昨天 " + sH_M_A_DataFormat_12.format(paramDate);
			}
		} else if ((paramDate.getTime() - today.getTime()) >= 0) {

			if (is24) {
				sH_M_A_DataFormat_24.setTimeZone(TimeZone.getDefault());
				return "今天 " + sH_M_A_DataFormat_24.format(paramDate);
			} else {
				sH_M_A_DataFormat_12.setTimeZone(TimeZone.getDefault());
				return "今天 " + sH_M_A_DataFormat_12.format(paramDate);
			}

		} else {
			return format.format(paramDate);
		}

	}

	public static String getDay(Context mContext, long time,
								 SimpleDateFormat format) {

		boolean is24 = DateFormat.is24HourFormat(mContext);
		Date paramDate = new Date(time);
		// 得到今天零时零分零秒这一时刻
		Date today = null;
		try {
			today = sNormalDataFormat.parse(sNormalDataFormat
					.format(new Date()));

		} catch (ParseException e) {
			throw new org.apache.http.ParseException(e.toString());
		}
		if ((today.getTime() - paramDate.getTime()) > 0
				&& (today.getTime() - paramDate.getTime()) < 86400000) {
			return "昨天";
		} else if ((paramDate.getTime() - today.getTime()) >= 0) {
			return "今天";
		} else {
			return format.format(paramDate);
		}

	}

	/**
	 * 生成系统时间戳
	 * 
	 * @author sxn
	 * @return
	 */
	public static long createCurrtentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(""
				+ System.currentTimeMillis());
		String strdate = dateFormat.format(new Date());
		return Long.parseLong(strdate) / 1000;
	}

	/**
	 * 生成秒表记时的时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static long createTimingCurrtentTime(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("00:00,000");
		String strdate = "";
		try {
			Date date = dateFormat.parse(time);
			strdate = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Long.parseLong(strdate) / 1000;
	}

	/**
	 * 反编系统时间戳
	 * 
	 * @author sxn
	 * @param mContext
	 * @param time
	 * @param format
	 * @return
	 */
	private static String getFullDate(long time, SimpleDateFormat format) {
		Date paramDate = new Date(time);
		return format.format(paramDate);

	}

	/**
	 * 反编系统时间戳
	 * 
	 * @author sxn
	 * @param mContext
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getyyyyMMddDate(long time, SimpleDateFormat format) {
		return getFullDate(time * 1000, format);
	}

	public static String getDate1000(Context mContext, long time,
			SimpleDateFormat format) {
		return getDate(mContext, time * 1000, format);
	}

	/**
	 * 如果时间单位为秒的情况，需要用这个接口，损失一点精度
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate1000(Context mContext, long time) {
		return getDate(mContext, time * 1000);
	}

	public static String getChronoscopeTime(int time) {
		return sH_M_A_DataFormat_12.format(time * 1000);
	}

	public static String getSunnyTime(int time) {
		int h, m, s;
		String hh, mm, ss;

		h = time / 3600 == 0 ? 0 : time / 3600;
		hh = h == 0 ? "00" : time / 3600 + "";

		m = (time - h * 3600) / 60 == 0 ? 0 : (time - h * 3600) / 60;
		mm = m == 0 ? "00" : (time - h * 3600) / 60 + "";

		s = (time - h * 3600 - m * 60) == 0 ? 0 : (time - h * 3600 - m * 60);
		ss = s == 0 ? "00" : (time - h * 3600 - m * 60) + "";

		return hh + ":" + mm + ":" + ss;
	}

	public static String getFullDate(long time) {

		Date paramDate = new Date(time);
		return sFullDataFormat.format(paramDate);
	}

	public static long getTime(String time_str) {
		// TODO Auto-generated method stub
		try {
			return sNormalDataFormat.parse(time_str).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得当前是星期几
	 * 
	 * @return
	 */
	public static String getWeekOfDate() throws Exception{
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];

	}

	public static String getCurMMddDate() throws Exception{
		String date = MMdd.format(new Date());
		return date;
	}

	/**
	 *
	 * @return
	 * @throws Exception
     */
	public static String getCurYYMMddHHmmss() throws Exception{
		String date = curYYMMddHHmmss.format(new Date());
		return date;
	}
	 
    //计算时间差
   public static String getDifference(long nowTime , long targetTime,String des){//目标时间与当前时间差  
       long period = targetTime - nowTime ;  
       return getDifference(period,des);  
   } 
 
   private static String getDifference(long period,String des) {//根据毫秒差计算时间差  
       String result = null ;  
       int absPeriod=(int) Math.abs(period);
         
       /*******计算出时间差中的年、月、日、天、时、分、秒*******/  
       int year = getYear(absPeriod) ;  
       int month = getMonth(absPeriod - year*yearLevelValue) ;  
       int week=getWeek(absPeriod - year*yearLevelValue - month*monthLevelValue) ; 
       int day = getDay(absPeriod - year*yearLevelValue - month*monthLevelValue) ;  
       int hour = getHour(absPeriod - year*yearLevelValue - month*monthLevelValue - day*dayLevelValue) ;  
       int minute = getMinute(absPeriod - year*yearLevelValue - month*monthLevelValue - day*dayLevelValue - hour*hourLevelValue) ;  
       int second = getSecond(absPeriod - year*yearLevelValue - month*monthLevelValue - day*dayLevelValue - hour*hourLevelValue - minute*minuteLevelValue) ;  
         
       //应用中统一的倒计时表现为：
       		//小于1分显示XXS前。
       		//小于1h，显示XX分钟前，不具体到秒。
       		//小于1d显示XX小时前，不具体到分和秒。
       		//小于7显示XX天，不具体到时分秒。
       		//小于30d显示XX周前，不具体到天时分秒。
       		//大于1个月，显示XX月前，不具体到周天时分秒
       if(absPeriod>=monthLevelValue){
       	result = year*12+month+"个月";
       }
       if(monthLevelValue>absPeriod && absPeriod>=weekLevelValue){
       	result = week+"周";
       }
       if(weekLevelValue>absPeriod && absPeriod>=dayLevelValue){
       	result = day+"天";
       }
       if(dayLevelValue>absPeriod && absPeriod>=hourLevelValue){
       	result = hour+"小时";
       }
       if(hourLevelValue>absPeriod && absPeriod>=minuteLevelValue){
       	result = minute+"分钟";
       }
       if(minuteLevelValue>absPeriod && absPeriod>=secondLevelValue){
       	result = second+"秒";
       }
       if(period>0){
       	result=result+des;
       }
       if(period<0){
       	result="延期"+result;
       }else{
       	result="今天";
       }
       //result = year+"年"+month+"月"+day+"天"+hour+"时"+minute+"分"+second+"秒";  
       return result;  
   }  
   public static int getYear(long period){  
       return (int) (period/yearLevelValue);  
   }  
   public static int getMonth(long period){  
       return (int) (period/monthLevelValue);  
   }
   public static int getWeek(long period){  
       return (int) (period/weekLevelValue);  
   }
   public static int getDay(long period){  
       return (int) (period/dayLevelValue);  
   }  
   public static int getHour(long period){  
       return (int) (period/hourLevelValue);  
   }  
   public static int getMinute(long period){  
       return (int) (period/minuteLevelValue);  
   }  
   public static int getSecond(long period){  
       return (int) (period/secondLevelValue);  
   }  
     
     
   /**
    * 将日期格式的字符串转换为长整型
    * 
    * @param date
    * @param format
    * @return
    */
   public static long convert2long(String date, String format) {
    try {
     if (date!=null && format!=null) {
      SimpleDateFormat sf = new SimpleDateFormat(format);
      return sf.parse(date).getTime();
     }
    } catch (ParseException e) {
     e.printStackTrace();
    }
    return 0l;
   }
   
   /**
    * 将长整型数字转换为日期格式的字符串
    * 
    * @param time
    * @param format
    * @return
    */
   public static String convert2String(long time, String format) {
    if (time > 0l) {
   	 if (format!=null){ 
     SimpleDateFormat sf = new SimpleDateFormat(format);
     Date date = new Date(time);
     return sf.format(date);
   	 }
    }
    return "";
   }
   
   /**
    * 获取当前系统的日期
    * 
    * @return
    */
   public static long curTimeMillis() {
    return System.currentTimeMillis();
   }
   /**
    * 将日期字符串转为date
    * 
    * @param dateStr
    * @return
    */
   public static Date string2Date(String dateStr,String format) {
   	 Date dt=null;
		 if (dateStr!=null &&!"".equals(dateStr)) {
			 SimpleDateFormat sf = new SimpleDateFormat(format);
		    	 try {
					dt = sf.parse(dateStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		 }
	     return dt;
   }
}
