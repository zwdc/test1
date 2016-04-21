package com.hdc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.beanutils.Converter;

/**
* 重写日期转换
*
* @author houzhiqing
*/
public class DateConvert implements Converter {

   public Object convert(Class arg0, Object arg1) {
       String p=null;
	   if(arg1!=null){
    	   p=arg1.toString();
       }
       if (p == null || p.trim().length() == 0) {
           return null;
       }
       try {
           SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           return df.parse(p.trim());
       } catch (Exception e) {
           try {
               SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
               return df.parse(p.trim());
           } catch (ParseException ex) {
               return null;
           }
       }

   }

}
