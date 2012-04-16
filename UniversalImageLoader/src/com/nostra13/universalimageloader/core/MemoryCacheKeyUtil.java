package com.nostra13.universalimageloader.core;

import java.util.Comparator;

/**
 * Utility for generating of keys for memory cache and key comparing
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
class MemoryCacheKeyUtil {

	private static final String ID_AND_SIZE_SEPARATOR = "_";
	private static final String MEMORY_CACHE_KEY_FORMAT = "%s" + ID_AND_SIZE_SEPARATOR + "%sx%s";

	static String generateKey(String id, ImageSize targetSize) {
		return String.format(MEMORY_CACHE_KEY_FORMAT, id, targetSize.width, targetSize.height);
	}

	static Comparator<String> createFuzzyKeyComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String key1, String key2) {
				String imageUrl1 = key1.substring(0, key1.lastIndexOf(ID_AND_SIZE_SEPARATOR));
				String imageUrl2 = key2.substring(0, key2.lastIndexOf(ID_AND_SIZE_SEPARATOR));
				return imageUrl1.compareTo(imageUrl2);
			}
		};
	}
}
