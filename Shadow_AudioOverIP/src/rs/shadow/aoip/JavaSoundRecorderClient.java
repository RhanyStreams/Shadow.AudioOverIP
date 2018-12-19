package rs.shadow.aoip;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
 
/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class JavaSoundRecorderClient {
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
		AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
	    TargetDataLine microphone;
	    SourceDataLine speakers;
	    
        try {
        	microphone = AudioSystem.getTargetDataLine(format);

	        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	        microphone = (TargetDataLine) AudioSystem.getLine(info);
	        microphone.open(format);
            
            // NETWORK writing            
            String hostName = "25.80.71.32";
    	    int portNumber = 55242;    
		    write("Try Start");
		    
		    Socket clientSocket = new Socket(hostName, portNumber);
		    write("Socket initialized");
		    
		    OutputStream oStream = clientSocket.getOutputStream();
		    write("OutputStream done");
		    
		    BufferedOutputStream bos = new BufferedOutputStream(oStream);
		    
		    boolean running = true;
		    
		    while(running) {		    	
	            //AudioSystem.write(ais, fileType, bos);  
			    
			    //write("???"+ais.getFrameLength());
		    	//bos.write(ais.read(new byte[8]));
		    	write("SENDING");
		    }
		    
		    clientSocket.close();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
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
        final JavaSoundRecorderClient recorder = new JavaSoundRecorderClient();
 
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });
 
        stopper.start();
 
        // start recording
        recorder.start();
    }
}