package com.wyb.pms.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DateUtil {
	
	private static final Logger L = LoggerFactory.getLogger(DateUtil.class);

	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

	private static final ThreadLocal<Map<String, DateFormat>> dateFormats = new ThreadLocal<>();

	public static DateFormat getDateFormat(String format) {
		Map<String, DateFormat> formats = dateFormats.get();
		if (null == formats) {
			formats = new HashMap<>();
		}
		DateFormat dateFormat = null;
		if (!formats.containsKey(format)) {
			dateFormat = new SimpleDateFormat(format);
			formats.put(format, dateFormat);
			dateFormats.set(formats);
		} else {
			dateFormat = formats.get(format);
		}
		return dateFormat;
	}

    /**
     * yyyy-MM-dd
     * @return
     */
	public static DateFormat getDateFormatYYYYYMMDD() {
        return getDateFormat(DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 默认时间格式
	 */
	public static final String DEFUALT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当前时间字符串
	 * @param pattern 字符串格式
	 * @return
	 */
	public static String getCurrDateStr(String pattern){
		return getDateStr(Calendar.getInstance(),pattern);
	}
	
	/**
	 * 获取当前时间字符串
	 * @return
	 */
	public static String getCurrDateStr(){
		return getCurrDateStr(DEFUALT_DATE_FORMAT);
	}
	
	/**
	 * 获取指定时间格式的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateStr(Calendar date,String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date.getTime());
	}
	
	/**
	 * 将时间字符串转换成时间对象Calendar
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static Calendar getDate(String dateStr,String pattern) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		Calendar c=Calendar.getInstance();
		c.setTime(sdf.parse(dateStr));
		return c;
	}
	
	/**
	 * 将时间字符串转换成时间对象Calendar，字符串格式默认：yyyy-MM-dd HH:mm:ss格式
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static Calendar getDate(String dateStr) throws Exception{
		return getDate(dateStr,DEFUALT_DATE_FORMAT);
	}
	
	/**
	 * 将long时间转换成时间字符串
	 * @param longTime
	 * @param pattern
	 * @return
	 */
	public static String getDateStr(long longTime,String pattern){
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(longTime);
		return getDateStr(c,pattern);
	}
	
	/**
	 * 将long时间转成时间字符串HH:mm
	 * @param longTime 
	 * @return
	 */
	public static String getTimeStr(String longTime){
		String time="";
		if(longTime!=null && !"".equals(longTime)){
			Long times;
			try{
				times=Long.parseLong(longTime);
			}catch(Exception e){
				return time;
			}
			long offset=System.currentTimeMillis()-times;
			long day=(offset/1000/60/60/24);
			if(day==0){
				time=getDateStr(times, "HH:mm");
			}else if(day==1){
				time="昨天";
			}else{
				time=day+"天前";
			}
		}
		return time;
	}

    /**
     * 获取某个时间点偏移n天后的时间
     * @param srcDate 目标时间点
     * @param offset 偏移天数。 正数：未来的时间点; 负数：过去的时间点
     * @return
     */
	public static Date offsetDays(Date srcDate, int offset) {
		L.debug("srcDate = {}, offset = {}", srcDate, offset);
	    Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        c.add(Calendar.DAY_OF_YEAR, offset);
        Date resDate = c.getTime();
        L.debug("resDate = {}", resDate);
        return resDate;
    }

	/**
	 * 获取某个时间点偏移n小时后的时间
	 * @param srcDate 目标时间点
	 * @param offset 偏移小时数。 正数：未来的时间点; 负数：过去的时间点
	 * @return
	 */
	public static Date offsetHours(Date srcDate, int offset) {
		Calendar c = Calendar.getInstance();
		c.setTime(srcDate);
		c.add(Calendar.HOUR_OF_DAY, offset);
		Date resDate = c.getTime();
		return resDate;
	}

	/**
	 * 生成日期序列 。 日期格式yyyy-MM-dd
	 * 	<pre> 条件与返回结果说明：
	 * 	start > finish : []
	 * 	start = finish : [start]
	 * 	start < finish : [start, start + 1, ..., finish]
	 * 	</pre>
	 * @param start 起始日期
	 * @param finish 结束日期
	 * @return
	 */
	public static List<String> generateDateSeq(String start, String finish) {
		try {
			DateFormat df = getDateFormatYYYYYMMDD();
			return generateDateSeq(df.parse(start), df.parse(finish));
		} catch (ParseException e) {
			return null;
		}
	}


	/**
	 * 生成日期序列 。 日期格式yyyy-MM-dd
	 * 	<pre> 条件与返回结果说明：
	 * 	start > finish : []
	 * 	start = finish : [start]
	 * 	start < finish : [start, start + 1, ..., finish]
	 * 	</pre>
	 * @param start 起始日期
	 * @param finish 结束日期
	 * @return
	 */
	public static List<String> generateDateSeq(Date start, Date finish) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(start);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(finish);
		return generateDateSeq(c1, c2);
	}

	/**
	 * 生成日期序列 。 日期格式yyyy-MM-dd
	 * 	<pre> 条件与返回结果说明：
	 * 	start > finish : []
	 * 	start = finish : [start]
	 * 	start < finish : [start, start + 1, ..., finish]
	 * 	</pre>
	 * @param start 起始日期
	 * @param finish 结束日期
	 * @return
	 */
    public static List<String> generateDateSeq(Calendar start, Calendar finish) {
		List<String> seq = new ArrayList<>();
		start = setTo0(start);
		finish = setTo0(finish);
		DateFormat df = getDateFormatYYYYYMMDD();
		if (start.after(finish)) {
		    return seq;  // empty
		} else if (start.equals(finish)) {
			seq.add(df.format(start.getTime()));
		} else {
			do {
				seq.add(df.format(start.getTime()));
				start.add(Calendar.DATE, 1);
			} while (!start.after(finish));
		}
		return seq;
	}

	public static Date strToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private static Calendar setTo0(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return setTo0(c);
	}

	private static Calendar setTo0(Calendar c) {
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

}
