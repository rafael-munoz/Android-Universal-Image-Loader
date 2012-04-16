package com.nostra13.universalimageloader.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.nostra13.universalimageloader.utils.StreamUtils;

/**
 * Decodes images to {@link Bitmap}
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
final class ImageDecoder {

	private Object source;

	private ImageSize targetSize;
	private DecodingType decodingType;

	/**
	 * @param InputStream
	 *            Image InputStream
	 * @param targetImageSize
	 *            Image size to scale to during decoding
	 * @param decodingType
	 *            {@link DecodingType Decoding type}
	 */
	ImageDecoder(Object source, ImageSize targetImageSize, DecodingType decodingType) {
		this.source = source;
		this.targetSize = targetImageSize;
		this.decodingType = decodingType;
	}

	/**
	 * Decodes image from InputStream into {@link Bitmap}. Image is scaled close
	 * to incoming {@link ImageSize image size} during decoding. Initial image
	 * size is reduced by the power of 2 (according Android recommendations)
	 * 
	 * Note: the image InputStream is closed after this method execution.
	 * 
	 * @return Decoded bitmap
	 * @throws IOException
	 */
	public Bitmap decodeFile() throws IOException {

		Bitmap result;
		if (source instanceof URL) {
			Options decodeOptions = getBitmapOptionsForImageDecoding(((URL) source).openStream());
			InputStream is = ((URL) source).openStream();
			try {
				result = BitmapFactory.decodeStream(is, null, decodeOptions);
			} finally {
				is.close();
			}
		} else if (source instanceof Bitmap) {
			Bitmap bitmap = ((Bitmap) source);
			Options decodeOptions = getBitmapOptionsForImageDecoding(StreamUtils.getInputStreamFromBitmap(bitmap));
			InputStream is = StreamUtils.getInputStreamFromBitmap(bitmap);
			try {
				result = BitmapFactory.decodeStream(is, null, decodeOptions);
			} finally {
				is.close();
			}
		} else {
			InputStream is = ((InputStream) source);
			try {
				result = BitmapFactory.decodeStream(is);
			} finally {
				is.close();
			}
		}
		return result;
	}

	private Options getBitmapOptionsForImageDecoding(InputStream imageStream) throws IOException {
		Options options = new Options();
		try {
			options.inSampleSize = computeImageScale(imageStream);
		} finally {
			imageStream.close();
		}
		return options;
	}

	private int computeImageScale(InputStream imageStream) {
		int width = targetSize.width;
		int height = targetSize.height;

		// decode image size
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(imageStream, null, options);

		int scale = 1;
		switch (decodingType) {
		default:
		case FAST:
			// Find the correct scale value. It should be the power of 2.
			int width_tmp = options.outWidth;
			int height_tmp = options.outHeight;

			while (true) {
				if (width_tmp / 2 < width || height_tmp / 2 < height)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			break;
		case MEMORY_SAVING:
			int widthScale = (int) (Math.floor(((double) options.outWidth) / width));
			int heightScale = (int) (Math.floor(((double) options.outHeight) / height));
			int minScale = Math.min(widthScale, heightScale);
			if (minScale > 1) {
				scale = minScale;
			}
			break;
		}

		return scale;
	}
}
