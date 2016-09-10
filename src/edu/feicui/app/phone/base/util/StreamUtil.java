package edu.feicui.app.phone.base.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtil {

	/**
	 * 从输入流中获取数�?	 * 
	 * @param inStream
	 *            输入�?	 * @return
	 * @throws Exception
	 */
	public static byte[] readStreamToByte(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 从输入流中获取数�?	 * 
	 * @param inStream
	 *            输入�?	 * @return
	 * @throws Exception
	 */
	public static String readStreamToString(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toString();
	}

	/**
	 * 将输入流转化成某字符编码的String
	 * 
	 * @param inStream
	 *            输入�?	 * @param encoding
	 *            编码
	 * @return
	 * @throws Exception
	 */
	public static String readStreamToString(InputStream inStream, String encoding) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return new String(outStream.toByteArray(), encoding);
	}

}
