package com.lsl.graduation.http.loadlistener;

import android.graphics.Bitmap;

import com.lsl.graduation.http.context.BitmapContext;
import com.lsl.graduation.http.context.LoadContext;


/**
 * 图片加载的默认LoadListener
 *
 */
	
public class BitmaploadListener implements LoadListener<Bitmap>{
	
	private final BitmapContext.ImageDisplayer mDisplayer;
	
	
	public BitmaploadListener(BitmapContext.ImageDisplayer displayer){
		this.mDisplayer = displayer;
	}

	@Override
	public void preExecut(LoadContext<Bitmap> context) {
		mDisplayer.prepare((BitmapContext) context);
	}

	@Override
	public void postExecut(LoadContext<Bitmap> context) {
	}

	@Override
	public void loadComplete(LoadContext<Bitmap> context) {
		mDisplayer.display((BitmapContext) context);
	}

	@Override
	public void loadFail(LoadContext<Bitmap> context) {
		mDisplayer.fail((BitmapContext) context);
	}

}
