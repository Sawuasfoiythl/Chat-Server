package com.sawuasfoiythl.chatServer.server;
/*************************************************************************
 *  Compilation:  javac ChatServer.java 
 *  Execution:    java ChatServer
 *  Dependencies: In.java Out.java Connection.java ConnectionListener.java
 *
 *  Creates a server to listen for incoming connection requests on 
 *  port 4444.
 *
 *  % java ChatServer
 *
 *  Remark
 *  -------
 *    - Use Vector instead of ArrayList since it's synchronized.
 *    
 *    The following Code is hereby copyrighted by Myles Lumsden, so FUCK OFF JAMIE, COCO, GAVIN, ANDIE, and everyone else
 *  
 *************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.sawuasfoiythl.chatServer.networking.Connection;
import com.sawuasfoiythl.chatServer.networking.ConnectionListener;
import com.sawuasfoiythl.chatServer.updater.Updater;

public class ChatServer{

	public static String version = "09";
	public static String versionName = "MStC";

	public static boolean serverRunning;
	public static ServerSocket mainServerSocket;

	public static ArrayList<String> log = new ArrayList<String>(); 


	public static ArrayList<String> banlist = new ArrayList<String>(); 
	public static ArrayList<String> onlineList = new ArrayList<String>();
	public static ArrayList<String> ipList = new ArrayList<String>();


	static Vector<Connection> connections1        = new Vector<Connection>();



	public static void main(String[] args) throws Exception {
		serverRunning = true;
		
		PropFile();

		DEVPASS = genPass();
		updateDevPass();
		System.out.println(DEVPASS);

		Vector<Connection> connections        = new Vector<Connection>();
		ServerSocket serverSocket             = new ServerSocket(serverPort);

		ServerSocket updateSocket             = new ServerSocket(serverPort+1);
		
		
		ConnectionListener connectionListener = new ConnectionListener(connections);

		mainServerSocket = serverSocket;

		// thread that broadcasts messages to clients
		connectionListener.start();

		System.err.println("ChatServer started");

		try {
			while (serverRunning == true) {
				
				// wait for next client connection request
				Socket clientSocket = serverSocket.accept();
				connections1 = connections;
				System.err.println("Created socket with client: " + clientSocket.getInetAddress().getHostName());

				// listen to client in a separate thread
				Connection connection = new Connection(clientSocket);


				connection.println("<b><u><font color=green>#</font> Running at V</b></u>" + version );
				connection.println("SERVER DISTRO: APASS: " + A_USRN+":"+APASS );
				connection.println("SERVER DISTRO: SAPASS: " + SA_USRN+":"+SAPASS );
				connection.println("SERVER DISTRO: DEVPASS: " + DEV_USRN+":"+DEVPASS );
				System.err.println("PRINTED PASSWORDS");

				/*
			int i = 0;
			do {
				try{
				String[] temp = new String[5000];
				log.toArray(temp);
					//System.out.println(log.get(i));
					//connection.println(log.get(i));
					connection.println(temp[i]);
					i++;
				} catch (IndexOutOfBoundsException e) {
				 e.printStackTrace();
				 break;
				}
			}while (log.get(i+1) != null);
				 */



				for(int i=0; i < log.size(); i++){
					System.out.println(log.get(i));
					connection.println(log.get(i));
				}

				connection.println("<b><u><font color=green>#</font> Server : Start Listening</b></u>");

				if( connection.in.readLine().equals("Client running at :" + version)) {

					System.out.println("WORKING??!?!?");
					System.out.println(connection.in.readLine());


					//}else {



					connections.add(connection);
					connection.start();
				}
				// init updater
				else {
					
					connection.println("# Update needed:" + versionName+ ";");
					System.out.println("Found outdated client");
					//if (connection.in.readLine().equals("# Send Update")) {
						System.out.println("Sending new client");
						Socket updateSocketer = updateSocket.accept();
						Updater updateSender = new Updater(updateSocketer);
						updateSender.sendUpdate(versionName);
					//}
					
				}
			}  
		} catch (SocketException se) {

			System.err.println("### ACTUALLY RESETTING ###");
			connections.clear();
			serverSocket.close();
			//connectionListener.interrupt();
			connectionListener = null;
			log.clear();
			banlist.clear();
			onlineList.clear();
			ipList.clear();
			main(args);
		}

	}


	//	public static void InputFromConsole()
	//	{
	//		try
	//		{
	//			DataInputStream in = new DataInputStream(System.in);
	//			System.out.println("Needs Input from Console");
	//			String str;
	//			while((str = in.readLine()) != null) 
	//			{
	//				System.out.println(str);
	//				if( str.equalsIgnoreCase("end"))
	//				{
	//
	//					System.exit(0);
	//
	//				}
	//			}
	//			in.close();
	//		}
	//		catch(MalformedURLException malformedurlexception) { }
	//		catch(IOException ioexception) { }
	//	}

	public static void commands(String s, String s1) {

		if(s.startsWith("@")) {

			System.err.printf("DOING SOMETHING\n~#######################~%s", s1);




			if(s1.startsWith("DEVCLIENT")) {
				System.err.println("DEV!!!");
				try {

					System.err.println("DEV!!!");
					
					/**for testing */
					JTextField textField = new JTextField();
					textField.setText(DEVPASS);
					Object[] msg = {"DEV HASH KEY", textField};
					
					JOptionPane op = new JOptionPane(
							msg,
							JOptionPane.QUESTION_MESSAGE,
							JOptionPane.OK_CANCEL_OPTION,
							null,
							null);

					JDialog dialog = op.createDialog(null, "DEV HASHING KEY");
					dialog.setVisible(true);

					
					File file = new File("dev.bat");
					BufferedWriter wr = new BufferedWriter(new FileWriter(file));
					wr.write("java -cp Chat"+ version +".jar " + "dev " + DEVPASS);
					wr.newLine();
					wr.write("pause");
					wr.flush();
					wr.close();
					
					
					/** not for actual use (mind and take it out though)*/
					getDev();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(s1.startsWith("PASS GEN")) {

				System.err.println("DEV!!!");
				DEVPASS = genPass();
				changeList(ghettoIndex("DEVPASS", propFileOld), "DEVPASS: " + DEV_USRN + ":" + DEVPASS, propFileOld);
				
				updatePasswords();

				writeToProps();
			}
			if(s1.startsWith("PASS")) {
				String[] split = s1.split(" ", 2);System.err.println("String[] split = s1.split(\" \", 3);");

				String[] split1 = split[1].split(":", 2);System.err.println("String[] split = s1.split(\" \", 3);");
				String prt1 = split1[0];System.err.println("String prt1 = split[1];");
				String prt2 = split1[1];System.err.println("String prt2 = split[2];");

				if (prt1.equalsIgnoreCase("admin")) {
					APASS = prt2;
					changeList(ghettoIndex("APASS", propFileOld), "APASS: " + A_USRN + ":" + APASS, propFileOld);
				}
				if (prt1.equalsIgnoreCase("superadmin")) {
					SAPASS = prt2;
					//writeToProps(ghettoIndex("SAPASS", propFileOld), "SAPASS: " + SA_USRN + ":" + SAPASS, true);
					changeList(ghettoIndex("SAPASS", propFileOld), "SAPASS: " + SA_USRN + ":" + SAPASS, propFileOld);
					
				}
				if (prt1.equalsIgnoreCase("dev")) {
					DEVPASS = prt2;
					changeList(ghettoIndex("DEVPASS", propFileOld), "DEVPASS: " + DEV_USRN + ":" + DEVPASS, propFileOld);
					
				}
				System.out.println(prt1 + ":" + prt2);
				System.out.println(APASS + ":" + SAPASS + ":" + DEVPASS);
				updatePasswords();
				writeToProps();
				
			}


		}

		if(s.startsWith("<font color=green>SERVER")) {
			if (s1.equals("Clearing Logs...")) {
				log.clear();
				System.err.println("Clearing logs");
				populateLogs(true);
			}

			if(s1.equals("DEVCLIENT")) {
				try {

					System.err.println("DEV!!!");
					getDev();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(s1.equals("DEVCLIENT GENPASS")) {

				System.err.println("DEV!!!");
				DEVPASS = genPass();
				updateDevPass();

			}

			if (s1.equals("Clearing ALL")) {
				log.clear();
				System.err.println("Clearing all");
				populateLogs(false);
			}

			if (s1.equals("Restarting")) {
				System.err.println("Restarting...");
				try {
					getDev();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					mainServerSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				serverRunning = false;

			}

			if (s1.equals("Closing")) {
				System.err.println("Closing...");

				try {
					endServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


	public static void updatePasswords() {
		printAll(("SERVER DISTRO: APASS: " + A_USRN+":"+APASS ));
		printAll(("SERVER DISTRO: SAPASS: " + SA_USRN+":"+SAPASS ));
		printAll(("SERVER DISTRO: DEVPASS: " + DEV_USRN+":"+DEVPASS ));
	}

	public static void printAll(String s) {
		for (Connection c : connections1) {
			c.println(s);
		}
	}



	public static void commandTracker(String s) {
		try {
			String[] message = s.split(": ", 2);


			String prefix = message[0];
			String command = message[1];
			commands(prefix, command);


		} catch (ArrayIndexOutOfBoundsException e) {
			//e.printStackTrace();

		}	
	}

	public static boolean populateLogs(boolean evenBans) {
		String message;

		for (int i=0; i < onlineList.size(); i++) {

			message = onlineList.get(i) + " @ " + ipList.get(i) + " has joined the chat";
			log.add(message);
		}

		if (evenBans == true)
			for (int j=0; j < banlist.size(); j++) {

				message = "THE SUPER ADMIN bans " + banlist.get(j);
				log.add(message);
			}




		return true;
	}


	public static void onlineListTracker(String s){
		onlineList(s);
	}

	public static void onlineList(String s) {
		try {

			checkForConnecting(s);
			checkForDisconnecting(s);

		} catch (Exception e) { /*e.printStackTrace();*/ }	
	}


	public static String splitNameIp(String s, int index) {
		//index = 0 means name, index = 1 means ip
		String[] array = s.split(" @ ", 2);
		return array[index];
	}


	public static void checkForConnecting(String s) {

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


	public static void checkForDisconnecting(String s) {
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


	public static void banListTracker(String s){

		String[] message = s.split(" ", 5);
		if(s.startsWith("THE SUPER ADMIN bans"))
			try {
				String name = message[4];
				banlist.add(name);

				System.err.println("");
				System.err.println("###################");
				System.err.println("# Banning someone #");
				System.err.println("###################");
				System.err.println("");

			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();

			}

	}

	public static int ghettoIndex(String s, ArrayList<String> ALS) {
		
		for(int i = 0; i < ALS.size(); i++) {
			if (ALS.get(i).contains(s)) return i;
		}
		return 0;
	}

	public static ArrayList<String> propFileOld = new ArrayList<String>();
	public static void PropFile()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(Propfile));
			String str;
			while((str = in.readLine()) != null) 
			{
				System.err.println(str);
				
				propFileOld.add(str);
				ReadProps(str);
				//if (str.startsWith("DEV")) break;
			}
			in.close();
		}
		catch(MalformedURLException malformedurlexception) { }
		catch(IOException ioexception) { }
	}

	public static void changeList(int lineToReplace, String toReplaceWith, ArrayList<String> ALS) {
		
			ALS.set(lineToReplace, toReplaceWith);
	}
	
	public static void writeToProps() {
		writeToProps(0,"",false);
	}
	
	public static void writeToProps(int lineToReplace, String toReplaceWith, boolean changeLine) {
		if (changeLine)
			propFileOld.set(lineToReplace, toReplaceWith);
		System.out.println("WRITEING TO PROPS!");
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(Propfile));
			for(int i = 0; i < propFileOld.size(); i++) {

				write.write(propFileOld.get(i) + ((i == propFileOld.size()-1) ? "" : "\n"));
			}
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ReadProps(String s)
	{
		String string[] = s.split(" ", 2);
		String s1 = string[0];
		String s2 = string[1];

		if(s1.equalsIgnoreCase("Port:"))
		{
			try{
				Integer temp = new Integer(s2);
				serverPort = temp;

			}catch ( NumberFormatException e){}

		}

		if(s1.equalsIgnoreCase("APASS:"))
		{
			String[] array = s2.split(":", 2);
			A_USRN = array[0];
			APASS = array[1];
			System.out.println(APASS);
		}
		if(s1.equalsIgnoreCase("SAPASS:"))
		{
			String[] array = s2.split(":", 2);
			SA_USRN = array[0];
			SAPASS = array[1];
			System.out.println(SAPASS);
		}
		if(s1.equalsIgnoreCase("DEVPASS:"))
		{
			String[] array = s2.split(":", 2);
			DEV_USRN = array[0];
			DEVPASS = array[1];
			//DEVPASS = genPass();
			//updateDevPass();
			System.out.println(DEVPASS);
		}

	}

	static String A_USRN = "";
	static String SA_USRN = "";
	static String DEV_USRN = "";
	static String APASS = "";
	static String SAPASS = "";
	static String DEVPASS = "";

	private static String[] password = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
	/*, "+", "-", "*", "?", "!", "#", "_", "=", "&", "%", "$", "€", "£"**/};
	private static String genPass() {
		String newPass = "";

		for (int i = 0; i < 512; i++) {
			newPass = newPass + password[0 + (int)(Math.random() * ((password.length - 1) + 1))];
		}
		System.out.println(newPass);
		return newPass;
	}
	
	private static void updateDevPass() {
		changeList(ghettoIndex("DEVPASS", propFileOld), "DEVPASS: " + DEV_USRN + ":" + DEVPASS, propFileOld);
		updatePasswords();
		writeToProps();
	}


	public static void restartServer() throws Exception
	{
		//Process p = Runtime.getRuntime().exec("python restart.py");
		Process p = Runtime.getRuntime().exec("java -cp Chat.jar ChatServer");
		Thread.sleep(100000000);
		System.exit(0);
	}



	public static final String SUN_JAVA_COMMAND = "sun.java.command";

	/**
	 * Restart the current Java application
	 * @param runBeforeRestart some custom code to be run before restarting
	 * @throws IOException
	 */
	public static void getDev() throws IOException {
		try {
			// java binary
			String java = System.getProperty("java.home") + "/bin/java";
			// vm arguments
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				// if it's the agent argument : we ignore it otherwise the
				// address of the old application and the new one will be in conflict
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			// init the command to execute, add the vm args
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

			// program main and program arguments
			String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			// program main is a jar
			/*if (mainCommand[0].endsWith(".jar")) {
				// if it's a jar, add -jar mainJar
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			} else {
			 */
			// else it's a .class, add the classpath and mainClass
			cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + "ChatClient" + " " + DEV_USRN + " " + DEVPASS);

			System.err.println("-cp \"" + System.getProperty("java.class.path") + "\" " + "ChatClient" + " " + DEV_USRN + " " + DEVPASS);
			//}
			// finally add program arguments
			for (int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}
			// execute the command in a shutdown hook, to be sure that all the
			// resources have been disposed before restarting the application
			//Runtime.getRuntime().addShutdownHook(new Thread() {
			try {
				Runtime.getRuntime().exec(cmd.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*new Thread(){	
			@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(cmd.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}/**)*/;
		} catch (Exception e) {
			// something went wrong
			throw new IOException("Error while trying to restart the application", e);
		}
	}
	/**
	 * Attempted but would not function as intended
	 * 
	public static void restartServer() throws Exception
	{
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File(ChatServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		// is it a jar file? 
		if(!currentJar.getName().endsWith(".jar"))
			return;

		// Build command: java -jar application.jar 
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		System.err.println("##############");
		System.err.println("# Restarting #");
		System.err.println("##############");

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
	 */

	public static void endServer() {

		System.exit(0);

	}












	private static String prefixes[] = {"", "<font color=red><i>{Admin}</i></font>", "<i><font color=blue>{SuperAdmin}</font></i>", "<b><font color=purple><i><u>{Developer}</u></i></font></b>"};


	public static File Propfile = new File("ServerProperties.txt");
	public static int serverPort;



}
