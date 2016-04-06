package com.lsl.graduation.net.parser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lsl.graduation.net.context.BitmapContext;


/**
	 * 默认的ImageDisplayer
	 * @author jialg
	 * 
	 */
	public class DefaultImageDisplayer implements BitmapContext.ImageDisplayer {
		private final Drawable mDefaultDrawable;
		private final Drawable mErrorDrawable;
		private final ImageView img;

		public DefaultImageDisplayer(BitmapContext context, ImageView img) {
			this.mDefaultDrawable = context.getDefaultImage();
			this.mErrorDrawable = context.getErrorImage();
			this.img = img;
		}

		@Override
		public void prepare(BitmapContext context) {
			if(mDefaultDrawable!=null){
				img.setImageDrawable(mDefaultDrawable);
			}/*else{
				img.setVisibility(View.INVISIBLE);
			}*/
			if(context.getDefaultImageView()!=null){
				if(context.getAnimation()!=null){
					context.getDefaultImageView().startAnimation(context.getAnimation());
				}
			}
		}

		@Override
		public void display(BitmapContext context) {
			if(context.getDefaultImageView()!=null&&context.getDefaultImageView().getAnimation()!=null){
				context.getDefaultImageView().getAnimation().cancel();
            }
			if (context.getResult() == null) {//||bmp.isRecycled()) {
				img.setImageDrawable(mDefaultDrawable);
			}
			else {
				img.setImageBitmap(context.getResult());
				img.setVisibility(View.VISIBLE);
			}
		}

        @Override
        public void display(BitmapContext context, Context ctx) {
        	if(context.getDefaultImageView()!=null&&context.getDefaultImageView().getAnimation()!=null){
				context.getDefaultImageView().getAnimation().cancel();
            }
            if (context.getResult() == null) {//||bmp.isRecycled()) {
                img.setImageDrawable(mDefaultDrawable);
            }
            else {
                img.setImageBitmap(context.getResult());
                img.setVisibility(View.VISIBLE);
                if (null != ctx) {
                		//执行一个淡入动画
                        Animation fadeInAnimation = AnimationUtils.loadAnimation(ctx, ctx.getResources().getIdentifier("fade_in", "anim", ctx.getPackageName()));
                        img.startAnimation(fadeInAnimation);
                }
            }
        }

		@Override
		public void fail(BitmapContext context) {
			if(img.getAnimation()!=null){
            	img.getAnimation().cancel();
            }
			if(mErrorDrawable != null)
				img.setImageDrawable(mErrorDrawable);
		}
	}
