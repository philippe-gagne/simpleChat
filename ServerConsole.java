import java.io.IOException;
import java.util.Scanner;
import common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 6655;
	
	EchoServer server;
	
	Scanner fromConsole;
	
	
	public ServerConsole(int port) throws IOException {
		// TODO Auto-generated constructor stub
		try 
	    {
			this.server = new EchoServer(port);	      
	    } 
	    catch(Exception exception) 
	    {
	      System.out.println("Error: Failed to setup Server"
	                + " Terminating client.");
	      System.exit(1);
	    }
		
		server.listen();
		fromConsole = new Scanner(System.in); 
	}

	 public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerConsole(message);
	        display(message);
	      }
	      
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println("> "+message);
	}

	public static void main(String[] args) throws IOException {
		
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	
	    ServerConsole serverConsole = new ServerConsole(port);
	    serverConsole.accept();  //Wait for console data
	 }

}
