package com.sawuasfoiythl.chatServer.client;
/**
 *    The following Code is hereby copyrighted by Myles Lumsden, so FUCK OFF JAMIE, COCO, GAVIN, ANDIE, and everyone else
 *    No seriously Jamie this has been a lot of hard work so don't fucking cunt just leave this code alone, 
 *    I'm sorry the code you want is in another class. (Login.class as that hold all login data e.g. passwords and usernames)
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.bind.DatatypeConverter;

import com.sawuasfoiythl.chatServer.networking.In;
import com.sawuasfoiythl.chatServer.networking.Out;
import com.sawuasfoiythl.chatServer.resources.ResourceLoader;
import com.sawuasfoiythl.chatServer.updater.Updater;
import com.sawuasfoiythl.chatServer.util.Utils;

public class ChatClient extends JFrame implements ActionListener {

	public static String permissions = "";
	private boolean loggingHasStarted = false;
	private int dupeCheck = 0;

	public static String version = "09";
	public static String trueVersion = "Alpha v8";

	public static String screenName;
	private static String prefix;

	private boolean useDisguise;
	private String disguiseName;

	public static boolean SA = false;
	public static boolean Admin = false;
	public static boolean Developer = false;

	public ArrayList<String> onlineList = new ArrayList<String>();
	public ArrayList<String> ipList = new ArrayList<String>();

	//Different effects for client
	public boolean pyroVision = false;
	public boolean punishedEyes = false;
	public boolean mute = false;
	public boolean blind = false;

	//Private Message stuff
	public boolean isAboutToPM = false;
	public String PMto = "";

	public int timesSworen = 0;
	public int timesSpamming = 0;
	public String previousMessage = "";


	public static Color background = Color.WHITE;
	public static Color foreground = Color.BLACK;



	// GUI stuff
	private JEditorPane  enteredText = new JEditorPane();
	private JTextField typedText   = new JTextField(32);

	// socket for connection to chat server
	private Socket socket;
	private Socket updateSocket;

	// for writing to and reading from the server
	private Out out;
	private In in;
	
	private String hostName;

	public ChatClient(final String screenName, String hostName) {

		this.hostName = hostName;

		disguiseName = screenName;
		// connect to server
		try {
			socket = new Socket(hostName, serverPort);
			out    = new Out(socket);
			in     = new In(socket);

			out.println("Client running at :" + version);
			out.println(prefix + screenName + " @ " + socket.getLocalAddress() + " has joined the chat");	


			/*
			if(useDisguise == true){
				out.println(prefix + disguiseName + " has joined the chat");
			}else{
				out.println(prefix + screenName + " has joined the chat");	
			}
			 */
			//out.println(screenName + " has joined as " + socket.getInetAddress().getHostName());
		}
		catch (Exception ex) { ex.printStackTrace(); }
		this.screenName = screenName;
		out.println(/**prefix +*/ screenName + " @ " + socket.getLocalAddress() + " has joined the chat");	




		// close output stream  - this will cause listen() to stop and exit
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {

						out.println(screenName + " @ " + socket.getLocalAddress() + " has left the chat");


						out.close();

						//System.exit(-1);
						//                    in.close();
						//                    try                   { socket.close();        }
						//                    catch (Exception ioe) { ioe.printStackTrace(); }
					}
				}
				);


		HyperlinkListener hll = new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		};


		enteredText.addHyperlinkListener(hll);


		// create GUI stuff
		enteredText.setEditorKit(new HTMLEditorKit());

		enteredText.setEditable(false);	


		DefaultCaret caret = (DefaultCaret)enteredText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);




		enteredText.setText("<html><b><font color=blue>{Developer} </font></b></html>");
		System.out.println(enteredText.getText());
		System.out.println(enteredText.getText().substring(0, 40)
				+ "/nhey brother/n"
				+ enteredText.getText().substring(enteredText.getText().length() - 18, enteredText.getText().length()));

		String x = enteredText.getText().substring(0, 40)
				+ ""+htmlNewLine+"hey brother"+htmlNewLine+""
				+ enteredText.getText().substring(enteredText.getText().length() - 18, enteredText.getText().length());

		enteredText.setText(x);

		System.out.println(enteredText.getText());

		//enteredText.setBackground(background);
		//enteredText.setForeground(foreground);



		typedText.addActionListener(this);

		/**
		Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
		Font font = new Font("Arial", Font.BOLD, 13);
		enteredText.setFont(font);
		typedText.setFont(font1);
		 **/

		typedText.setBorder (BorderFactory.createLineBorder (Color.DARK_GRAY, 3));
		typedText.setBackground(background);
		typedText.setForeground(foreground);

		Container content = getContentPane();


		JScrollPane pane = new JScrollPane(enteredText);
		pane.setBorder (BorderFactory.createLineBorder (Color.BLACK, 2));

		content.add( pane, BorderLayout.CENTER);
		content.add(typedText, BorderLayout.SOUTH);



		// display the window, with focus on typing box
		setTitle("Chat Client " + trueVersion /**+ ": { " + prefix + " } [" + screenName + "]"*/);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		setSize(500, 500);
		//pack();
		typedText.requestFocusInWindow();
		setVisible(true);


	}


	// process TextField after user hits Enter
	public void actionPerformed(ActionEvent e) {
		String messageCheckSwearing = /*checkForSwearing(*/typedText.getText();
		/*);*/
		/**
		 * Next few lines changed for revised url stuff
		 */
		//messageCheckSwearing = checkForSwearingBETA(messageCheckSwearing);
		checkForHtml(messageCheckSwearing);
		messageCheckSwearing = textToHtmlConvertingURLsToLinks(messageCheckSwearing);

		//	System.err.println(messageCheckSwearing);
		if (Developer == false ) checkForSpamming(messageCheckSwearing);
		/**
		 * Next two lines changed for revised url stuff
		 */
		//checkForHtml(messageCheckSwearing);
		//messageCheckSwearing = textToHtmlConvertingURLsToLinks(messageCheckSwearing);
		/**
		 * dont think it matters
		 *
		for (int i = 0; i < typedText.getText().length(); i++) {
		 */
		messageCheckSwearing = checkForSwearingBETA(messageCheckSwearing);
		/**
		 * 

		}
		 */

		if(checkForCommand(typedText.getText()) == false){
			if(useDisguise == true){
				out.println((Developer ? " " + disguiseName + " : " : "[" + disguiseName + "]: ") + messageCheckSwearing);
			} else {
				/*	
				if(Developer){	out.println("{Developer}" + "[" + screenName + "]: " + messageCheckSwearing); }else
				if(SA){	out.println("{SuperAdmin}" + "[" + screenName + "]: " + messageCheckSwearing); }else
				if(Admin){	out.println("{Admin}" + "[" + screenName + "]: " + messageCheckSwearing); } else
				{out.println("[" + screenName + "]: " + messageCheckSwearing);	}	
				 */





				if(serverSpeaks == true && Developer == true)
				{

					out.println( rainbow ? useSine(messageCheckSwearing, rainbowFrequency)/*makeRainbow(messageCheckSwearing)*/ : (color + messageCheckSwearing));	
					serverSpeaks = false;
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"<b><font color=red>You have returned to normal chat.</font></b>"+htmlNewLine+"" + htmlSuf);


				} else {


					out.println((prefixpresent ? prefix : "")
							+ (namePresent ?  (Developer ? " " + screenName + " : " : "[" + screenName + "]: ") : " ")
							+ (rainbow ? useSine(messageCheckSwearing, rainbowFrequency)/*makeRainbow(messageCheckSwearing)*/ : (color + messageCheckSwearing)));	

				}






			}}
		/**
		if(typedText.getText().equals( messageCheckSwearing))
		{
			out.println("[" + screenName + "]: " + messageCheckSwearing);
		}else {
			out.println("[" + screenName + "]: " + messageCheckSwearing);
		}
		 */


		typedText.setText("");
		typedText.requestFocusInWindow();
	}

	public void checkForHtml(String s) {

		if((s.contains("<") || s.contains(">")) && (Developer == false && SA == false))
		{
			enteredText.setText("<font size=12><b><font color=red>STOP IT NOW!!!!!!!!!!<br /></b></font> <font color=Blue><br />Ending Client communications <br /> lol you will now ban yourself");

			if((Developer == false || SA == false) && loggingHasStarted) {
				out.println("<br /><br />An <font color=Blue>Admin</font> or user has decided to muck around... <font color=ref><u>the outcome wasn't <b>very</b> good");
				out.println("THE SUPER ADMIN has kicked " + screenName);	
				closeConnection(false);

			}
		}	
	}

	public static String textToHtmlConvertingURLsToLinks(String text) {
		if (text == null) {
			return text;
		}

		return text.replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
				"$1<a href=\"$2\">$2</a>$4");
	}


	public String makeRainbow(String s)
	{

		int colorpos=0;
		String[] partials = new String[3000];
		int i=0;



		for(i=0; i < s.length()-1; i++){


			partials[i] = rainbowArray[colorpos] + s.charAt(i);

			if(s.charAt(i) != ' ') colorpos++;


			if(colorpos>=7) colorpos = colorpos - 7; 

		}

		String rebuilt = "";
		//Rebuild
		for(int p=0; p < i;p++){
			rebuilt = rebuilt + partials[p];

		}
		return rebuilt;
	}

	public static String useSine(String s, double fre)
	{

		String[] partials = new String[3000];


		double frequency = fre;

		for (int i=0;i < s.length(); i++)
		{
			double red = Math.sin(frequency * i + 0) * (255/2) + (255/2);
			double green = Math.sin(frequency * i + 2) * (255/2) + (255/2);
			double blue = Math.sin(frequency * i + 4) * (255/2) + (255/2);

			partials[i] = "<font color=" + rgbToColor((byte)red, (byte)green, (byte)blue) + ">" + s.charAt(i);


		}

		String rebuilt = "";
		//Rebuild
		for(int p=0; p < s.length();p++){
			rebuilt = rebuilt + partials[p];

		}
		return rebuilt;
	}


	public static String byteToHex(byte i){
		byte[] bytes = {i};
		return DatatypeConverter.printHexBinary(bytes);	
	}

	public static String rgbToColor(byte r, byte g, byte b){	
		return "#" + byteToHex(r) + byteToHex(g) + byteToHex(b);
	}


	public boolean checkForCommand(String s)
	{
		String temp1 = "";
		String temp2 = "";
		String[] temp = s.split(" ", 2);
		try{
			temp1 = temp[0];
			temp2 = temp[1];
		} catch (IndexOutOfBoundsException e){}

		if(temp1.toLowerCase().startsWith("/me"))
		{
			if(useDisguise == true){
				out.println("* " + disguiseName + " " + temp2);
			} else {
				out.println("* " + screenName + " " + temp2);
			}

			return true;
		}

		if(temp1.toLowerCase().startsWith("/help"))
		{
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + 
					(helpMessage +  htmlSuf));
			//

			return true;
		}
		if(temp1.toLowerCase().startsWith("/ahelp") && Admin == true)
		{
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + aHelpMessage + htmlSuf);
			//enteredText.setCaretPosition(enteredText.getText().length());

			return true;
		}

		if(temp1.toLowerCase().startsWith("/sahelp") && SA == true)
		{
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + saHelpMessage + 
					"" + htmlSuf);
			//enteredText.setCaretPosition(enteredText.getText().length());

			return true;
		}
		if(temp1.toLowerCase().startsWith("/dhelp") && Developer == true)
		{
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + "" + helpMessage +

					//Admin
					" <b>/a ban (PLAYER USERNAME)</b> - this will ban (PLAYER USERNAME) from the chat"+htmlNewLine+"\t" +
					" <b>/a pyro-vision (PLAYER USERNAME)</b> - this will enlighten (PLAYER USERNAME) to the true beauty of life(Repeat Command To Undo, Can be done to ADMINS!)"+htmlNewLine+"\t" +
					" <b>/a punish (PLAYER USERNAME)</b> - this will change(PLAYER USERNAME)'s fonts to Comic Sans MS (Repeat Command To Undo) "+htmlNewLine+"\t" +
					" <b>/a mute (PLAYER USERNAME)</b> - this will shut (PLAYER USERNAME) up!(Repeat Command To Undo)"+htmlNewLine+"\t" +
					" <b>/a blind (PLAYER USERNAME)</b> - this will change the colour scheme of (PLAYER USERNAME) to all black so they can't read anything(Repeat Command To Undo)"+htmlNewLine+"\t" +
					" <b>/a kick (PLAYER USERNAME)</b> - this will kick (PLAYER USERNAME)"+htmlNewLine+"\t" +

					" <b>/pm</b> to start a Private Message, then type message in next line. "+htmlNewLine+""+htmlNewLine+"\t  <b><font color=green>#SA commands</b></font>"+htmlNewLine+"\t" +

					//SA
					" <b>/sa ban (ADMIN USERNAME)</b> - this will ban (ADMIN USERNAME) from the chat"+htmlNewLine+"\t" +
					" <b>/sa pyro-vision (ADMIN USERNAME)</b> - this will enlighten (ADMIN USERNAME) to the true beauty of life(Repeat Command To Undo)"+htmlNewLine+"\t" +
					" <b>/sa punish  (ADMIN USERNAME)</b> - this will change(ADMIN USERNAME)'s fonts to Comic Sans MS (Repeat Command To Undo) "+htmlNewLine+"\t" +
					" <b>/sa mute (ADMIN USERNAME)</b> - this will shut (ADMIN USERNAME) up!(Repeat Command To Undo)"+htmlNewLine+"\t" +
					" <b>/sa blind (ADMIN USERNAME)</b> - this will change the colour scheme of (ADMIN USERNAME) to all black so they can't read anything(Repeat Command To Undo)"+htmlNewLine+"\t" +
					" <b>/sa reveal (ADMIN USERNAME)</b> - this will remove the disguise of (ADMIN USERNAME) "+htmlNewLine+"\t" +
					" <b>/sa kick (PLAYER USERNAME)</b> - this will kick (PLAYER USERNAME)"+htmlNewLine+"\t" +

					//Dev
					" <b>/d hackz-all (Number of times to repeat)</b> - to get you troll on "+htmlNewLine+"\t" +
					" <b>/hideprefix</b> - hides the dev prefix so you can just be a rainbow (again to toggle) "+htmlNewLine+"\t" +

					" <b>/whisper (Blach anything)</b> - this will this will say anything through the server "+htmlNewLine+"\t" +
					" <b>/d admin (PLAYER USERNAME)</b> - this will admin (PLAYER USERNAME)"+htmlNewLine+"\t" +
					" <b>/d deadmin (PLAYER USERNAME)</b> - this will deadmin (PLAYER USERNAME)"+htmlNewLine+"\t" +
					" <b>/d superadmin (PLAYER USERNAME)</b> - this will superadmin (PLAYER USERNAME)"+htmlNewLine+"\t" +
					" <b>/d desuperadmin (PLAYER USERNAME)</b> - this will desuperadmin (PLAYER USERNAME)"+htmlNewLine+"\t" +
					" <b>/d set <level> (password)</b> - sets password"+htmlNewLine+"\t" +
					" <b>/d genpass</b> - sets password"+htmlNewLine+"\t" +
					" <b>/push</b> - popup text"+htmlNewLine+"\t" +

					" <b><font color=red>/iplist</b></font> - this will show you the ips and names of those online"+htmlNewLine+"\t" +

					" <b><font color=red>/color (color/(or other html stuff))</b></font> - this will make you a pretty colour (Color)"+htmlNewLine+"\t" +
					" <b><font color=red>/rainbow (rainbows (double for the frequency) / null to disable)</b></font> - this will make you a pretty colour (Color)"+htmlNewLine+"\t" +
					" <b><font color=red>/hideprefix (color/rainbows)</b></font> - this will hide your prefix ()"+htmlNewLine+"\t" +
					" <b><font color=red>/hidename ()</b></font> - this will hide your name ()"+htmlNewLine+"\t" +
					" <b><font color=red>/iplist ()</b></font> - this will display all ips ()"+htmlNewLine+"\t" +
					" <b><font color=red>/online ()</b></font> - this will display all names ()"+htmlNewLine+"\t" +
					" <b><font color=red>/sm ()</b></font> - this will make you talk through the server ()"+htmlNewLine+"\t" +

					" <b><font color=red>/boo (PLAYER USERNAME)</b></font> - this will present (PLAYER USERNAME) with a lovely image"+htmlNewLine+"\t" +
					" <b><font color=red>/nyan (PLAYER USERNAME)</b></font> - this will present (PLAYER USERNAME) with a lovely image"+htmlNewLine+"\t" +

					" <b><font color=green>/s (clearlogs / clearall / end / message)</b></font> - server command"+htmlNewLine+"\t" +


					""+htmlNewLine+""+htmlNewLine+"" +
					"" + htmlSuf);
			//enteredText.setCaretPosition(enteredText.getText().length());

			return true;
		}

		if(temp1.toLowerCase().equals("@dev") && new File("ServerProperties.txt").exists()) {
			out.println("@: DEVCLIENT");
		}


		if(temp1.toLowerCase().startsWith("/uc")) {

			typedText.setEditable(true);
			enteredText.setBackground(background);
			enteredText.setForeground(foreground);

			Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
			Font font = new Font("Arial", Font.BOLD, 13);
			enteredText.setFont(font);
			typedText.setFont(font1);

			typedText.setBorder (BorderFactory.createLineBorder (Color.DARK_GRAY, 3));
			typedText.setBackground(background);
			typedText.setForeground(foreground);

			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"<b><font color=red>COLOURS SHOULD BE UPDATED</font></b>"+htmlNewLine+"" + htmlSuf);

			return true;

		}


		if(temp1.toLowerCase().equals("/red") || 
				temp1.toLowerCase().equals("/green") || 
				temp1.toLowerCase().equals("/blue") || 
				temp1.toLowerCase().equals("/orange") || 
				temp1.toLowerCase().equals("/purple") || 
				temp1.toLowerCase().equals("/yellow") || 
				temp1.toLowerCase().equals("/black") || 
				temp1.toLowerCase().equals("/white") || 
				temp1.toLowerCase().equals("/gray")) {
			messageColor(s);
			return true;
		}

		if(temp1.toLowerCase().equals("/bold") || 
				temp1.toLowerCase().equals("/italic") || 
				(temp1.toLowerCase().equals("/super") && Admin) || 
				(temp1.toLowerCase().equals("/sub") && Admin)) {
			messageFormatting(s);
			return true;
		}


		if(temp1.toLowerCase().startsWith("/a") && Admin == true)
		{
			String string[] = temp2.split(" ", 2);
			String s1 = string[0];
			String s2 = string[1];

			if(s1.equals("ban"))
			{
				/**
				//for all connected
				if (s2.toLowerCase().equals("onlinelist")) {
					for (int i=0; i < onlineList.size();i++) {
						for (int j=0; j < 4; j++)

							if ( onlineList.get(i).contains(prefixes[j])) {
								s2 = onlineList.get(i).substring(prefixes[j].length(), onlineList.get(i).length());
								out.println("THE ADMIN bans " + s2);
								return true;
								//stuff but replace s2 with onlineList.get(i)
							}

					}
				} else 
					//what used to happen
				 * 
				 */
				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE ADMIN bans ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE ADMIN bans ", false);
				} else

					out.println("THE ADMIN bans " + s2);
				return true;
			}
			if(s1.equals("pyro-vision"))
			{
				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE ADMIN gives you the vision of the Pyro ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE ADMIN gives you the vision of the Pyro ", false);
				} else



					out.println("THE ADMIN gives you the vision of the Pyro " + s2);
				return true;
			}
			if(s1.equals("punish"))
			{

				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE ADMIN punishes your eyes ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE ADMIN punishes your eyes ", false);
				} else


					out.println("THE ADMIN punishes your eyes " + s2);
				return true;
			}
			if(s1.equals("mute"))
			{
				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE ADMIN fed your tongue to cats ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE ADMIN fed your tongue to cats ", false);
				} else


					out.println("THE ADMIN fed your tongue to cats " + s2);
				return true;
			}
			if(s1.equals("blind"))
			{
				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE ADMIN removed your ears ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE ADMIN removed your ears ", false);
				} else


					out.println("THE ADMIN removed your ears " + s2);
				return true;
			}
			if(s1.equals("kick"))
			{

				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE ADMIN has kicked ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE ADMIN has kicked ", false);
				} else


					out.println("THE ADMIN has kicked " + s2);
				return true;
			}
		}
		if(temp1.toLowerCase().startsWith("/sa") && SA == true)
		{
			String string[] = temp2.split(" ", 2);
			String s1 = string[0];
			String s2 = string[1];

			if(s1.equals("ban"))
			{


				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN bans ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN bans ", false);
				} else

					/**

				//for all connected
				if (s2.toLowerCase().equals("onlinelist")) {
					for (int i=0; i < onlineList.size();i++) {
						for (int j=0; j < 4; j++)

						if ( onlineList.get(i).contains(prefixes[j])) {
							s2 = onlineList.get(i).substring(prefixes[j].length(), onlineList.get(i).length());
							out.println("THE SUPER ADMIN bans " + s2);

							//stuff but replace s2 with onlineList.get(i)
						}

					}
				} else
					 */

					out.println("THE SUPER ADMIN bans " + s2);
				return true;
			}
			if(s1.equals("pardon"))
			{


				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN pardons ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN pardons ", false);
				} else

					out.println("THE SUPER ADMIN pardons " + s2);
				return true;
			}
			if(s1.equals("pyro-vision"))
			{

				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN gives you the vision of the Pyro ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN gives you the vision of the Pyro ", false);
				} else


					out.println("THE SUPER ADMIN gives you the vision of the Pyro " + s2);
				return true;
			}
			if(s1.equals("punish"))
			{
				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN punishes your eyes ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN punishes your eyes ", false);
				} else


					out.println("THE SUPER ADMIN punishes your eyes " + s2);
				return true;
			}
			if(s1.equals("mute"))
			{

				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN fed your tongue to cats ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN fed your tongue to cats ", false);
				} else

					out.println("THE SUPER ADMIN fed your tongue to cats " + s2);
				return true;
			}
			if(s1.equals("blind"))
			{

				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN removed your ears ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN removed your ears ", false);
				} else

					out.println("THE SUPER ADMIN removed your ears " + s2);
				return true;
			}
			if(s1.equals("reveal"))
			{
				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN unmasked you ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN unmasked you ", false);
				} else

					out.println("THE SUPER ADMIN unmasked you " + s2);
				return true;
			}
			if(s1.equals("kick"))
			{

				if (s2.toLowerCase().equals("onlinelist")) {
					multiCommand("THE SUPER ADMIN has kicked ", true);
				} else if (s2.toLowerCase().equals("iplist")) {
					multiCommand("THE SUPER ADMIN has kicked ", false);
				} else

					out.println("THE SUPER ADMIN has kicked " + s2);
				return true;
			}

		}




		if(temp1.toLowerCase().startsWith("/disguise") && Admin == true)
		{
			useDisguise = !useDisguise;

			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\tDisguise = " + useDisguise + ""+htmlNewLine+"" + htmlSuf);
			//enteredText.setCaretPosition(enteredText.getText().length());

			return true;
		}

		if(temp1.toLowerCase().startsWith("/setdisguise") && Admin == true)
		{
			disguiseName = temp2;

			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\tYou are now disguised as: " + temp2 + ""+htmlNewLine+"" + htmlSuf);
			//enteredText.setCaretPosition(enteredText.getText().length());

			return true;
		}


		if(temp1.toLowerCase().startsWith("/pm"))
		{
			isAboutToPM = !isAboutToPM;
			PMto = temp2;
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\tPM to : " + temp2 + " : Please enter your message."+htmlNewLine+"" + htmlSuf);


			return true;
		}

		if(isAboutToPM == true)
		{
			isAboutToPM = false;
			out.println("PM [" + (useDisguise ? disguiseName : screenName) + "] -> [" + PMto + "]: " + temp1 + " " + temp2);

			return true;
		}


		if (temp1.toLowerCase().startsWith("/online")) {

			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + 
					(""+htmlNewLine+" 	- <b><font color=red>ONLINE LIST</b></font> - "+htmlNewLine+ htmlNewLine + htmlSuf));


			for (int i=0;i<onlineList.size();i++) {

				enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + 
						(" - " + onlineList.get(i) + " - " + htmlNewLine + htmlSuf));
			}



			return true;
		}

		if (temp1.toLowerCase().startsWith("/iplist") && Developer == true) {

			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + 
					(""+htmlNewLine+" 	- <b><font color=red>ONLINE LIST [IPs]</b></font> - "+htmlNewLine+ htmlNewLine + htmlSuf));


			for (int i=0;i<onlineList.size();i++) {

				enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + 
						(" - " + onlineList.get(i) + " @ " + ipList.get(i) + " - " + htmlNewLine + htmlSuf));
			}



			return true;
		}




		if(temp1.toLowerCase().startsWith("/d") && Developer == true)
		{

			try{
				String string[] = temp2.split(" ", 2);
				String s1 = string[0];
				String s2 = string[1];

				String[] pass = new String[2];

				if(s1.toLowerCase().equals("hackz-all"))
				{

					out.println("Developerz Be Trolling " + s2);


				}


				if(s1.toLowerCase().equals("admin"))
				{
					if (s2.toLowerCase().equals("onlinelist")) {
						multiCommand("THE DEVELOPER re-admins ", true);
					} else if (s2.toLowerCase().equals("iplist")) {
						multiCommand("THE DEVELOPER re-admins ", false);
					} else

						out.println("THE DEVELOPER re-admins " + s2);


				}if(s1.toLowerCase().equals("deadmin"))
				{

					if (s2.toLowerCase().equals("onlinelist")) {
						multiCommand("THE DEVELOPER de-admins ", true);
					} else if (s2.toLowerCase().equals("iplist")) {
						multiCommand("THE DEVELOPER de-admins ", false);
					} else

						out.println("THE DEVELOPER de-admins " + s2);


				}if(s1.toLowerCase().equals("superadmin"))
				{

					if (s2.toLowerCase().equals("onlinelist")) {
						multiCommand("THE DEVELOPER super-admins ", true);
					} else if (s2.toLowerCase().equals("iplist")) {
						multiCommand("THE DEVELOPER super-admins ", false);
					} else

						out.println("THE DEVELOPER super-admins " + s2);


				}if(s1.toLowerCase().equals("desuperadmin"))
				{

					if (s2.toLowerCase().equals("onlinelist")) {
						multiCommand("THE DEVELOPER de-super-admins ", true);
					} else if (s2.toLowerCase().equals("iplist")) {
						multiCommand("THE DEVELOPER de-super-admins ", false);
					} else

						out.println("THE DEVELOPER de-super-admins " + s2);


				}


				if(s1.toLowerCase().equals("set"))
				{
					String[] split = s2.split(" ", 2);
					String prt1 = split[0];
					String prt2 = split[1];

					if (prt1.equalsIgnoreCase("admin")) {
						pass[0] = prt2;
						out.println("@: PASS " + prt1 + ":"+pass[0]);
					}
					if (prt1.equalsIgnoreCase("superadmin")) {
						pass[0] = prt2;
						out.println("@: PASS " + prt1 + ":"+pass[0]);
					}
					if (prt1.equalsIgnoreCase("dev")) {
						pass[0] = prt2;
						out.println("@: PASS " + prt1 + ":"+pass[0]);
					}
					/**
					 * 
					 * TODO: finish the password setting, need to do serverside aswell
					 * 		 Work out how to save to the server properties for reboots, etc.
					 * 		 add popup / alternative loggin method for dev client
					 * 
					 */

				}

				if(s1.toLowerCase().equals("genpass"))
				{
					out.println("@: PASS GEN");

				}


			} catch (Exception e){
				e.printStackTrace();
			}

			return true;
		}

		if (temp1.toLowerCase().startsWith("/push") && Developer == true) {

			String string[] = temp2.split(" ", 2);
			String s1 = string[0];
			String s2 = string[1];

			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + 
					(""+htmlNewLine+" 	- <b><font color=red>Pushing Notification</b></font> - "+htmlNewLine + htmlSuf));

			if (s1.toLowerCase().equals("onlinelist")) {
				multiCommand("PUSH:" + screenName + "- " + s2 +":", true);
			} else if (s1.toLowerCase().equals("iplist")) {
				multiCommand("PUSH:" + screenName + "- " + s2 + ":", false);
			} else

				out.println("PUSH:[" + screenName + "] - " + s2 + ":" + s1);


			return true;
		}


		if(temp1.toLowerCase().startsWith("/s") && Developer == true)
		{

			try{
				String string[] = temp2.split(" ", 2);
				String s1 = string[0];


				if(s1.toLowerCase().equals("clearlogs"))
				{
					out.println("<font color=green>SERVER: Clearing Logs...");
				}

				if(s1.toLowerCase().equals("clearall"))
				{
					out.println("<font color=green>SERVER: Clearing ALL");
				}

				if(s1.toLowerCase().equals("end"))
				{
					out.println("<font color=green>SERVER: Server will now close...");
					out.println("<font color=green>SERVER: Closing");
				}

				if(s1.toLowerCase().equals("restart"))
				{
					out.println("<font color=green>SERVER: Server will now restart...");
					out.println("<font color=green>SERVER: Restart your clients to reconnect.");
					out.println("<font color=green>SERVER: Restarting");
				}

				if(s1.toLowerCase().equals("message"))
				{

					serverSpeaks = !serverSpeaks;
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"<b><font color=red>Please enter your message for the server to say</font></b>"+htmlNewLine+"" + htmlSuf);

				}


			} catch (Exception e){
				e.printStackTrace();
			}

			return true;
		}





		if(temp1.toLowerCase().startsWith("/color") && Developer == true)
		{

			try{
				String string[] = temp2.split(" ", 1);
				String s1 = string[0];

				color = colorpre + s1 + colorsuf;
				enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + "<b><i><font color=purple>Your default text will now be </font>" + color + s1 + "</font></i></b>" + ""+htmlNewLine+"" + htmlSuf);

				if(s1.toLowerCase().equals("rainbow")){
					rainbow = !rainbow;	
					rainbowFrequency = 0.5;
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + "<b><i><font color=purple>Rainbow effect:</font>" + rainbow + "</font></i></b>" + ""+htmlNewLine+"" + htmlSuf);

				} else {
					rainbow = false;
				}

			} catch (Exception e){
				e.printStackTrace();
			}

			return true;
		}


		if(temp1.toLowerCase().startsWith("/rainbow") && Developer == true)
		{

			try{
				String string[] = temp2.split(" ", 1);
				String s1 = string[0];


				rainbow = true;	
				rainbowFrequency = Double.parseDouble(s1);

				//rainbow = !rainbow;	
				//rainbowFrequency = rainbow ? Double.parseDouble(s1) : 0.5;

			} catch (Exception e){
				e.printStackTrace();
				rainbow = false;
				rainbowFrequency = 0.5;

			}
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + "<b><i><font color=purple>Rainbow effect:</font>" + rainbow + "</font></i></b>" + ""+htmlNewLine+"" + htmlSuf);
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + "<b><i><font color=purple>Rainbow frequency:</font>" + rainbowFrequency + "</font></i></b>" + ""+htmlNewLine+"" + htmlSuf);


			return true;
		}





		if(temp1.toLowerCase().startsWith("/whisper") && Developer == true)
		{
			out.println(temp2);

			return true;
		}

		if(temp1.toLowerCase().startsWith("/boo") && Developer == true)
		{

			//enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"<b><font color=green>Prefix Present: " + prefixpresent + "</font></b>"+htmlNewLine+"" + htmlSuf);


			if (temp2.toLowerCase().equals("onlinelist")) {
				multiCommand("Boo! ", true);
			} else if (temp2.toLowerCase().equals("iplist")) {
				multiCommand("Boo! ", false);
			} else


				out.println("Boo! " + temp2);

			return true;
		}
		if(temp1.toLowerCase().startsWith("/nyan") && Developer == true)
		{

			if (temp2.toLowerCase().equals("onlinelist")) {
				multiCommand("Nyan! ", true);
			} else if (temp2.toLowerCase().equals("iplist")) {
				multiCommand("Nyan! ", false);
			} else

				out.println("Nyan! " + temp2);

			return true;
		}

		if(temp1.toLowerCase().startsWith("/hideprefix") && Developer == true)
		{
			prefixpresent = !prefixpresent;
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"<b><font color=green>Prefix Present: " + prefixpresent + "</font></b>"+htmlNewLine+"" + htmlSuf);

			return true;
		}
		if(temp1.toLowerCase().startsWith("/hidename") && Developer == true)
		{
			namePresent = !namePresent;
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"<b><font color=green>Name Present: " + namePresent + "</font></b>"+htmlNewLine+"" + htmlSuf);


			return true;
		}


		if(temp1.toLowerCase().startsWith("/"))
		{
			enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\t LOL idiot that is not a command so check the help message by typing: /help "+htmlNewLine+"" + htmlSuf);
			//enteredText.setCaretPosition(enteredText.getText().length());

			return true;
		}


		return false;
	}


	public void messageColor(String s) {
		String[] array = s.split(" ",2);
		String s1 = array[0];
		String s2 = array[1];

		String colour = s1.substring(1, s1.length());
		out.println((prefixpresent ? prefix : "")
				+ (namePresent ?  (Developer ? " " + screenName + " : " : "[" + screenName + "]: ") : " ") 
				+ colorpre + colour + colorsuf + checkForSwearingBETA(s2));
	}

	public void messageFormatting(String s) {
		String[] array = s.split(" ",2);
		String s1 = array[0];
		String s2 = array[1];
		String[] format={"", ""};

		if (s1.toLowerCase().equals("/italic")) {format[0] = ("<i>"); format[1] = ("</i>");}
		if (s1.toLowerCase().equals("/bold")) {format[0] = ("<b>"); format[1] = ("</b>");}
		if (s1.toLowerCase().equals("/super")) {format[0] = ("<sup>"); format[1] = ("</sup>");}
		if (s1.toLowerCase().equals("/sub")) {format[0] = ("<sub>"); format[1] = ("</sub>");}
		out.println((prefixpresent ? prefix : "")
				+ (namePresent ?  (Developer ? " " + screenName + " : " : "[" + screenName + "]: ") : " ") 
				+ format[0] + checkForSwearingBETA(s2) + format[1]);
	}

	private static final String[] swearWords = {"fuck" , "fuk", "wanker", "shit", "shite", "slag", "slut", "cunt", "bitch", "sob", "dick", "wank", "whore"};
	public String checkForSwearingBETA(String s) {

		String message = s;
		for (int loops = 0; loops < message.length(); loops++)
		for (int i = 0; i < swearWords.length; i++) {

			String replacementWord = "" + swearWords[i].charAt(0);
			for (int j = 0; j < swearWords[i].length()-1; j++) {
				replacementWord = replacementWord + "*";
			}
			if (message.toLowerCase().contains(swearWords[i]) && !(s.contains("<") || s.contains(">"))) {
				message = message.substring(0, message.toLowerCase().indexOf(swearWords[i])) 
						+ replacementWord +
						message.substring(message.indexOf("", message.toLowerCase().indexOf(swearWords[i]) + swearWords[i].length()), message.length());
				timesSworen++;
			}
		}
		
		if(timesSworen > 4)
		{
			if(useDisguise == true){
				out.println(disguiseName + " has been kicked for swearing :(");
				timesSworen = 0;

				enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\tYou are no longer disguised to protect you form suspicion "+htmlNewLine+""+htmlNewLine+"" + htmlSuf);
				//enteredText.setCaretPosition(enteredText.getText().length());
				useDisguise = false;


			} else {
				out.println(screenName + " has been kicked for swearing :(");
				closeConnection(true);
			}
			//			if(useDisguise == false)
			//				closeConnection();
		}
		return message;
	}

	public String checkForSwearing(String s)
	{
		String message = s;

		String[] temp = s.split(" ");
		ArrayList<String> messagelist = new ArrayList<String>(Arrays.asList(temp));

		String[] forCheck = s.split(" ");
		int i = 0;
		for(String e : temp)
		{

			forCheck[i] = e.toLowerCase();
			//	System.out.println(e + " : " + e.toLowerCase());
			i++;
		}

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(forCheck));
		//list = (ArrayList<String>);


		if(list.contains("fuck"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("fuck");
			messagelist.set(temporary, " (A Naughty word) ");
			timesSworen++;
		}
		if(list.contains("shit"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("shit");
			messagelist.set(temporary, " (A Naughty word) ");
			timesSworen++;
		}
		if(list.contains("bitch"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("bitch");
			messagelist.set(temporary, " (A Naughty word) ");
			timesSworen++;
		}
		if(list.contains("bastard"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("bastard");
			messagelist.set(temporary, " (A Naughty word) ");
			timesSworen++;
		}
		if(list.contains("ass"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("ass");
			messagelist.set(temporary, " (A Naughty word) ");
			timesSworen++;
		}
		if(list.contains("asshole"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("asshole");
			messagelist.set(temporary, " a****** ");
			timesSworen++;
		}


		/**
		 * VERY VERY BAD WORD
		 */
		if(list.contains("cunt"))
		{
			System.out.println("FOUND BAD WORD");
			int temporary = list.indexOf("cunt");
			messagelist.set(temporary, " (The worst word of all) ");
			timesSworen++;
		}

		/*
		if(list.contains("fuck".toLowerCase()))
		{
			int temporary = list.indexOf("fuck".toLowerCase());
			messagelist.set(temporary, "(A Naughty word)");
			timesSworen++;
		}
		 */


		System.err.println(timesSworen);

		if(timesSworen > 4)
		{
			if(useDisguise == true){
				out.println(disguiseName + " has been kicked for swearing :(");
				timesSworen = 0;

				enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\tYou are no longer disguised to protect you form suspicion "+htmlNewLine+""+htmlNewLine+"" + htmlSuf);
				//enteredText.setCaretPosition(enteredText.getText().length());
				useDisguise = false;


			} else {
				out.println(screenName + " has been kicked for swearing :(");
				closeConnection(true);
			}
			//			if(useDisguise == false)
			//				closeConnection();
		}

		StringBuilder builder = new StringBuilder();
		for(String a : messagelist) {
			builder.append(a + " ");
		}

		message = builder.toString();

		return message;
	}

	public void checkForSpamming(String s)
	{
		if(s.equalsIgnoreCase(previousMessage))
		{
			timesSpamming++;
		}

		System.err.println(timesSpamming);
		previousMessage = s;

		if(timesSpamming > 2)
		{
			if(useDisguise == true){
				out.println(disguiseName + " has been kicked for spamming the chat :(");
				timesSpamming = 0;

				enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ""+htmlNewLine+"\tYou are no longer disguised to protect you form suspicion "+htmlNewLine+""+htmlNewLine+"" + htmlSuf);
				//enteredText.setCaretPosition(enteredText.getText().length());
				useDisguise = false;


			} else {
				out.println(screenName + " has been kicked for spamming the chat :(");
				closeConnection(true);
			}

			//			if(useDisguise == false)
			//				closeConnection();
		}


		//return s;


	}

	public void closeConnection(boolean flag)
	{
		out.println( screenName  + " @ " + socket.getLocalAddress() +" has left the chat");

		in.close();
		out.close();
		try {
			socket.close();
			//Thread.sleep(1000);
		} catch (IOException e) { e.printStackTrace();
		//} catch (InterruptedException e) { e.printStackTrace();
		}

		if(flag == true)
			System.exit(-1);
	}

	public void checkForCommandUsed(String s)
	{
		if( Developer == false )
		{

			if(((s.equals("THE ADMIN has kicked " + screenName) || s.equals("THE SUPER ADMIN has kicked " + screenName)) || (s.equals("THE ADMIN has kicked " + disguiseName) || s.equals("THE SUPER ADMIN has kicked " + disguiseName)) || s.equals("THE SUPER ADMIN has kicked " + socket.getLocalAddress()) || (s.equals("THE ADMIN has kicked " + socket.getLocalAddress()))) && loggingHasStarted == true)
			{
				System.out.println(""+htmlNewLine+" has been kicked");

				closeConnection(false);



			}
			if (s.startsWith("PUSH:") && loggingHasStarted == true) {
				String[] array = s.split(":", 3);
				String a2 = array[1];
				String a3 = array[2];
				if((((a3.equals(disguiseName) || a3.equals(disguiseName)) || a3.equals(socket.getLocalAddress()))) && loggingHasStarted == true)
				{
					JOptionPane option = new JOptionPane(a2);
					JDialog dialog1 = option.createDialog(null, "Chat Client");
					dialog1.setVisible(true);
					dialog1.pack();
					dialog1.toFront();
				}
			}
		}

		if( Developer == false )
		{

			if((s.equals("THE ADMIN gives you the vision of the Pyro " + screenName) || s.equals("THE SUPER ADMIN gives you the vision of the Pyro " + screenName)) || (s.equals("THE ADMIN gives you the vision of the Pyro " + disguiseName) || s.equals("THE SUPER ADMIN gives you the vision of the Pyro " + disguiseName) || s.equals("THE ADMIN gives you the vision of the Pyro " + socket.getLocalAddress()) || s.equals("THE SUPER ADMIN gives you the vision of the Pyro " + socket.getLocalAddress())) )
			{
				System.out.println(""+htmlNewLine+" pyroVision: " + pyroVision +
						""+htmlNewLine+" punishedEyes: " + punishedEyes +
						""+htmlNewLine+" mute: " + mute +
						""+htmlNewLine+" blind: " + blind
						);

				pyroVision = !pyroVision;
				blind = false;
				mute = false;

				if(pyroVision == true){
					Font font1 = new Font("Helvetica", Font.ITALIC + Font.BOLD, 14);
					Font font = new Font("Helvetica", Font.BOLD, 13);
					enteredText.setFont(font);
					typedText.setFont(font1);

					enteredText.setBackground(Color.YELLOW);
					enteredText.setForeground(Color.MAGENTA);


					typedText.setBackground(Color.YELLOW);
					typedText.setForeground(Color.MAGENTA);

					typedText.setBorder (BorderFactory.createLineBorder (Color.CYAN, 3));
				} else if(punishedEyes == false && pyroVision == false && blind == false && mute == false) {

					typedText.setEditable(true);
					enteredText.setBackground(background);
					enteredText.setForeground(foreground);

					Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
					Font font = new Font("Arial", Font.BOLD, 13);
					enteredText.setFont(font);
					typedText.setFont(font1);

					typedText.setBorder (BorderFactory.createLineBorder (Color.DARK_GRAY, 3));
					typedText.setBackground(background);
					typedText.setForeground(foreground);
				}
			}



			if(SA == false) {
				if(s.equals("THE SUPER ADMIN bans " + screenName) || s.equals("THE SUPER ADMIN bans " + disguiseName) || s.equals("THE SUPER ADMIN bans " + socket.getLocalAddress()))
				{
					if (loggingHasStarted)
						out.println("<font color=purple>"+ screenName + " will now only be allowed to join back after the server has restarted,...");
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + s + ""+htmlNewLine+"" + ""+htmlNewLine+""+htmlNewLine+" you will now only be allowed to join back after the server has restarted,..."+htmlNewLine+"" + htmlSuf);
					//enteredText.setCaretPosition(enteredText.getText().length());

					toBeBanned = true;
				}

				if(s.equals("THE SUPER ADMIN pardons " + screenName) || s.equals("THE SUPER ADMIN pardons " + disguiseName) || s.equals("THE SUPER ADMIN pardons " + socket.getLocalAddress()))
				{
					out.println("<font color=purple>"+screenName + " got lucky this time, but be careful you may not get so lucky next time");
					toBeBanned = false;
				}

				if(s.equals("THE ADMIN punishes your eyes " + screenName) || s.equals("THE ADMIN punishes your eyes " + disguiseName) || s.equals("THE ADMIN punishes your eyes " + socket.getLocalAddress()))
				{

					System.out.println(""+htmlNewLine+" pyroVision: " + pyroVision +
							""+htmlNewLine+" punishedEyes: " + punishedEyes +
							""+htmlNewLine+" mute: " + mute +
							""+htmlNewLine+" blind: " + blind
							);

					punishedEyes = !punishedEyes;
					blind = false;
					mute = false;

					if(punishedEyes == true) {
						Font font1 = new Font("Comic Sans MS", Font.ITALIC + Font.BOLD, 14);
						Font font = new Font("Comic Sans MS", Font.BOLD, 13);
						enteredText.setFont(font);
						typedText.setFont(font1);
					} else if(punishedEyes == false && pyroVision == false && blind == false && mute == false) {

						typedText.setEditable(true);
						enteredText.setBackground(background);
						enteredText.setForeground(foreground);

						Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
						Font font = new Font("Arial", Font.BOLD, 13);
						enteredText.setFont(font);
						typedText.setFont(font1);

						typedText.setBorder (BorderFactory.createLineBorder (Color.DARK_GRAY, 3));
						typedText.setBackground(background);
						typedText.setForeground(foreground);
					}
				}





				if(s.equals("THE ADMIN fed your tongue to cats " + screenName) || s.equals("THE ADMIN fed your tongue to cats " + disguiseName) || s.equals("THE ADMIN fed your tongue to cats " + socket.getLocalAddress()))
				{
					System.out.println(""+htmlNewLine+" pyroVision: " + pyroVision +
							""+htmlNewLine+" punishedEyes: " + punishedEyes +
							""+htmlNewLine+" mute: " + mute +
							""+htmlNewLine+" blind: " + blind
							);

					mute = !mute;

					if(mute == true){

						typedText.setEditable(false);

					} else if(punishedEyes == false && pyroVision == false && blind == false && mute == false) {

						typedText.setEditable(true);
						enteredText.setBackground(background);
						enteredText.setForeground(foreground);

						Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
						Font font = new Font("Arial", Font.BOLD, 13);
						enteredText.setFont(font);
						typedText.setFont(font1);

						typedText.setBorder (BorderFactory.createLineBorder (Color.DARK_GRAY, 3));
						typedText.setBackground(background);
						typedText.setForeground(foreground);
					}

					if (mute == false){

						typedText.setEditable(true);
					}
				}


				if(s.equals("THE ADMIN removed your ears " + screenName) || s.equals("THE ADMIN removed your ears " + disguiseName) || s.equals("THE ADMIN removed your ears " + socket.getLocalAddress()))
				{
					System.out.println(""+htmlNewLine+" pyroVision: " + pyroVision +
							""+htmlNewLine+" punishedEyes: " + punishedEyes +
							""+htmlNewLine+" mute: " + mute +
							""+htmlNewLine+" blind: " + blind
							);

					blind = !blind;
					pyroVision = false;
					mute = false;

					if(blind == true){
						Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
						Font font = new Font("Arial", Font.BOLD, 13);
						enteredText.setFont(font);
						typedText.setFont(font1);

						enteredText.setBackground(Color.BLACK);
						enteredText.setForeground(Color.BLACK);


						typedText.setBackground(Color.BLACK);
						typedText.setForeground(Color.BLACK);

						typedText.setBorder (BorderFactory.createLineBorder (Color.BLACK, 3));
					} else if(punishedEyes == false && pyroVision == false && blind == false && mute == false) {

						typedText.setEditable(true);
						enteredText.setBackground(background);
						enteredText.setForeground(foreground);

						Font font1 = new Font("Arial", Font.ITALIC + Font.BOLD, 14);
						Font font = new Font("Arial", Font.BOLD, 13);
						enteredText.setFont(font);
						typedText.setFont(font1);

						typedText.setBorder (BorderFactory.createLineBorder (Color.DARK_GRAY, 3));
						typedText.setBackground(background);
						typedText.setForeground(foreground);
					}
				}


				if(s.equals("THE ADMIN unmasked you " + screenName ) || s.equals("THE ADMIN unmasked you " + disguiseName ) || s.equals("THE ADMIN unmasked you " + socket.getLocalAddress() ))
				{
					useDisguise = false;
					out.println( screenName + " was hiding as " + disguiseName);
					disguiseName = screenName;

				}



				if(s.startsWith("Developerz Be Trolling" )  && loggingHasStarted == true)
				{
					String[] tmp = s.split(" ", 4);
					String trolls = tmp[3]; 
					int j = 0;
					try{
						j = Integer.parseInt(trolls);
					} catch (Exception e) {}
					for(int i = 0; i < j; i++){
						out.println("[" + screenName +  "] fbkl aoja12121g4141njafnagjnajkllagjklaljkag");
						out.println("[" + screenName +  "] fbkl aojagnjafs41511241f fndasffa adf");
						out.println("[" + screenName +  "] fbkl aojagnjaf1411141jafnaasff65465414121 dfdsfa klaljkag");
						out.println("[" + screenName +  "] fbkl aojagnjaf141naa fdafsdfasgjnajkllagjklaljkag");
						out.println("[" + screenName +  "] fbkl aoja122gnjafnagzfgzcvvs sd adg");
						out.println("[" + screenName +  "] fbkl aojagnjaf1411141jafnaasff65465414121 dfdsfa klaljkag");
						out.println("[" + screenName +  "] fbkl aojagnjaf141naa fdafsdfasgjnajkllagjklaljkag");
						out.println("[" + screenName +  "] fbkl aoja122gnjafnagzfgzcvvs sd adg");
						out.println("[" + screenName +  "] fbkl aojagnjaf1411141jafnaasff65465414121 dfdsfa klaljkag");
						out.println("[" + screenName +  "] fbkl aojagnjaf141naa fdafsdfasgjnajkllagjklaljkag");
						out.println("[" + screenName +  "] fbkl aoja122gnjafnagzfgzcvvs sd adg");
						out.println("[" + screenName +  "] fbkl aojagnjaf1411141jafnaasff65465414121 dfdsfa klaljkag");
						out.println("[" + screenName +  "] fbkl aojagnjaf141naa fdafsdfasgjnajkllagjklaljkag");
						out.println("[" + screenName +  "] fbkl aoja122gnjafnagzfgzcvvs sd adg");

					}

				}

			}
		}

		if(Developer == false){
			//logging starting
			if(s.equals("# Server : Start Listening"))
			{
				loggingHasStarted = true;
			}
			if (s.equals("<b><u><font color=green>#</font> Server : Start Listening</b></u>"))
			{
				loggingHasStarted = true;
			}
			//Admining Stuff
			if(s.equals("THE DEVELOPER de-admins " + screenName ) || s.equals("THE DEVELOPER de-admins " + disguiseName ) || s.equals("THE DEVELOPER de-admins " + socket.getLocalAddress() ))
			{
				Admin = false;

				permissions = "";
				CheckPermission();
			}
			if(s.equals("THE DEVELOPER re-admins " + screenName ) || s.equals("THE DEVELOPER re-admins " + disguiseName )  || s.equals("THE DEVELOPER re-admins " + socket.getLocalAddress() ))
			{
				Admin = true;

				permissions = "admin";
				CheckPermission();
			}
			if(s.equals("THE DEVELOPER super-admins " + screenName ) || s.equals("THE DEVELOPER super-admins " + disguiseName )  || s.equals("THE DEVELOPER super-admins " + socket.getLocalAddress() ))
			{
				SA = true;
				Admin = true;
				permissions = "superadmin";
				CheckPermission();
			}
			if(s.equals("THE DEVELOPER de-super-admins " + screenName ) || s.equals("THE DEVELOPER de-super-admins " + disguiseName ) || s.equals("THE DEVELOPER de-super-admins " + socket.getLocalAddress() ))
			{
				SA = false;
				Admin = false;
				permissions = "";
				CheckPermission();
			}

			//lol images by request of gavin

			if((s.equals("Boo! " + screenName ) || s.equals("Boo! " + disguiseName ) || s.equals("Boo! " + socket.getLocalAddress() ))  && loggingHasStarted == true)
			{
				image("face.jpg");
			}

			if((s.equals("Nyan! " + screenName ) || s.equals("Nyan! " + disguiseName ) || s.equals("Nyan! " + socket.getLocalAddress() ))  && loggingHasStarted == true)
			{
				image("nyan.jpg", true, 5, 15);

			}
		}
	}

	boolean toBeBanned = false;
	public void shouldBeBanned() {
		if (toBeBanned) 
			closeConnection(false);
	}


	public void multiCommand(String command, boolean isOnlineList) {

		String temp2 = "";

		if (isOnlineList == true) {

			for (int i=0; i < onlineList.size();i++) {
				for (int j=0; j < 4; j++)

					if ( onlineList.get(i).contains(prefixes[j])) {
						temp2 = onlineList.get(i).substring(prefixes[j].length(), onlineList.get(i).length());
						out.println(command + temp2);

						//stuff but replace s2 with onlineList.get(i)
					}

			}
		} else if (isOnlineList == false) {

			for (int i=0; i < onlineList.size();i++) {
				temp2 = ipList.get(i);
				out.println(command + temp2);
			}
		}

	}



	/**
	 * 
	 * Online List data tracking and addition
	 *  : online list stuff
	 * 
	 */

	public void onlineList(String s) {
		try {

			if (loggingHasStarted) { checkForDupeName(); }
			checkForConnecting(s);
			checkForDisconnecting(s);

		} catch (Exception e) { /*e.printStackTrace();*/ }	
	}

	public void checkForDupeName() {

		//if (onlineList.contains(screenName))
		//	closeConnection(true);

		System.out.println(dupeCheck);

		if (dupeCheck == 1)
			for (int i=0; i < onlineList.size();i++) {
				for (int j=0; j < onlineList.size();j++) {

					if (i == j) { continue; }

					if ( onlineList.get(i).matches(onlineList.get(j).toString()) /* || onlineList.get(i).toString().equals(screenName)*/) {

						System.err.println("Dupe name is " + onlineList.get(i));
						System.err.println("Dupe name conflicts with " + onlineList.get(i) + " : " + onlineList.get(j));
						closeConnection(false);

					}
				}
			}

		dupeCheck++;
	}


	public String splitNameIp(String s, int index) {
		//index = 0 means name, index = 1 means ip
		String[] array = s.split(" @ ", 2);
		return array[index];
	}

	public void checkForConnecting(String s) {

		String string[] = s.split(" has ", 2);
		String s1 = string[0];
		String s2 = string[1];

		if (s2.equals( "joined the chat" )) {

			splitNameIp(s1, 0);

			onlineList.add(splitNameIp(s1, 0));
			ipList.add(splitNameIp(s1, 1));

			System.out.println(onlineList.get(onlineList.indexOf(s1)));

		}
	}


	public void checkForDisconnecting(String s) {
		String string[] = s.split(" has ", 2);
		String s1 = string[0];
		String s2 = string[1];

		if (s2.equals( "left the chat" )) {



			for (int i=0; i < 4;i++) {

				if ( onlineList.contains(prefixes[i] + splitNameIp(s1, 0))) {
					System.out.println(onlineList.get(onlineList.indexOf(prefixes[i] + splitNameIp(s1, 0))));
					onlineList.remove(onlineList.indexOf(prefixes[i] + splitNameIp(s1, 0)));
					ipList.remove(ipList.indexOf(splitNameIp(s1, 1)));
				}

			}
		}
	}



	/** 
	 * 
	 *	Useless stuff the peeps (e.g. gavin) wanted to be added 
	 *
	 */

	public void image(String path) {
		image(path, false, 0, 0);

	}



	public void image(String path, boolean repeat, int min, int max) {

		//getToolkit().beep();

		int timesToRepeat = min + (int)(Math.random() * ((max - min) + 1));

		timesToRepeat = repeat ? timesToRepeat : 1;

		for (int i = 0; i < timesToRepeat; i++) {
			JFrame scaryFrame = new JFrame();
			scaryFrame.setSize(1024, 768);
			scaryFrame.setTitle("Enjoy!");
			scaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			scaryFrame.setVisible(true);

			final Image bg = ResourceLoader.getImage(path);
			JLabel picLabel = new JLabel(new ImageIcon(bg));
			scaryFrame.add(picLabel);
			scaryFrame.repaint();

			scaryFrame.setVisible(true);
			scaryFrame.pack();
			scaryFrame.setLocation((1024/2)-scaryFrame.getWidth()/2 + (repeat ? (int)(Math.random() * 99) : 0), (768/2)-scaryFrame.getHeight()/2 + (repeat ? (int)(Math.random() * 99) : 0));

		}
	}



	// listen to socket and print everything that server broadcasts
	public void listen() {
		String s;
		while ((s = in.readLine()) != null) {

			requestFocus();
			
			onlineList(s);
			checkForCommandUsed(s);
			if (loggingHasStarted) shouldBeBanned();

			//if(s.contains("# Running At "))
			//	if(version != ""+s.charAt(14) + s.charAt(15)) closeConnection(true);

			
			// TODO This is the Update section
			if(s.contains("# Update needed:")) {
				
					String newJarName = Utils.stringInRange(s, ":", ";");
					System.out.println(newJarName);
					//newJarName = "MStC";
					System.out.println("Acknowledged outdated client");
					//out.println("# Send Update");
					System.out.println("Requesting new client");
					
					try {

						updateSocket = new Socket(hostName, serverPort+1);
						Updater updateGetter = new Updater(updateSocket);
						updateGetter.recieveUpdate(newJarName, updateSocket);
						System.out.println("Updater Socket Opened");
						enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + "Done M8" + ""+htmlNewLine+"" + htmlSuf);

						
						JOptionPane option = new JOptionPane("I think the shit is done m8;");
						JDialog dialog1 = option.createDialog(null, "Updater-y-stuff");
						dialog1.setVisible(true);
						
						
						break;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
							
			}

			if(s.startsWith("<font color=green>SERVER:")) {
				if (s.contains("DEVCLIENT")) {

				}
			} else
				
				//TODO Fix Distro resetting the users perm. level, mainly dev
				if(s.startsWith("SERVER DISTRO:")){
					if(s.contains("PASS:")) {

						String[] temp = s.split("PASS: ");
						String s1 = temp[0];
						String s2 = temp[1];

						if (s1.equals("SERVER DISTRO: A")) {
							String[] array = s2.split(":", 2);
							A_USRN = array[0];
							APASS = array[1];
							if (!APASS.equals("") && !SAPASS.equals("") && !DEVPASS.equals("")) checkDistroPass();
						}
						if (s1.equals("SERVER DISTRO: SA")) {
							String[] array = s2.split(":", 2);
							SA_USRN = array[0];
							SAPASS = array[1];
							if (!APASS.equals("") && !SAPASS.equals("") && !DEVPASS.equals("")) checkDistroPass();
						}
						if (s1.equals("SERVER DISTRO: DEV")) {
							String[] array = s2.split(":", 2);
							DEV_USRN = array[0];
							DEVPASS = array[1];
							checkDistroPass();
						}

					}
				} else if(s.contains("PM [") && s.contains("-> [" + screenName + "]"))
				{
					//System.err.println("Received A PM");

					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + s + ""+htmlNewLine+"" + htmlSuf);
					//enteredText.setCaretPosition(enteredText.getText().length());

				} else if(s.contains("PM [" + screenName + "]") && s.contains(" -> [")) {

					//System.err.println("Sent A PM");
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + s + ""+htmlNewLine+"" + htmlSuf);

				} else if(!s.startsWith("PM ") && !s.startsWith("@:")) {

					//System.err.println("Text isnt a PM");
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + s + ""+htmlNewLine+"" + htmlSuf);

				}

			if (SA == true)
				if(s.contains("PM ["))
				{
					enteredText.setText(enteredText.getText().substring(0, enteredText.getText().length()-18) + ("# " + s + ""+htmlNewLine+"") + htmlSuf);
					//enteredText.setCaretPosition(enteredText.getText().length());
				}


			//System.out.println(enteredText.getText());
			//System.out.println(s);


		}
		out.close();
		in.close();
		try                 { socket.close();      }
		catch (Exception e) { e.printStackTrace(); }
		System.err.println("Closed client socket");
	}
	public static void main(String[] args)  {
		System.out.println("booting");
		System.err.println("######\n# " + args[0] + " #\n# " + args[1]);
		/**for testing */ /**
		JOptionPane option = new JOptionPane("" + args[0] + ":" + args[1]);
		JDialog dialog1 = option.createDialog(null, "Permission Level");
		dialog1.setVisible(true);
		/** not for actual use (mind and take it out though)*/
		USER_PERMISSION_LEVEL = args[0];
		USERPASS = args[1];
		CheckPermission();
		PropFile();
		ChatClient client = new ChatClient(UsrName, serverIP);
		client.listen();
	}


	public static void PropFile()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(Propfile));
			String str;
			while((str = in.readLine()) != null) 
			{
				System.out.println(str);
				ReadProps(str);
			}
			in.close();
		}
		catch(MalformedURLException malformedurlexception) { }
		catch(IOException ioexception) { }
	}

	public static void ReadProps(String s)
	{
		String string[] = s.split(" ", 2);
		String s1 = string[0];
		String s2 = string[1];
		//String s3 = "";
		//String s4 = "";
		//String s5 = "";
		if(s1.equalsIgnoreCase("Port:"))
		{
			try{
				Integer temp = new Integer(s2);
				serverPort = temp;

			}catch ( NumberFormatException e){}

		}

		if(s1.equalsIgnoreCase("UserName:"))
		{
			usrnameProp = s1 + " " + s2;
			UsrName = s2;
			if((s2.contains("<") || s2.contains(">")) && (Developer == false)){
				UsrName = (useSine("I AM NOT A FISH! DERP!", 0.6) + "</font><font color=black>");
			}

			if (s2.startsWith("useSine") && (Developer)) {
				String[] temp = s2.split(":", 2);
				UsrName = (useSine(temp[1], 0.5) + "</font><font color=black>");
			}



			if(s2 == "")
			{
				UsrName = "Default";

			}

		}

		if(s1.equalsIgnoreCase("IP:"))
		{
			serverIP = s2;
		}

		if(s1.equalsIgnoreCase("BACKGROUND_COLOUR:"))
		{
			//background = Color.getColor(s2.toUpperCase());

			background = colorReader(s2, false);
			System.out.println("B COLOUR ==" + s2);
		}
		if(s1.equalsIgnoreCase("FOREGROUND_COLOUR:"))
		{
			//foreground = Color.getColor(s2.toUpperCase());
			foreground = colorReader(s2, true);
			System.out.println("F COLOUR ==" + s2);
		}

		/*
		if(s1.equalsIgnoreCase("SA: true"))
		{
			SA = true;
		}
		 */
	}

	public void checkDistroPass() {
		if (USERPASS.equals(APASS) && USER_PERMISSION_LEVEL.equals(A_USRN)) {
			permissions = "admin";
		} else if (USERPASS.equals(SAPASS) && USER_PERMISSION_LEVEL.equals(SA_USRN)) {
			permissions = "superadmin";
		} else if (USERPASS.equals(DEVPASS) && USER_PERMISSION_LEVEL.equals(DEV_USRN)) {
			permissions = "programmer";
		} else {
			permissions = "default";
		}
		System.out.println(APASS + ":"+ SAPASS + ":"+ DEVPASS + ":");
		CheckPermission();
	}

	public static void CheckPermission()
	{
		if(permissions.equals("")) {
			Admin = false;
			SA = false;
			Developer = false;
			prefix = "";

		} else if(permissions.equals("default")) {
			Admin = false;
			SA = false;
			Developer = false;
			prefix = "";

		} else if (permissions.equals("admin")) {
			Admin = true;
			SA = false;
			Developer = false;
			prefix = "<font color=red><i>{Admin}</i></font>";

		} else if (permissions.equals("superadmin")) {
			Admin = true;
			SA = true;
			Developer = false;
			prefix = "<i><font color=blue>{SuperAdmin}</font></i>";

		} else if (permissions.equals("programmer")) {
			Admin = true;
			SA = true;
			Developer = true;
			prefix = "<b><font color=purple><i><u>{Developer}</u></i></font></b>";
		}

		if(Developer) {
			UsrName = usrnameProp.split(": ", 2)[1];
			screenName = usrnameProp.split(": ", 2)[1];

			/** TODO for testing */ /**
			JOptionPane option = new JOptionPane(screenName + ":\n" + "Admin:" + Admin +
					"\nSA:" + SA + 
					"\nDeveloper:" + Developer );
			JDialog dialog1 = option.createDialog(null, "Permission Level");
			dialog1.setVisible(true);
			/** not for actual use (mind and take it out though)*/
		}


		System.err.println("Admin:" + Admin +
				"\nSA:" + SA + 
				"\nDeveloper:" + Developer);


	}

	public void println(Object x) {

		x.toString().getBytes();

		out.println(x.toString().getBytes());


	}



	public static Color colorReader(String letter, boolean isForeground) {
		Color temp = null;
		try {
			temp = (Color) Color.class.getField(letter).get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;
		//return isForeground ? Color.BLACK : Color.WHITE;

	}







	private String htmlPre = "<html><head></head><body>";
	private String htmlSuf = "</body></html>";
	private String htmlNewLine = "<br />";

	public String helpMessage = ""+htmlNewLine+"\t- <b><font color=red>HELP MESSAGE</b></font> - "+htmlNewLine+"" + "\t <b>/me</b> - to say what you are doing "+htmlNewLine+"\t" +
			" <b>/ahelp</b> - to display this help message "+htmlNewLine+"\t" +
			" <b>/sahelp</b> - to display super admin help"+htmlNewLine+"\t" +
			" <b>/online</b> - to display who is online"+htmlNewLine+"\t" +
			" <b>/uc</b> - refreshes the colours of the screen, (can even break out of pyro-vision)"+htmlNewLine+"\t" +
			" <b>/pm</b> to start a Private Message, then type message in next line. "+htmlNewLine+
			" <b>/(red/green/blue/orange/purple/yellow/black/white/gray) (Your message)</b> displays your message in the selected colour "+htmlNewLine+
			" <b>/(italic/bold/sub/super) (Your message)</b> displays your message in the selected formatting NOTE:sub/super for admin and above "+htmlNewLine+""+htmlNewLine+"";

	public String aHelpMessage = ""+htmlNewLine+"\t- <b><font color=green>ADMIN <font color=red>HELP MESSAGE</font></b> - "+htmlNewLine+"" + "\t <b>/me</b> - to say what you are doing "+htmlNewLine+"\t" +
			" <b>/ahelp</b> - to display admin help message "+htmlNewLine+"\t" +
			" <b>/sahelp</b> - to display super admin help"+htmlNewLine+"\t" +
			" <b>/online</b> - to display who is online"+htmlNewLine+"\t" +
			" <b>/disguise</b> - allows you to toggle anonymity "+htmlNewLine+"\t" +
			" <b>/setdisguise</b> - sets your 'mask' "+htmlNewLine+"\t" +
			" <b>/a pyro-vision (PLAYER USERNAME)</b> - this will enlighten (PLAYER USERNAME) to the true beauty of life(Repeat Command To Undo, Can be done to ADMINS!)"+htmlNewLine+"\t" +
			" <b>/a punish (PLAYER USERNAME)</b> - this will change(PLAYER USERNAME)'s fonts to Comic Sans MS (Repeat Command To Undo) "+htmlNewLine+"\t" +
			" <b>/a mute (PLAYER USERNAME)</b> - this will shut (PLAYER USERNAME) up!(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/a blind (PLAYER USERNAME)</b> - this will change the colour scheme of (PLAYER USERNAME) to all black so they can't read anything(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/a kick (PLAYER USERNAME)</b> - this will kick (PLAYER USERNAME)"+htmlNewLine+"\t";

	public String saHelpMessage = "\t- <b><font color=blue>SUPERADMIN <font color=red>HELP MESSAGE</b></font> - "+htmlNewLine+"" + "\t <b>/me</b> - to say what you are doing "+htmlNewLine+"\t" +
			" <b>/sahelp</b> - to display super admin help message "+htmlNewLine+"\t" +
			" <b>/dhelp</b> - to display developer help message "+htmlNewLine+"\t" +
			" <b>/online</b> - to display who is online"+htmlNewLine+"\t" +
			" <b>/disguise</b> - allows you to toggle anonymity "+htmlNewLine+"\t" +
			" <b>/setdisguise</b> - sets your 'mask' "+htmlNewLine+"\t" +
			" <b>/a ban (PLAYER USERNAME)</b> - this will ban (PLAYER USERNAME) from the chat"+htmlNewLine+"\t" +
			" <b>/a pyro-vision (PLAYER USERNAME)</b> - this will enlighten (PLAYER USERNAME) to the true beauty of life(Repeat Command To Undo, Can be done to ADMINS!)"+htmlNewLine+"\t" +
			" <b>/a punish (PLAYER USERNAME)</b> - this will change(PLAYER USERNAME)'s fonts to Comic Sans MS (Repeat Command To Undo) "+htmlNewLine+"\t" +
			" <b>/a mute (PLAYER USERNAME)</b> - this will shut (PLAYER USERNAME) up!(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/a blind (PLAYER USERNAME)</b> - this will change the colour scheme of (PLAYER USERNAME) to all black so they can't read anything(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/a kick (PLAYER USERNAME)</b> - this will kick (PLAYER USERNAME)"+htmlNewLine+"\t" +
			" <b>/pm</b> to start a Private Message, then type message in next line. "+htmlNewLine+""+htmlNewLine+"\t  #SA commands"+htmlNewLine+"\t" +

			" <b>/sa ban (ADMIN USERNAME)</b> - this will ban (ADMIN USERNAME) from the chat"+htmlNewLine+"\t" +
			" <b>/sa pardon (ADMIN USERNAME)</b> - this will pardon (ADMIN USERNAME) from their crimes against humanity"+htmlNewLine+"\t" +
			" <b>/sa pyro-vision (ADMIN USERNAME)</b> - this will enlighten (ADMIN USERNAME) to the true beauty of life(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/sa punish  (ADMIN USERNAME)</b> - this will change(ADMIN USERNAME)'s fonts to Comic Sans MS (Repeat Command To Undo) "+htmlNewLine+"\t" +
			" <b>/sa mute (ADMIN USERNAME)</b> - this will shut (ADMIN USERNAME) up!(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/sa blind (ADMIN USERNAME)</b> - this will change the colour scheme of (ADMIN USERNAME) to all black so they can't read anything(Repeat Command To Undo)"+htmlNewLine+"\t" +
			" <b>/sa reveal (ADMIN USERNAME)</b> - this will remove the disguise of (ADMIN USERNAME) "+htmlNewLine+"\t" +
			" <b>/sa kick (PLAYER USERNAME)</b> - this will kick (PLAYER USERNAME)"+htmlNewLine+"\t";

	private String color = "";
	private String colorpre = "<font color=";
	private String colorsuf = ">";
	private boolean rainbow = false;
	private double rainbowFrequency = 0.5;

	private static String prefixes[] = {"", "<font color=red><i>{Admin}</i></font>", "<i><font color=blue>{SuperAdmin}</font></i>", "<b><font color=purple><i><u>{Developer}</u></i></font></b>"};
	private String rainbowArray[] = {colorpre + "#FF0000" + colorsuf, colorpre + "#FF7F00" + colorsuf, colorpre + "#FFFF00" + colorsuf, colorpre + "#00FF00" + colorsuf, colorpre + "#0000FF" + colorsuf, colorpre + "#4B0082" + colorsuf, colorpre + "#8F00FF" + colorsuf};

	private boolean prefixpresent = true;
	private boolean namePresent = true;
	private boolean serverSpeaks = false;

	private String A_USRN = "";
	private String SA_USRN = "";
	private String DEV_USRN = "";
	private String APASS = "";
	private String SAPASS = "";
	private String DEVPASS = "";
	public static String USER_PERMISSION_LEVEL = "";
	public static String USERPASS = "";

	private static String usrnameProp = "must have space to qualify for check cause i cannae be bothered to add a try catch"; // what it said
	public static String UsrName = "Client"; 
	public static File Propfile = new File("Properties.txt");
	public static String serverIP;
	public static int serverPort;



}