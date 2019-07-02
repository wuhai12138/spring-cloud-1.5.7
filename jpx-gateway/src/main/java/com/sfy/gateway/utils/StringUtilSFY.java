package com.sfy.gateway.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class StringUtilSFY {

	// public static String getZh(String str) throws
	// UnsupportedEncodingException {
	// return new String(str.getBytes("ISO-8859-1"), "UTF-8");
	// }
	
	public static String getFormat(String base64){
		return base64.split(",")[0].split(";")[0].split("\\/")[1];
	}
	
	public static String getbase64Str(String base64){
		return base64.split(",")[1];
	}
	
	public static boolean getBool(String bool) {
		boolean isBool = false;
		if ("1".equalsIgnoreCase(bool)) {
			isBool = true;
		} else if ("1".equalsIgnoreCase(bool)) {
			isBool = false;
		}

		return isBool;
	}

	public static String getGBKZh(String str) throws UnsupportedEncodingException {
		return new String(str.getBytes("GBK"), "UTF-8");
	}

	public static String getZh(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String setZh(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileTimeYMDH() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		return time;
	}
	
	@SuppressWarnings("deprecation")
	public static Date geTime1990(){
		return new Date("January 1,1990 00:00:00");
	}
	
	
	public static Date getInitTime(Date time){
		if(null != time){
			if(time.getTime() != 631123200000L){
				return time;
			}
		}
		String strDate="1990-01-01";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
			date = simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getInitDate(Date time){
		if(time.getTime() == 631123200000L){
			return null;
		}else{
			return time;
		}
	}
	
	public static Date getTimeDDMMYY(String ddmmyy) {		
	    SimpleDateFormat format =  new SimpleDateFormat("dd/mm/yy"); 
	    Date date = null;
		try {
			date = format.parse(ddmmyy);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return date;
	}
	
	public static Date getTimeDDMMYYHHMMSS(String ddmmyyhhmmss) {		
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    Date date = null;
		try {
			date = format.parse(ddmmyyhhmmss);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return date;
	}

	public static String getFileTimeYMDHs() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}

	public static String formatDouble4(String strD) {
		double d = Double.parseDouble(strD) / 10000;
		// DecimalFormat df = new DecimalFormat("#.00");
		// return Double.parseDouble(df.format(d));
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(d);
	}

	public static String getEnZh(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static boolean getIsScore(String str, int score) {
		// str = "(a >= 0 && a <= 5)";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		engine.put("x", score);
		Object result;
		try {
			result = engine.eval(str);
			// System.out.println("结果类型:" + dto.getClass().getName() +
			// ",计算结果:" + dto);
			return (Boolean) result;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static Date getCurDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date time = null;
		try {
			time = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	public static Date getNewTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		try {
			time = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static Date getTomorrow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dates = new Date();// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dates);
		calendar.add(Calendar.DATE, +1);

		try {
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateStr(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		calendar.add(Calendar.MONTH, -1);

		return getTimeYMD(calendar.getTime());
	}

	public static Date getDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		calendar.add(Calendar.MONTH, -1);

		return calendar.getTime();
	}
	
	public static Date getDate(int year, int month, int day, int ho, int f) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, ho, f, 0);
		calendar.add(Calendar.MONTH, -1);

		return calendar.getTime();
	}
	
	public static Date getDate(int year, int month, int day, int ho, int f, int m) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, ho, f, m);
		calendar.add(Calendar.MONTH, -1);

		return calendar.getTime();
	}
	
	public static String getDataStrMarch(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);		
		return getTimeYMD(calendar.getTime());
	}
	
	public static String getDataStrNow(){
		Calendar calendar = Calendar.getInstance();		
		return getTimeYMD(calendar.getTime());
	}
	
	/**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
	
	 /**
     * 获取当年的第一天
     * @param year
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
         
        return currYearLast;
    }
    
    /**
     * 获取当年的最后一天
     * @param year
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }
    
    /**
     * unicode转中文
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        Matcher matcher = pattern.matcher(str);

        char ch;

        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);

            str = str.replace(matcher.group(1), ch+"" );

        }

        return str;

    }

	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.out.println("Group_" + (int)((Math.random()*9+1)*100000));
		
		System.out.println(unicodeToString("\u4ea7\u54c1\u0020\u0031\u0032\u0033"));
		// System.out.println(StringUtilSFY.getCurDate());
		// System.out.println(StringUtilSFY.getTomorrow());
		// System.out.println(Integer.parseInt(getFileTimeYMD().split("-")[1]));

		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation1','CONSTELLATION_TYPE','" + Base64FR.getBase64("巨蟹座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/juxie@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/juxie@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation2','CONSTELLATION_TYPE','" + Base64FR.getBase64("天蝎座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/tianxie@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/tianxie@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation3','CONSTELLATION_TYPE','" + Base64FR.getBase64("金牛座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/jinniu@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/jinniu@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation4','CONSTELLATION_TYPE','" + Base64FR.getBase64("双鱼座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/shuangyu@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/shuangyu@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation5','CONSTELLATION_TYPE','" + Base64FR.getBase64("狮子座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/shizi@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/shizi@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation6','CONSTELLATION_TYPE','" + Base64FR.getBase64("天秤座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/tiancheng@2x.http://192.168.8.233:8081/img/constellation/bg_img/png,tiancheng@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation7','CONSTELLATION_TYPE','" + Base64FR.getBase64("魔羯座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/mojie@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/mojie@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation8','CONSTELLATION_TYPE','" + Base64FR.getBase64("白羊座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/baiyang@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/baiyang@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation9','CONSTELLATION_TYPE','" + Base64FR.getBase64("处女座")
		// +
		// "','http://192.168.8.233:8081/img/constellation/icon/chunv@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/chunv@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation10','CONSTELLATION_TYPE','" +
		// Base64FR.getBase64("水瓶座") +
		// "','http://192.168.8.233:8081/img/constellation/icon/shuiping@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/shuiping@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation11','CONSTELLATION_TYPE','" +
		// Base64FR.getBase64("双子座") +
		// "','http://192.168.8.233:8081/img/constellation/icon/shuangzi@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/shuangzi@2x.png')");
		// System.out.println("insert into t_dict (dict_id, dict_type,
		// dict_value, dict_desc) values
		// ('constellation12','CONSTELLATION_TYPE','" +
		// Base64FR.getBase64("射手座") +
		// "','http://192.168.8.233:8081/img/constellation/icon/sheshou@2x.png,http://192.168.8.233:8081/img/constellation/bg_img/sheshou@2x.png')");

		// double d = Double.parseDouble("200000") / 10000;
		// DecimalFormat df = new DecimalFormat("######0.00");
		// System.out.println(df.format(d));

		System.out.println(getZh(""));

//		String number = "1.0.1";
		String code = "sdfs";

//		String dbNumber = "1.0.5";
		String dbcode = "105";

		int intc = 0;
		try {
			intc = Integer.parseInt(dbcode) - Integer.parseInt(code);
		} catch (Exception e) {
			System.out.println("检查发布系统或手机版本编码是否为数字类型");
			// return;
		}
		if (0 != intc) {
			if (0 > intc) {
				System.out.println("版本录入错误,发布版本不能小于操作版本");
			} else {
				int qCode = 0;
				for (int i = 1; i < intc + 1; i++) {
					// 查询数据库找到期间最大版本，并且需要强制更新节点
					qCode = Integer.parseInt(dbcode) - i;
					if (qCode != Integer.parseInt(code)) {
						System.out.println(qCode);
					}
					// if(true){
					// break;
					// }
				}

				// return;
			}
		} else {
			System.out.println("放行");
			// return;
		}
	}

	public static String getTime(Date date) {
		// Date date = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(sdf.format(date));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sdf.format(date));
		// sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return sdf.format(date);
	}

	public static String getTimeYMD(Date date) {
		// Date date = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(sdf.format(date));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(sdf.format(date));
		// sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return sdf.format(date);
	}

	public static String getTimeYMDHHmmss(Date date) {
		// Date date = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// System.out.println(sdf.format(date));
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sdf.format(date));
		// sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return sdf.format(date);
	}

	public static String getFileTime() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = format.format(date);
		return time;
	}

	public static String getFileTimeYMD() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		return time;
	}

	public static int getVerifyCode() {
		return (int) ((Math.random() * 9 + 1) * 100000);
	}

	public static void addToekn(JSONObject result, HttpServletRequest request) {
//		if (null != request.getAttribute(parameterFR.TOKEN)) {
//			dto.put(parameterFR.TOKEN, request.getAttribute(parameterFR.TOKEN));
//		}
	}

	public static void addToekn1(JSONObject result, JSONObject json) {
//		if (null != json.get(parameterFR.TOKEN)) {
//			dto.put(parameterFR.TOKEN, json.get(parameterFR.TOKEN));
//		}
	}

	public static String getLocalTempDirImg(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "static"
				+ File.separator + "tempImg";
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static boolean isAllFieldNull(Object obj) throws Exception{
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
        boolean flag = true;
        for (Field f : fs) {//遍历属性
            f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
            if("simpleDateFormat".equalsIgnoreCase(f.getName())){
            	continue;
            }
            Object val = f.get(obj);// 得到此属性的值
            if(null != val) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                flag = false;
                break;
            }
        }
        return flag;
    }
	
	/**  
	 * 格式化数字为千分位显示；  
	 * @param 要格式化的数字；  
	 * @return  
	 */    
	public static String fmtMicrometer(String text)    
	{    
	    DecimalFormat df = null;    
	    if(text.indexOf(".") > 0)    
	    {    
	        if(text.length() - text.indexOf(".")-1 == 0)    
	        {    
	            df = new DecimalFormat("###,##0.");    
	        }else if(text.length() - text.indexOf(".")-1 == 1)    
	        {    
	            df = new DecimalFormat("###,##0.0");    
	        }else    
	        {    
	            df = new DecimalFormat("###,##0.00");    
	        }    
	    }else     
	    {    
	        df = new DecimalFormat("###,##0");    
	    }    
	    double number = 0.0;    
	    try {    
	         number = Double.parseDouble(text);    
	    } catch (Exception e) {    
	        number = 0.0;    
	    }    
	    return df.format(number);    
	}
	
	public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }
	
	public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }
}