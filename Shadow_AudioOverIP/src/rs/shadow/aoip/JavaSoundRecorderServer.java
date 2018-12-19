package rs.shadow.aoip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
 
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
 	    AudioFormat format = getAudioFormat();
 	    
 	    ServerSocket serverSocket = null;
 	    Socket clientSocket = null;
    	try {
			serverSocket = new ServerSocket(portNumber);
		    clientSocket = serverSocket.accept();	 	    
		    InputStream iStream = clientSocket.getInputStream();
		    BufferedInputStream ois = new BufferedInputStream(iStream);
		    FileWriter fWriter = new FileWriter(wavFile);
			
		    Mixer.Info[] mi = AudioSystem.getMixerInfo();
			for (Mixer.Info info : mi) {
				System.out.println("info: " + info);
			Mixer m = AudioSystem.getMixer(info);
				
				Line[] lines = m.getSourceLines();
				
				write("NAME: "+info.getName());
				write("DESC: "+info.getDescription());
				write("VEND: "+info.getVendor());
				
				if(info.getName().contains("Microphone") && info.getDescription().contains("DirectSound Capture")) {
					write("CONFIRMED Mic");
					SourceDataLine sDataLine = (SourceDataLine) lines[0];
					write("Got Data Line");
					sDataLine.open(format);
					write("Data Line Open");
			        byte[] buf = new byte[1024];
				    
				    while((line = ois.read()) != -1) {
				    	fWriter.write(line);
						sDataLine.write(buf, 0, line);
				    }
					write("WRITTEN TO SOURCELINE");
				}
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
        } catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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