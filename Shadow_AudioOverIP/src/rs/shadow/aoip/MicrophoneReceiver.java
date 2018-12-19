package rs.shadow.aoip;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
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
	
	public byte[] mergeArrays(byte[] a, byte[] b) {	    
	    byte[] c = new byte[a.length + b.length];
	    System.arraycopy(a, 0, c, 0, a.length);
	    System.arraycopy(b, 0, c, a.length, b.length);
	    return c;
	}
	
	public void startReceiving() {
        byte[] data = new byte[96000 / 5];
		
		ServerSocket serverSocket = null;
 	    Socket clientSocket = null;
    	try {
			serverSocket = new ServerSocket(55242);
			System.out.println("Server initialized");
		    clientSocket = serverSocket.accept();	
			System.out.println("Client accepted"); 	    
		    InputStream iStream = clientSocket.getInputStream();
		    //PushbackInputStream bis = new PushbackInputStream(iStream);
		    
		    AudioInputStream ais = new AudioInputStream(iStream, format, AudioSystem.NOT_SPECIFIED);
		    
		    int currentRead;

		    while((currentRead = ais.read(data)) != -1) {
		    	//System.out.println("CURRENT: "+currentRead);
		    	
		    	speakers.write(data, 0, currentRead);
		    }
		    
		    System.out.println(currentRead);
		    
		    System.out.println("ENDING?");
		
		    
    	} catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
	}
	
	public static void main(String[]args) {
		AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
		new MicrophoneReceiver(format);
	}
}
