package com.sawuasfoiythl.chatServer.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Updater{

	BufferedInputStream input; //not used
	BufferedInputStream fileInput;
	BufferedOutputStream output;

	public Updater(Socket client) throws IOException{
		output = new BufferedOutputStream(client.getOutputStream());
		input = new BufferedInputStream(client.getInputStream());
	}


	public void sendUpdate(final String name) {
		Thread updater = new Thread (){
			public void run()
			{
				try
				{
					File perm = new File(System.getProperty("user.dir")+"/" + name + ".jar");
					//fileInput = new BufferedInputStream(new FileInputStream(perm));
					fileInput = new BufferedInputStream(new FileInputStream(perm));

					byte[] buffer = new byte[1024];
					int numRead;
					while((numRead = fileInput.read(buffer)) != -1)
						output.write(buffer, 0, numRead);






					

					output.flush();
					output.close();
					//input.close();
					//fileInput.close();
					
					//this.interrupt();
				}
				catch(Exception e)
				{e.printStackTrace();}
			}
		};

		updater.start();

	}

	/*
	public void sendUpdate(String name, Socket socket) {

		Thread updater = new Thread (){
			public void run()
			{
			    try
			    {
			        File perm = new File(System.getProperty("user.dir")+"/MStC.jar");
			        //fileInput = new BufferedInputStream(new FileInputStream(perm));
			        fileInput = new BufferedInputStream(new FileInputStream(perm));

			        byte[] buffer = new byte[1024];
			        int numRead;
			        while((numRead = fileInput.read(buffer)) != -1)
			            output.write(buffer, 0, numRead);

			        output.flush();

			        fileInput.close();
			        input.close();
			        output.close();
			        this.interrupt();
			    }
			    catch(Exception e)
			    {e.printStackTrace();}
			}
		};

		updater.start();
	}
	 */

	/*
	public void recieveUpdate(String name, String ip, int port)  throws IOException, UnknownHostException{
		System.out.println("Connecting to update socket");
	    Socket update = new Socket(ip,4450);
	    BufferedInputStream is = new BufferedInputStream(update.getInputStream());
	    BufferedOutputStream os = new BufferedOutputStream(update.getOutputStream());

	    System.out.println("Cleaning GameClient.jar file");
	    File updated = new File(System.getProperty("user.dir")+"/GameClient.jar");
	    if(updated.exists())
	        updated.delete();
	    updated.createNewFile();

	    BufferedOutputStream osf = new BufferedOutputStream(new FileOutputStream(updated));

	    System.out.println("Writing to GameClient.jar");
	    byte[] buffer = new byte[1024];
	    int numRead = 0;
	    while((numRead = is.read(buffer)) != -1)
	        osf.write(buffer, 0, numRead);

	    System.out.println("Finished updating...");


	    os.flush();
	    osf.flush();

	    is.close();
	    os.close();
	    update.close();
	    osf.close();

	}

	 */


	public void recieveUpdate(final String name, Socket socket) throws IOException, UnknownHostException{

		System.out.println("Connecting to update socket");
		BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
		BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());

		System.out.println("Cleaning MStC.jar file");
		File updated = new File(System.getProperty("user.dir")+"/" + name +".jar");
		if(updated.exists())
			updated.delete();
		updated.createNewFile();

		BufferedOutputStream osf = new BufferedOutputStream(new FileOutputStream(updated));

		System.out.println("Writing to MStC.jar");
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while((numRead = is.read(buffer)) != -1)
			osf.write(buffer, 0, numRead);

		System.out.println("Finished updating...");







		//socket.close();
		//osf.flush();
		osf.close();
		// is.close();
//		os.flush();
//		os.close();
	}

}
