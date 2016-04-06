package com.lsl.graduation.net.parser;

import java.text.ParseException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;

public class BitmapParser implements Parser<Bitmap> {

	private final static Config mDefaultDecodeConfig = Config.RGB_565;
	private final Config mDecodeConfig;
	private final int mMaxWidth;
	private final int mMaxHeight;

	public BitmapParser() {
		this(0, 0, mDefaultDecodeConfig);
	}
	
	public BitmapParser(int maxWidth, int maxHeight) {
		this(maxWidth, maxHeight, mDefaultDecodeConfig);
	}

	public BitmapParser(int maxWidth, int maxHeight, Config decodeConfig) {
		mMaxWidth = maxWidth;
		mMaxHeight = maxHeight;
		mDecodeConfig = decodeConfig;
	}

	@Override
	public Bitmap parse(byte[] data) throws ParseException {
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if (mMaxWidth == 0 && mMaxHeight == 0) {
			decodeOptions.inPreferredConfig = mDecodeConfig;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					decodeOptions);
		} else {
			// If we have to resize this image, first get the natural bounds.
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			// Then compute the dimensions we would ideally like to decode to.
			int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
					actualWidth, actualHeight);
			int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
					actualHeight, actualWidth);

			// Decode to the nearest power of two scaling factor.
			decodeOptions.inJustDecodeBounds = false;
			// support it?
			// decodeOptions.inPreferQualityOverSpeed =
			// PREFER_QUALITY_OVER_SPEED;
			decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
					actualHeight, desiredWidth, desiredHeight);
			Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length, decodeOptions);

			// If necessary, scale down to the maximal acceptable size.
			if (tempBitmap != null
					&& (tempBitmap.getWidth() > desiredWidth || tempBitmap
							.getHeight() > desiredHeight)) {
				bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
						desiredHeight, true);
				tempBitmap.recycle();
			} else {
				bitmap = tempBitmap;
			}
		}
		return bitmap;
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary,
			int actualPrimary, int actualSecondary) {
		// If no dominant value at all, just return the actual.
		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}

		// If primary is unspecified, scale primary to match secondary's scaling
		// ratio.
		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}

		return (int) n;
	}

}
