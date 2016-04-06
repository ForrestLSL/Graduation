package com.lsl.graduation.net.parser;

import java.lang.reflect.Type;
import java.text.ParseException;

import android.util.Log;

import com.google.gson.Gson;

public class GsonParser<T> implements Parser<T> {
	private Gson gson = new Gson();
	private Class<? extends T> clazz;
	private Type type;

	public GsonParser(Class<? extends T> beanClass) {
		clazz = beanClass;
	}
	
	public GsonParser(Type type) {
		this.type = type;
	}

	@Override
	public T parse(byte[] data) throws ParseException {
		try {
			String s = new String(data, "UTF-8");
			Log.i("Graduation", s);
			if(clazz == null&&type != null){
				return gson.fromJson(s, type);
			}
			
			T result = gson.fromJson(s, clazz);
			return result;
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}
}
