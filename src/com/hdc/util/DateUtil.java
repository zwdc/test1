package com.hdc.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @ClassName: DateUtil
 * @Description:TODO(时间转换工具类)
 * @author: zml
 * @date: 2014-4-18 上午10:20:40
 *
 */
public class DateUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	  /** 
     * 获取当年的第一天 
     * @param year 
     * @return 
     */  
    public static String getCurrYearFirst(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearFirst(currentYear);  
    }  
      
    /** 
     * 获取当年的最后一天 
     * @param year 
     * @return 
     */  
    public static String getCurrYearLast(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearLast(currentYear);  
    }  
	  /** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static String getYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return sdf.format(currYearFirst);  
    }  
      
    /** 
     * 获取当年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static String getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();           
        return sdf.format(currYearLast);  
    }  
  
	
	public static Date StringToDate(String dateString,String formatString){
		try {
			DateFormat fm = new SimpleDateFormat(formatString);
			Date date = null;
			try {
				date = fm.parse(dateString);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return null;
			} // Thu Jan 18 00:00:00 CST 2007
			return date;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public static String DateToString(Date date,String formatString){
		try {
			DateFormat fm = new SimpleDateFormat(formatString);
			String str = new String();
			str = fm.format(date);   
			return str;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public static String penaltyString(int second) {
		
		int sec=second%60;
			second=second/60;
		int min=second%60;	
		int hour=second=second/60;
		
		String nowTime=new String();
		
		if(hour<10) {
			if(hour==0) nowTime="00";
			nowTime="0"+hour;
		}else {nowTime=Integer.toString(hour);}
	
		if(min<10) {
			nowTime=nowTime+":0"+min;
		}else {nowTime=nowTime+":"+min;}
		
		if(sec<10) {
			nowTime=nowTime+":0"+sec;
		}else {nowTime=nowTime+":"+sec;}
		
		return nowTime;
	}
	public static String secondToString(long second) {
		
		long sec=second%60;
			second=second/60;
		long min=second%60;	
		long hour=second=second/60;
		
		String nowTime=new String();
		
		if(hour<10) {
			if(hour==0) nowTime="00";
			nowTime="0"+hour;
		}else {nowTime=Long.toString(hour);}
	
		if(min<10) {
			nowTime=nowTime+":0"+min;
		}else {nowTime=nowTime+":"+min;}
		
		if(sec<10) {
			nowTime=nowTime+":0"+sec;
		}else {nowTime=nowTime+":"+sec;}
		
		return nowTime;
	}

	public static String toFriendlyDate(Date time){
		if(time == null) return "unknown";
		int ct = (int)((System.currentTimeMillis() - time.getTime())/1000);
		if(ct < 3600)
			return Math.max(ct / 60,1) +" minutes ago";
		if(ct >= 3600 && ct < 172800)  // 48小时
			return ct / 3600 + " hours ago";
		if(ct >= 172800 && ct < 2592000){ //86400 * 30
			int day = ct / 86400 ;	
			return day + " days ago";			
		}
		if(ct >= 2592000 && ct < 62208000) //到24个月
			return ct / 2592000 + " months ago";
		return ct / 31104000 + " years ago";
	}

	// 返回年份
	public static int getYear(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.YEAR);
	}

	// 返回月份
	public static int getMonth(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MONTH) + 1;
	}

	// 返回天
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	// 返回秒钟
	public static int getSecond(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.SECOND);
	}

	// 返回小时
	public static int getHour(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.HOUR_OF_DAY);
	}

	// 返回分钟
	public static int getMinute(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.get(java.util.Calendar.MINUTE);
	}

	// 返回毫秒
	public static long getMillis(java.util.Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	// 	日期相减
	//	相差天数：long day=difference/(3600*24*1000) 
	//	相差小时：long hour=difference/(3600*1000) 
	//	相差分钟：long minute=difference/(60*1000) 
	//	相差秒： long second=difference/1000 
	public static int diffDate(java.util.Date date, java.util.Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (3600*1000));
	}
	
	public static String getTodayString() {
		Date d = new Date();
		return sdf.format(d);
	}
}
