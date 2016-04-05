package com.lsl.graduation.http.parser;

import java.text.ParseException;

/**
 * 将字符串解析转化为java对象
 * @author 13leaf
 *
 * @param <T>
 */
public interface Parser<T> {
	
	/**
	 * 执行解析
	 */
	T parse(byte[] data) throws ParseException;

}
