package rs.shadow.aoip;

import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 
/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class JavaSoundRecorderServer {
    // record duration, in milliseconds
    static final long RECORD_TIME = 180 * 1000;
 
    // path of the wav file
    File wavFile = new File("D:/Test.wav");
 
    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
 
    // the line from which audio data is captured
    TargetDataLine line;
 
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 48000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }
 
    /**
     * Captures the sound and record into a WAV file
     */
    void start() {
        try {        	
        	AudioFormat format = getAudioFormat();
        	
        	int portNumber = 55242;         
     	    ServerSocket serverSocket;
     	    
			write("Try Start");
			serverSocket = new ServerSocket(portNumber);
			write("Socket initialized");
	 	    Socket clientSocket = serverSocket.accept();
	 	    write("Accepted client");
	 	    System.out.println("Client connected!");
	 	    
	 	    InputStream iStream = clientSocket.getInputStream();
	 	    AudioInputStream ais = new AudioInputStream(iStream, format, 8);
	 	    		
	 	    Integer intC;
	 	    
	 	    System.out.println("Start read/writing");
	 	    		
	 	    AudioSystem.write(ais, fileType, wavFile);  
	 	    
	 	    ais.close();
	 	    iStream.close();
	 	    clientSocket.close();
	 	    serverSocket.close();
    	 	    
	 	    write("everything closed");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
	public static void write(String string) {
		System.out.println(string);
	}
 
    /**
     * Closes the target data line to finish capturing and recording
     */
    void finish() {
        line.stop();
        line.close();
        System.out.println(System.currentTimeMillis() + ": Finished");
    }
 
    /**
     * Entry to run the program
     */
    public static void main(String[] args) {
        final JavaSoundRecorderServer server = new JavaSoundRecorderServer();
 
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                server.finish();
            }
        });
 
        stopper.start();
 
        // start recording
        server.start();
    }
}