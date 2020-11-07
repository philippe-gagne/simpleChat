// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  String loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginID = loginID;
    this.clientUI = clientUI;
    try {
    	openConnection();
    	sendToServer("#login "+ loginID);
    } catch (Exception e) {
    	System.out.println("ERROR: Could not connect to server. Awaiting command.");
    }
  }
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
 * @throws IOException 
   */
  public void handleMessageFromClientUI(String message) throws IOException{
	
	if(message.charAt(0)=='#') {  
		
		String command = message.split(" ")[0];
		
		if(command.equals("#sethost")) {
			if(!super.isConnected()) {
				super.setHost(message.split(" ")[1]);
				System.out.println("> Host set to: "+ message.split(" ")[1]);
			}else{
				System.out.println("> You must log off (#logoff) from the server before setting the host");
			}	
			
		}else if(command.equals("#setport")) {
			if(!super.isConnected()) {
				super.setPort(Integer.parseInt(message.split(" ")[1]));
				System.out.println("> Port set to: "+ message.split(" ")[1]);
			}else {
				System.out.println("> You must log off (#logoff) from the server before setting the port");
			}
			
	  	}else if (command.equals("#quit")){
	  		sendToServer(message);
			quit();
			
	  	}else if (command.equals("#logoff")){
	  		sendToServer(message);
			closeConnection();
	  	}else if (command.equals("#login")){		
			if(!super.isConnected()) {
				super.openConnection();
				sendToServer(message);
			}else {
				System.out.println("> You're already connected to a server");}
			
	  	}else if (command.equals("#gethost")){		
			super.getHost();
			
	  	}else if (command.equals("#getport")){		
			super.getPort();
			
		}else {
			System.out.println("> Invalid command.");
		}
	}else{
	    try{
	      sendToServer(message);
	      
	    }catch(IOException e){
	      clientUI.display("Could not send message to server.  Terminating client.");
	      quit();
	    }
	}
  }
  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		System.out.println("The connection to the server was closed");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		System.out.print("The connection to the server was terminated ("+exception+")");
	}

	public String getLogin() {
		return loginID;
	}
	
	
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
