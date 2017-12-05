package com.framework.tile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapDecoderDisk implements BitmapDecoder {

	private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
	static {
		OPTIONS.inDither = false; // Disable Dithering mode
		OPTIONS.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		OPTIONS.inInputShareable = true; // Which kind of reference will be used
											// to recover the Bitmap data after
											// being clear, when it will be used
											// in the future
		OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	@Override
	public Bitmap decode(String fileName, Context context) {
		try {
			File file = new File(fileName);
			FileInputStream input = new FileInputStream(file);
			if (input != null) {
				try {
					Bitmap bit = BitmapFactory.decodeStream(input, null,
							OPTIONS);
					input.close();
					input = null;
					return bit;
				} catch (OutOfMemoryError oom) {
					// oom - you can try sleeping (this method won't be called
					// in the UI thread) or try again (or give up)
				} catch (Exception e) {
					// unknown error decoding bitmap
				}
			}
		} catch (IOException io) {
			// io error - probably can't find the file
		} catch (Exception e) {
			// unknown error opening the asset
		}
		return null;
	}

}
