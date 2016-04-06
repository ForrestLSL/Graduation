package com.lsl.graduation.net.parser;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class StringParser implements Parser<String> {

	@Override
	public String parse(byte[] data) throws ParseException {
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

}
