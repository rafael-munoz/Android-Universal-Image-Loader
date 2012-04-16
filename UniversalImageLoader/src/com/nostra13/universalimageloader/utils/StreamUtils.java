package com.nostra13.universalimageloader.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;

public class StreamUtils {
	
	public static InputStream getInputStreamFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] imageInByte = stream.toByteArray();
		return new ByteArrayInputStream(imageInByte);
	}

}
