package com.lsl.graduation.http.context;


import com.lsl.graduation.http.loadlistener.LoadListener;
import com.lsl.graduation.http.parser.StringParser;

public class StringContext extends LoadContext<String>{
	
	public StringContext(){
		this.parser = new StringParser();
	}
	
	public StringContext(String url){
		this.url = url;
		this.parser = new StringParser();
	}
	
	@Override
	public StringContext get(String url) {
		super.get(url);
		return this;
	}
	
	@Override
	public StringContext post(String url) {
		super.post(url);
		return this;
	}
	
	@Override
	public StringContext param(String key, String value) {
		super.param(key, value);
		return this;
	}
	
	@Override
	public StringContext flag(int flag) {
		super.flag(flag);
		return this;
	}
	
	@Override
	public StringContext listener(LoadListener<String> listener) {
		super.listener(listener);
		return this;
	}

}
