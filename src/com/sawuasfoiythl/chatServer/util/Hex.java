package com.sawuasfoiythl.chatServer.util;

public class Hex {

	public static String convertHexToString (byte[] hexValue) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < hexValue.length; j++) {
			buf.append(hexDigit[(hexValue[j] >> 4) & 0x0f]);
			buf.append(hexDigit[hexValue[j] & 0x0f]);
		}
		return buf.toString();
	}

	public static int convertStringToHex (String hexValue) {
		return Integer.parseInt(hexValue, 16);
	}

	public static boolean isABiggerThanB(String A, String B) {

		if (convertStringToHex(A) > convertStringToHex(B)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isBBiggerThanA(String A, String B) {

		if (convertStringToHex(B) > convertStringToHex(A)) {
			return true;
		} else {
			return false;
		}

	}

}
