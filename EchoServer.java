// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 6655;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient (Object msg, ConnectionToClient client) {
	  
	 String message = msg.toString();
	 
	if(message.split(" ")[0].equals("#login")) {
		
		if(client.getInfo("Login")==null) {
			System.out.println("Message received: \"" + msg + "\" from user "+client.getInfo("Login"));
			client.setInfo("Login", message.split(" ")[1]);	
			System.out.println(client.getInfo("Login") + " has logged in.");
			
		} else {
			try {
				client.sendToClient("ERROR: You must disconnect before logging in with a new Login ID.  Terminating connection");
				client.close();
			} catch (IOException e) {
				System.out.println("An error occured while attempting to communicate with the client. Terminating server.");
				System.exit(1);
			}
		}
	} else if (message.split(" ")[0].equals("#logoff")){ 	
		this.sendToAllClients(client.getInfo("Login")+" has disconnected.");
		
	} else if (message.split(" ")[0].equals("#quit")){ 		
		this.sendToAllClients(client.getInfo("Login")+" has disconnected.");
		
	} else { 
		System.out.println("Message received: \"" + msg + "\" from user "+client.getInfo("Login"));
	    this.sendToAllClients(client.getInfo("Login")+": "+ msg);
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client is attempting to connect to SimpleChat");
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println(client.getInfo("Login")+" has disconnected from SimpleChat");
  }
  
  public void handleMessageFromServerConsole(String message) throws IOException {
	  
	  if(message.charAt(0)=='#') {  
			
		String command = message.split(" ")[0];
		
		if (command.equals("#start")){		
	  		if(!super.isListening()) {
	  			super.listen();
	  		} else {
	  			System.out.println("The server is already listening for connections");
	  		}
	  		
		}else if (command.equals("#stop")){
	  		if(super.isListening()) {
	  			super.stopListening();
	  			sendToAllClients("WARNING - The server has stopped listening for connections");
	  		} else {
	  			System.out.println("The server has already stopped listening for connecitons");
	  		}
	  		
		} else if(command.equals("#setport")) {
			if(!super.isListening()) {
				super.setPort(Integer.parseInt(message.split(" ")[1]));
			} else {
				System.out.println("The server must be closed before setting a new port");
			}
		
		}else if (command.equals("#close")){
			sendToAllClients("SERVER SHUTTING DOWN.  DISCONNECTING FROM SERVER.");
	  		super.close();
	  		
	  	}else if (command.equals("#quit")){
			super.close();
			System.exit(0);		
	  		
	  	}else if (command.equals("#getport")){		
			System.out.println("Port: "+super.getPort());
			
		}else {
			System.out.println("Invalid command.");
		}
	}else{
	    try{
	      sendToAllClients("SERVER MSG> "+message);
	      
	    }catch(Exception e){
	      System.out.println("Could not send message to server.  Terminating client.");
	    }
	}
	  
  }
  
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
