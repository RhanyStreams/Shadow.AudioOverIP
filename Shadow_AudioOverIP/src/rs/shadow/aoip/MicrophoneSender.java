package rs.shadow.aoip;

import java.io.OutputStream;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneSender {
	
	TargetDataLine microphone;
    final int CHUNK_SIZE = 128;
    int numBytesReadFromMicrophone;
    AudioFormat format;
    byte[] data;
	
	public MicrophoneSender(AudioFormat format) {
		this.format = format;
		
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
		try {
			microphone = AudioSystem.getTargetDataLine(this.format);
	        microphone = (TargetDataLine) AudioSystem.getLine(info);
	        microphone.open(this.format);
	        
	        startSending();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void startSending() {         
        microphone.start(); 
        data = new byte[microphone.getBufferSize() / 5];

        String hostName = "25.80.71.32";
	    int portNumber = 55242;  
	    
	    Socket clientSocket;
	    OutputStream oStream;
		try {
			clientSocket = new Socket(hostName, portNumber);		    
		    oStream = clientSocket.getOutputStream();
	        
	        boolean running = true;
	        
	        System.out.println("STARTING");
	        
	        
		    while(running) {	
		        numBytesReadFromMicrophone = microphone.read(data, 0, CHUNK_SIZE);
		        oStream.write(data, 0, numBytesReadFromMicrophone);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String[]args) {
		AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
		new MicrophoneSender(format);
	}
}
