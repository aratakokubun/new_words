package com.kkbnart.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.res.AssetManager;

public class FileIOUtils {
	// Default encoding
	private static final String DEFAULT_ENCORDING = "UTF-8";
	// Amount of bytes to be read at one time
	private static final int DEFAULT_READ_LENGTH = 8192;
	
	/**
	 * Read file from assets folder for Android. <br>
	 * 
	 * @param filePath	File path inside assets folder
	 * @param context	Android activity context
	 * @return	File contents text
	 * @throws IOException	Exception occurred while file IO
	 */
	public static final String loadTextAsset(final String filePath, final Context context) throws IOException {
	    final AssetManager assetManager = context.getAssets();
	    InputStream is = assetManager.open(filePath);
	    return loadText(is, DEFAULT_ENCORDING);
	}
	
	/**
	 * Read file from assets folder for Android. <br>
	 * 
	 * @param filePath	File path inside assets folder
	 * @param context	Android activity context
	 * @return	File contents byte array
	 * @throws IOException	Exception occurred while file IO
	 */
	public static final byte[] loadByteAsset(final String filePath, final Context context) throws IOException {
	    final AssetManager assetManager = context.getAssets();
	    InputStream is = assetManager.open(filePath);
	    return readStream(is, DEFAULT_READ_LENGTH);
	}

	/**
	 * Return encoded text from file input stream. <br>
	 * 
	 * @param inputStream	File input stream
	 * @param charsetName	Char encoding 
	 * @return File encoded test
	 * @throws IOException 	Exception occurred while file IO
	 * @throws UnsupportedEncodingException	Encoding format error
	 */
	public static final String loadText(final InputStream inputStream, final String charsetName) 
	    throws IOException, UnsupportedEncodingException {
	    return new String(readStream(inputStream, DEFAULT_READ_LENGTH), charsetName);
	}
	
	/**
	 * Return byte array from file input stream. <br>
	 * 
	 * @param inputStream	File input stream
	 * @param readLength	Byte length to be read at one time
	 * @return File byte array
	 * @throws IOException	Exception occurred while file IO
	 */
	public static final byte[] readStream(final InputStream inputStream, final int readLength) throws IOException {
		// Temporary buffer
	    final ByteArrayOutputStream byteStream = new ByteArrayOutputStream(readLength);
	    // Buffer for one read length
	    final byte[] bytes = new byte[readLength];
	    final BufferedInputStream bis = new BufferedInputStream(inputStream, readLength);

	    try {
	        int len = 0;
	        while ((len = bis.read(bytes, 0, readLength)) > 0) {
	        	// Store to stream buffer
	            byteStream.write(bytes, 0, len);
	        }
	        return byteStream.toByteArray();
	    } finally {
	        try {
	        	// Throw away all data
	            byteStream.reset();
	            // Close input stream
	            bis.close();
	        } catch (Exception e) {
	            // Can not handle exception
	        }
	    }
	}
}
