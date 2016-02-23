package com.hdc.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratedCode implements Serializable {
	
	private static final long serialVersionUID = -8119848121961255688L;

	private static GeneratedCode gcInstance ;
	
	private GeneratedCode(){}
	
	/**
	 * Singleton
	 * @return
	 */
	public static synchronized GeneratedCode getGcInstance() {
		if( null == gcInstance ){
			gcInstance = new GeneratedCode();
		}
		return gcInstance;
	}

	/**
	 * synchronized 同步的静态方法(under Singleton Pattern)
	 * @return new SimpleDateFormat("yyMMddHHmmssSSS") 15位
	 * @throws RuntimeException
	 */
	public static synchronized Long getPrimayKeyCode() throws RuntimeException {
		try {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");	//15位
			return new Long(df.format(new Date()));
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
