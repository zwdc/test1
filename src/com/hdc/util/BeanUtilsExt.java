package com.hdc.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

/**
 * 
 * @ClassName: BeanUtils
 * @Description:工具类，判断bean，数组，集合是否为空
 * @author: zml
 * @date: 2015-8-6 上午11:20:26
 *
 */
public class BeanUtilsExt extends BeanUtils{
	static {
	       ConvertUtils.register(new DateConvert(), java.util.Date.class);
	       ConvertUtils.register(new DateConvert(), java.sql.Date.class);
	   }

	   public static void copyProperties(Object dest, Object orig) {
	       try {
	           BeanUtils.copyProperties(dest, orig);
	       } catch (IllegalAccessException ex) {
	           ex.printStackTrace();
	       } catch (InvocationTargetException ex) {
	           ex.printStackTrace();
	       }
	   }
	public static boolean isBlank(Object obj){
		if(obj == null){
			return true;
		}
		return false;
	}
	@SuppressWarnings("rawtypes")
	public static boolean isBlank(List list){
		if(list == null || list.size()<=0){
			return true;
		}
		return false;
	}
	@SuppressWarnings("rawtypes")
	public static boolean isBlank(Map map){
		if(map == null || map.size()<=0){
			return true;
		}
		return false;
	}
	public static boolean isBlank(Object []obj){
		if(obj == null || obj.length<=0){
			return true;
		}
		return false;
	}
}
