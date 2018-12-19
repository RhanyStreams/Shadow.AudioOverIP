package rs.shadow.aoip;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneReceiver {
	
	AudioFormat format;
	
	SourceDataLine speakers;
	
	public MicrophoneReceiver(AudioFormat format) {
		this.format = format;
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, this.format);
        try {
			speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	        speakers.open(this.format, speakers.getBufferSize());
	        speakers.start();
	        
	        startReceiving();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static Mixer getMixerByName(String toFind) {
	    for(Mixer.Info info : AudioSystem.getMixerInfo()) {
	    	String name = info.getName();
	    	
	    	if(name.contains(toFind)) {
		    	System.out.println(name);
	            return AudioSystem.getMixer(info);
	    	}
	    }
	    return null;
	}
	
	public void startReceiving() throws LineUnavailableException {        
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
