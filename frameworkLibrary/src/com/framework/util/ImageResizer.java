/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import com.framework.BuildConfig;
import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

/**
 * A simple subclass of {@link ImageWorker} that resizes images from resources
 * given a target width and height. Useful for when the input images might be
 * too large to simply load directly into memory.
 */
public class ImageResizer {
	private static final String LOGTAG = LogUtil.makeLogTag(ImageResizer.class);
	protected int mImageWidth;
	protected int mImageHeight;

	/**
	 * Initialize providing a single target image size (used for both width and
	 * height);
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageResizer(Context context, int imageWidth, int imageHeight) {
		setImageSize(imageWidth, imageHeight);
	}

	/**
	 * Initialize providing a single target image size (used for both width and
	 * height);
	 * 
	 * @param context
	 * @param imageSize
	 */
	public ImageResizer(Context context, int imageSize) {
		setImageSize(imageSize);
	}

	/**
	 * Set the target image width and height.
	 * 
	 * @param width
	 * @param height
	 */
	public void setImageSize(int width, int height) {
		mImageWidth = width;
		mImageHeight = height;
	}

	/**
	 * Set the target image size (width and height will be the same).
	 * 
	 * @param size
	 */
	public void setImageSize(int size) {
		setImageSize(size, size);
	}

	/**
	 * The main processing method. This happens in a background task. In this
	 * case we are just sampling down the bitmap and returning it from a
	 * resource.
	 * 
	 * @param resId
	 * @return
	 */
	private Bitmap processBitmap(Resources res, int resId) {
		if (BuildConfig.DEBUG) {
			Log.d(LOGTAG, "processBitmap - " + resId);
		}
		return decodeSampledBitmapFromResource(res, resId, mImageWidth,
				mImageHeight);
	}

	/**
	 * Decode and sample down a bitmap from resources to the requested width and
	 * height.
	 * 
	 * @param res
	 *            The resources object containing the image data
	 * @param resId
	 *            The resource id of the image data
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with
	 *            inBitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// If we're running on Honeycomb or newer, try to use inBitmap
		// if (Utils.hasHoneycomb()) {
		// addInBitmapOptions(options, cache);
		// }
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and
	 * height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with
	 *            inBitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// If we're running on Honeycomb or newer, try to use inBitmap
		// if (Utils.hasHoneycomb()) {
		// addInBitmapOptions(options, cache);
		// }
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * Decode and sample down a bitmap from a file input stream to the requested
	 * width and height.
	 * 
	 * @param fileDescriptor
	 *            The file descriptor to read from
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with
	 *            inBitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		// If we're running on Honeycomb or newer, try to use inBitmap
		// if (Utils.hasHoneycomb()) {
		// addInBitmapOptions(options, cache);
		// }

		return BitmapFactory
				.decodeFileDescriptor(fileDescriptor, null, options);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static void addInBitmapOptions(BitmapFactory.Options options) {
		// inBitmap only works with mutable bitmaps so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true;

		// if (cache != null) {
		// // Try and find a bitmap to use for inBitmap
		// Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
		//
		// if (inBitmap != null) {
		// options.inBitmap = inBitmap;
		// }
		// }
	}

	/**
	 * Calculate an inSampleSize for use in a
	 * {@link android.graphics.BitmapFactory.Options} object when decoding
	 * bitmaps using the decode* methods from
	 * {@link android.graphics.BitmapFactory}. This implementation calculates
	 * the closest inSampleSize that is a power of 2 and will result in the
	 * final decoded bitmap having a width and height equal to or larger than
	 * the requested width and height.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run
	 *            through a decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			long totalPixels = width * height / inSampleSize;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final long totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels > totalReqPixelsCap) {
				inSampleSize *= 2;
				totalPixels /= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * 保存图片
	 * 
	 * @param imageName
	 *            图片名称
	 * @param path
	 *            图片路径
	 * @param bitmap
	 * @return
	 */
	public static void saveImageData(String imageName, String path,
			Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		File fdir = new File(path);
		if (!fdir.exists()) {
			fdir.mkdirs();
		}
		File file = new File(path, imageName);
		if (!file.exists()) {
			byte[] bytes = baos.toByteArray();
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				try {
					fos.write(bytes);
					if (fos != null) {
						fos.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
						fos = null;
					}
					if (baos != null) {
						baos.close();
						baos = null;
					}
					if (!bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
					bytes = null;
					System.gc();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			DebugLog.i(LOGTAG, "file not exites ");

		}

	}

	/**
	 * 读取本地图片资源
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getLocationFileForPicture(String filePath) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			File file = new File(filePath);
			FileInputStream in = new FileInputStream(file);
			options.inJustDecodeBounds = false;
			options.inMutable = true;
			bitmap = BitmapFactory.decodeStream(in, null, options);
			// DebugLog.d(LOGTAG, "bitMap:" + bitmap.getByteCount());
		} catch (OutOfMemoryError o) {
			System.gc();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		} catch (Exception e) {
			// DebugLog.showToastRunnable(MakePaperActivity.this,
			// e.getMessage());
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 内存优化 加载本地资源图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	private static Bitmap readBitMap(byte[] bytes) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		ByteArrayInputStream is = new ByteArrayInputStream(bytes, 0,
				bytes.length);
		if (bytes != null) {
			bytes = null;
		}
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * compress bitmap
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapCompressBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static long sizeOf(Bitmap data) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			return data.getRowBytes() * data.getHeight();
		} else {
			return data.getByteCount();
		}
	}

	/**
	 * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		if (returnValue > 1)
			return (returnValue + "MB");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		return (returnValue + "KB");
	}

	/**
	 * 图片变灰
	 * 
	 * @param bitmap
	 * @return
	 */
	public static final Bitmap grey(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(faceIconGreyBitmap);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return faceIconGreyBitmap;
	}

}