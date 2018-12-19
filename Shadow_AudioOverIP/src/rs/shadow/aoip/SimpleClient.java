package rs.shadow.aoip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
	
	public static void main(String[]args) {
		String hostName = "25.80.71.32";
	    int portNumber = 55242;
	    
	    try{	        
		    write("Try Start");
		    
		    Socket clientSocket = new Socket(hostName, portNumber);
		    write("Socket initialized");
		    
		    OutputStream oStream = clientSocket.getOutputStream();
		    write("OutputStream done");
		    
		    InputStreamReader isr = new InputStreamReader(System.in);
		    write("InputStreamReader done");
		    
		    boolean running = true;		    
		    Integer line;
		    
		    // NETWORK writing
		    while(running) {
		    	write("READING");
		    	line = isr.read();
		    	write(""+line);
		    	oStream.write(line);
		    	System.out.println("WRITTEN");
		    }
		    
		    clientSocket.close();		
	    } catch(IOException ioe) {
	    	
	    }
	}
	
	public static void write(String string) {
		System.out.println(string);
	}
}
