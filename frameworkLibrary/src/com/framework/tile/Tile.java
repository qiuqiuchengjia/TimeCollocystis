package com.framework.tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

public class Tile {

	private int left;
	private int top;
	private int width;
	private int height;
	// private ImageView imageView;
	private Bitmap bitmap;


	public Tile() {
	}

	public Tile(int l, int t, int w, int h) {
		left = l;
		top = t;
		width = w;
		height = h;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}

	public Bitmap decode(Context context, TileCache cache,String fileName, BitmapDecoder decoder) {
		
		if (cache != null) {
			Bitmap cached = cache.getBitmap(fileName);
			if (cached != null) {
				bitmap = cached;
				return bitmap;
			}
		}
		bitmap = decoder.decode(fileName, context);
		if (cache != null) {
			cache.addBitmap(fileName, bitmap);
		}
		return bitmap;
	}

	public void destroy() {
		bitmap = null;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tile) {
			Tile m = (Tile) o;
			return (m.getLeft() == getLeft()) && (m.getTop() == getTop())
					&& (m.getWidth() == getWidth())
					&& (m.getHeight() == getHeight());
		}
		return false;
	}

	@Override
	public String toString() {
		return "(left=" + left + ", top=" + top + ", width=" + width
				+ ", height=" + height + ", file="  + ")";
	}

}
