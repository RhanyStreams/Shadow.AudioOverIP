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
    File wavFile = new File("D:/Eclipse/RecordAudio.au");
 
    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.AU;
 
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
 	    
        acceptInc();
    }
    
    public void acceptInc() {
    	
    	int portNumber = 55242;
 	    int line = 0;
 	    
 	    ServerSocket serverSocket = null;
 	    Socket clientSocket = null;
    	try {
    		serverSocket = new ServerSocket(portNumber);
	 	    clientSocket = serverSocket.accept();	 	    
	 	    InputStream iStream = clientSocket.getInputStream();
	 	    BufferedInputStream ois = new BufferedInputStream(iStream);
	 	    FileWriter fWriter = new FileWriter(wavFile);
	 	    
	 	    while((line = ois.read()) != -1) {
	 	    	fWriter.write(line);
	 	    }
    	 	
	 	    fWriter.close();
	 	    ois.close();
	 	    iStream.close();
	 	    
	 	    write("Streams closed");
	 	    
			clientSocket.close();
	 	    serverSocket.close();
	 	    //acceptInc();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
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