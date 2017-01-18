package org.profit.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * This is a string replacement method.
	 */
	public static String replaceString(String source, String oldStr, String newStr) {
		StringBuffer sb = new StringBuffer(source.length());
		int sind = 0;
		int cind = 0;
		while ((cind = source.indexOf(oldStr, sind)) != -1) {
			sb.append(source.substring(sind, cind));
			sb.append(newStr);
			sind = cind + oldStr.length();
		}
		sb.append(source.substring(sind));
		return sb.toString();
	}

	/**
	 * Replace string
	 */
	public static String replaceString(String source, Object[] args) {
		int startIndex = 0;
		int openIndex = source.indexOf('{', startIndex);
		if (openIndex == -1) {
			return source;
		}

		int closeIndex = source.indexOf('}', startIndex);
		if ((closeIndex == -1) || (openIndex > closeIndex)) {
			return source;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(source.substring(startIndex, openIndex));
		while (true) {
			String intStr = source.substring(openIndex + 1, closeIndex);
			int index = Integer.parseInt(intStr);
			sb.append(args[index]);

			startIndex = closeIndex + 1;
			openIndex = source.indexOf('{', startIndex);
			if (openIndex == -1) {
				sb.append(source.substring(startIndex));
				break;
			}

			closeIndex = source.indexOf('}', startIndex);
			if ((closeIndex == -1) || (openIndex > closeIndex)) {
				sb.append(source.substring(startIndex));
				break;
			}
			sb.append(source.substring(startIndex, openIndex));
		}
		return sb.toString();
	}

	/**
	 * Replace string.
	 */
	public static String replaceString(String source, Map<String, Object> args) {
		int startIndex = 0;
		int openIndex = source.indexOf('{', startIndex);
		if (openIndex == -1) {
			return source;
		}

		int closeIndex = source.indexOf('}', startIndex);
		if ((closeIndex == -1) || (openIndex > closeIndex)) {
			return source;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(source.substring(startIndex, openIndex));
		while (true) {
			String key = source.substring(openIndex + 1, closeIndex);
			Object val = args.get(key);
			if (val != null) {
				sb.append(val);
			}

			startIndex = closeIndex + 1;
			openIndex = source.indexOf('{', startIndex);
			if (openIndex == -1) {
				sb.append(source.substring(startIndex));
				break;
			}

			closeIndex = source.indexOf('}', startIndex);
			if ((closeIndex == -1) || (openIndex > closeIndex)) {
				sb.append(source.substring(startIndex));
				break;
			}
			sb.append(source.substring(startIndex, openIndex));
		}
		return sb.toString();
	}

	/**
	 * This method is used to insert HTML block dynamically
	 *
	 * @param source        the HTML code to be processes
	 * @param bReplaceNl    if true '\n' will be replaced by <br>
	 * @param bReplaceTag   if true '<' will be replaced by &lt; and
	 *                      '>' will be replaced by &gt;
	 * @param bReplaceQuote if true '\"' will be replaced by &quot;
	 */
	public static String formatHtml(String source, boolean bReplaceNl, boolean bReplaceTag, boolean bReplaceQuote) {
		StringBuffer sb = new StringBuffer();
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char c = source.charAt(i);
			switch (c) {
				case '\"':
					if (bReplaceQuote)
						sb.append("&quot;");
					else
						sb.append(c);
					break;
				case '<':
					if (bReplaceTag)
						sb.append("&lt;");
					else
						sb.append(c);
					break;
				case '>':
					if (bReplaceTag)
						sb.append("&gt;");
					else
						sb.append(c);
					break;
				case '\n':
					if (bReplaceNl) {
						if (bReplaceTag)
							sb.append("&lt;br&gt;");
						else
							sb.append("<br>");
					} else {
						sb.append(c);
					}
					break;
				case '\r':
					break;
				case '&':
					sb.append("&amp;");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}

	/**
	 * Pad string object
	 */
	public static String pad(String src, char padChar, boolean rightPad, int totalLength) {
		int srcLength = src.length();
		if (srcLength >= totalLength) {
			return src;
		}

		int padLength = totalLength - srcLength;
		StringBuffer sb = new StringBuffer(padLength);
		for (int i = 0; i < padLength; ++i) {
			sb.append(padChar);
		}

		if (rightPad) {
			return src + sb.toString();
		} else {
			return sb.toString() + src;
		}
	}

	/**
	 * Get hex string from byte array
	 */
	public static String toHexString(byte[] res) {
		StringBuffer sb = new StringBuffer(res.length << 1);
		for (int i = 0; i < res.length; i++) {
			String digit = Integer.toHexString(0xFF & res[i]);
			if (digit.length() == 1) {
				digit = '0' + digit;
			}
			sb.append(digit);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * Get byte array from hex string
	 */
	public static byte[] toByteArray(String hexString) {
		int arrLength = hexString.length() >> 1;
		byte buff[] = new byte[arrLength];
		for (int i = 0; i < arrLength; i++) {
			int index = i << 1;
			String digit = hexString.substring(index, index + 2);
			buff[i] = (byte) Integer.parseInt(digit, 16);
		}
		return buff;
	}

	/**
	 * Filter the space tab etc...
	 *
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * Checks if a CharSequence is empty ("") or null.
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Checks if the CharSequence contains only Unicode digits.
	 * A decimal point is not a Unicode digit and returns false.
	 *
	 * @param cs
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		final int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}
