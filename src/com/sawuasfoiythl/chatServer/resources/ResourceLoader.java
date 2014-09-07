package com.sawuasfoiythl.chatServer.resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ResourceLoader {

	static ResourceLoader rl = new ResourceLoader();
	
	public static Image getImage(String fileName) {
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("/com/sawuasfoiythl/chatServer/images/" + fileName));	
		
	}
	
	public static BufferedImage getBufferedImage(String fileName){
	     BufferedImage b = null; //Create the BufferedImage that we wish to return
	     try{ //Our method could throw an Exception so we use try/catch
	          b = ImageIO.read(rl.getClass().getResource("/com/sawuasfoiythl/chatServer/images/" + fileName)); //Make sure to update the fileName parameter to make it work with your directory setup
	     }catch(Exception e){
	          e.printStackTrace(System.err);
	     }
	     //Return the BufferedImage we have loaded
	     return b;
	}
	
}
