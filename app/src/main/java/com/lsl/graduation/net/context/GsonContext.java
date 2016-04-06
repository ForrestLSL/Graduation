package com.lsl.graduation.net.context;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.lsl.graduation.net.loadlistener.LoadListener;
import com.lsl.graduation.net.parser.GsonParser;
import com.lsl.graduation.net.parser.Parser;


public class GsonContext<Result> extends LoadContext<Result> {
	
	private Class<Result> clazz;
	
	public GsonContext(Context context){
		flag = GsonContext.FLAG_HTTP_FIRST;
		ctx = context;
	}
	
	public GsonContext(Class<Result> clazz){
		flag = GsonContext.FLAG_HTTP_FIRST;
		this.clazz = clazz;
		this.parser = new GsonParser<Result>(clazz);
	}

	public Class<Result> getClazz() {
		return clazz;
	}

	public GsonContext<Result> clazz(Class<Result> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	@Override
	public GsonContext<Result> get(String url) {
		super.get(url);
		return this;
	}
	
	@Override
	public GsonContext<Result> post(String url) {
		super.post(url);
		return this;
	}
	@Override
	public GsonContext<Result> put(String url) {
		super.put(url);
		return this;
	}
	
	@Override
	public GsonContext<Result> param(String key, String value) {
		super.param(key, value);
		return this;
	}
	
	@Override
	public GsonContext<Result> flag(int flag) {
		super.flag(flag);
		return this;
	}
	
	@Override
	public GsonContext<Result> listener(LoadListener<Result> listener) {
		super.listener(listener);
		return this;
	}
	
	@Override
	public GsonContext<Result> dialog(Dialog dialog) {
		super.dialog(dialog);
		return this;
	}

	@Override
	public GsonContext<Result> parser(Parser<Result> parser) {
		super.parser(parser);
		return this;
	}
	
	@Override
	public GsonContext<Result> ctx(Activity context) {
		this.ctx = context;
		return this;
	}

	
	@Override
	public void load() {
		if(parser == null && clazz != null){
			parser = new GsonParser<Result>(clazz);
		}
		if(this.method == HTTP_LOAD_METHOD.HTTP_METHOD_POST){
			this.flag = FLAG_HTTP_ONLY;
		}
		super.load();
	}
}
