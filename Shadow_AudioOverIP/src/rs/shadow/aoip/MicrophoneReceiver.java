package rs.shadow.aoip;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MicrophoneReceiver {
	
	AudioFormat format;
	
	SourceDataLine speakers;
	
	public MicrophoneReceiver(AudioFormat format) {
		this.format = format;
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, this.format);
        try {
			speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	        speakers.open(this.format);
	        speakers.start();
	        
	        startReceiving();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startReceiving() {
        byte[] data = new byte[16000 / 5];
		
		ServerSocket serverSocket = null;
 	    Socket clientSocket = null;
    	try {
			serverSocket = new ServerSocket(55242);
			System.out.println("Server initialized");
		    clientSocket = serverSocket.accept();	
			System.out.println("Client accepted"); 	    
		    InputStream iStream = clientSocket.getInputStream();
		    
		    int currentRead;
		    
		    while((currentRead = iStream.read(data)) != -1) {
		    	speakers.write(data, 0, currentRead);
		    }
		
		    
    	} catch(IOException ioe) {
    		
    	}
	}
	
	public static void main(String[]args) {
		AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
		MicrophoneReceiver mReceiver = new MicrophoneReceiver(format);
	}
}
