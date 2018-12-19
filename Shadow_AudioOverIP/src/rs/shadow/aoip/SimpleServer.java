package rs.shadow.aoip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	public static void main(String[]args) {
		int portNumber = 55242;         
 	    ServerSocket serverSocket;
 	    
		try {
			write("Try Start");
			serverSocket = new ServerSocket(portNumber);
			write("Socket initialized");
	 	    Socket clientSocket = serverSocket.accept();
	 	    write("Accepted client");
	 	    System.out.println("Client connected!");
	 	    
	 	    InputStream iStream = clientSocket.getInputStream();
	 	    		
	 	    Integer intC;
	 	    
	 	    System.out.println("Start read/writing");
	 	    		
	 	    while((intC = iStream.read()) != null) {
	 	    	write("READ");
	 	    	write(""+intC);
	 	    	write("WRITTEN");
	 	    }
	 	    
	 	    iStream.close();
	 	    clientSocket.close();
	 	    serverSocket.close();
	 	    
	 	    write("everything closed");
	 	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void write(String string) {
		System.out.println(string);
	}
}
