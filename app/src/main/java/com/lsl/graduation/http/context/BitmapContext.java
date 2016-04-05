package com.lsl.graduation.http.context;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lsl.graduation.http.loadlistener.BitmaploadListener;
import com.lsl.graduation.http.loadlistener.LoadListener;
import com.lsl.graduation.http.parser.BitmapParser;
import com.lsl.graduation.http.parser.DefaultImageDisplayer;
import com.lsl.graduation.http.parser.Parser;
import com.lsl.graduation.utils.MLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BitmapContext extends LoadContext<Bitmap> {
	
	protected Drawable defaultImage;
	protected Drawable errorImage;
	protected ImageView imageView;
	protected ImageView defaultImageView;
	protected ImageView errorImageView;
	protected ImageDisplayer displayer;
	protected Animation animation;
	
	
	public BitmapContext(){
	}
	
	public BitmapContext(ImageView imageView){
		this(imageView, 0 ,0);
	}
	
	public BitmapContext(ImageView imageView, int maxWidth, int maxHeight){
		this.imageView = imageView;
		this.parser = new BitmapParser(maxWidth, maxHeight);
	}
	
	public Drawable getDefaultImage() {
		return defaultImage;
	}

	public BitmapContext defaultImage(Drawable defaultImage) {
		this.defaultImage = defaultImage;
		return this;
	}

	public Drawable getErrorImage() {
		return errorImage;
	}

	public BitmapContext errorImage(Drawable errorImage) {
		this.errorImage = errorImage;
		return this;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public BitmapContext ImageView(ImageView imageView) {
		this.imageView = imageView;
		return this;
	}
	public ImageView getDefaultImageView() {
		return defaultImageView;
	}
	
	public BitmapContext defaultImageView(ImageView defaultImageView) {
		this.defaultImageView = defaultImageView;
		return this;
	}
	public ImageView getErrorImageView() {
		return errorImageView;
	}
	
	public BitmapContext errorImageView(ImageView errorImageView) {
		this.errorImageView = errorImageView;
		return this;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public BitmapContext animation(Animation animation) {
		this.animation = animation;
		return this;
	}

	public ImageDisplayer getDisplayer() {
		return displayer;
	}

	public BitmapContext displayer(ImageDisplayer displayer) {
		this.displayer = displayer;
		return this;
	}
	
	@Override
	public BitmapContext get(String url) {
		super.get(url);
		return this;
	}
	
	@Override
	public BitmapContext post(String url) {
		super.post(url);
		return this;
	}
	
	@Override
	public BitmapContext param(String key, String value) {
		super.param(key, value);
		return this;
	}
	
	@Override
	public BitmapContext flag(int flag) {
		super.flag(flag);
		return this;
	}
	
	@Override
	public BitmapContext listener(LoadListener<Bitmap> listener) {
		super.listener(listener);
		return this;
	}

	@Override
	public BitmapContext parser(Parser<Bitmap> parser) {
		super.parser(parser);
		return this;
	}
	
	
	
	@Override
	public void load() {
		if(imageView == null){
			MLog.w( "this context lost param:  'imageView'");
			return;
		}
		if(parser == null){
			parser = new BitmapParser();
		}
		if(displayer == null&&listener == null){
			this.displayer = new DefaultImageDisplayer(this, this.imageView);
		}
		if(this.listener == null){
			this.listener = new BitmaploadListener(displayer);
		}
		BitmapDisplayListener displayListener = new BitmapDisplayListener(this);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(defaultImage)
		.showImageOnFail(defaultImage)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		ImageLoader.getInstance().displayImage(url, imageView, options, displayListener);
	}
	
	private static class BitmapDisplayListener extends SimpleImageLoadingListener {
		
		BitmapContext context;
		
		public BitmapDisplayListener (BitmapContext context){
		this.context = context;	
		}
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
//			super.onLoadingStarted(imageUri, view);
			if(context.listener!=null){
				context.listener.preExecut(context);
			}
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
//			super.onLoadingFailed(imageUri, view, failReason);
			if(context.listener!=null){
				context.listener.loadComplete(context);
			}
		}
		
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
//			super.onLoadingComplete(imageUri, view, loadedImage);
			if(context.listener!=null){
				context.setResult(loadedImage);
				context.listener.loadComplete(context);
			}
		}
	}

	public interface ImageDisplayer {
		/**
		 * 预设默认图片
		 */
		void prepare(BitmapContext context);

		/**
		 * 图片成功回调
		 * @param
		 */
		void display(BitmapContext context);
		
		void display(BitmapContext context, Context ctx);
		
		/**
		 * 图片失败回调
		 */
		void fail(BitmapContext context);
	}
	
}
