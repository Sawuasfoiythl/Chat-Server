package com.sawuasfoiythl.chatServer.networking;
/*************************************************************************
 *  Compilation:  javac Connection.java
 * 
 *************************************************************************/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.Socket;

import com.sawuasfoiythl.chatServer.server.ChatServer;

public class Connection extends Thread {
	private Socket socket;
	public Out out;
	public In in;
	private String message;     // one line buffer

	public Connection(Socket socket) {
		in  = new In(socket);
		out = new Out(socket);
		this.socket = socket;
	}

	public void println(String s) { out.println(s); }

	public void run() {
		String s;
		while ((s = in.readLine()) != null) {
			setMessage(s);
		}
		out.close();
		in.close();
		try                 { socket.close();      }
		catch (Exception e) { e.printStackTrace(); }
		System.err.println("closing socket");
	}


	/*********************************************************************
	 *  The methods getMessage() and setMessage() are synchronized
	 *  so that the thread in Connection doesn't call setMessage()
	 *  while the ConnectionListener thread is calling getMessage().
	 *********************************************************************/
	public synchronized String getMessage() {
		if (message == null) return null;
		String temp = message;
		message = null;
		notifyAll();
		return temp;
	}

	public synchronized void setMessage(String s) {
		if (message != null) {
			try                  { wait();               }
			catch (Exception ex) { ex.printStackTrace(); }
		}
		message = s;
		System.out.println(s);
		ChatServer.log.add(s);
		ChatServer.onlineListTracker(s);
		ChatServer.banListTracker(s);
		ChatServer.commandTracker(s);
		writeLogs(s);
	}

	public void writeLogs(String message)
	{

		try{
			// Create file 
			FileWriter fstream = new FileWriter("Logs.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for(String e : ChatServer.log)
			{
				out.write(e);
				out.newLine();
			}
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}


	}



}
