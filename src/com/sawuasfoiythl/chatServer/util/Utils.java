package com.sawuasfoiythl.chatServer.util;

public class Utils {

	//This is just a class for lots of stuffs

	public static String stringInRange(String fullText, String charFirst, String charLast) {

		String finalText = "";

		try {

			if (fullText.contains(charFirst) && fullText.contains(charLast)) {

				int beginning = fullText.indexOf(charFirst);
				int ending = fullText.indexOf(charLast);

				finalText = fullText.substring(beginning+1, ending);

			}

			


		} catch (Exception e) {}
		return finalText;
	}





}
