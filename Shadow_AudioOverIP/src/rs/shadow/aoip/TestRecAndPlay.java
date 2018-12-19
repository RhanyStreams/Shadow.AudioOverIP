package rs.shadow.aoip;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.*;

public class TestRecAndPlay {
	public static void main(String[]args) {
		AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
	    TargetDataLine microphone;
	    SourceDataLine speakers;
	    try {
	        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	        microphone = AudioSystem.getTargetDataLine(format);
	        microphone = (TargetDataLine) AudioSystem.getLine(info);
	        microphone.open(format);

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int numBytesReadFromMicrophone;
	        int overallBytesRead = 0;
	        int CHUNK_SIZE = 1024;
	        byte[] data = new byte[microphone.getBufferSize() / 5];
	        //System.out.println(microphone.getBufferSize()+"BUFFER");
	        
	        microphone.start();
	        
	        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
	        speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	        speakers.open(format);
	        speakers.start();
	        
	        boolean running = true;
	        while (running) {
	            numBytesReadFromMicrophone = microphone.read(data, 0, CHUNK_SIZE);
	            //overallBytesRead += numBytesReadFromMicrophone;
	            // write the mic data to a stream for use later
	            //out.write(data, 0, numBytesReadFromMicrophone); 
	            // write mic data to stream for immediate playback
	            System.out.println("DATA:"+data+"//BYTES:"+numBytesReadFromMicrophone);
	            speakers.write(data, 0, numBytesReadFromMicrophone);
	        }
	        
	        speakers.drain();
	        speakers.close();
	        microphone.close();
	    } catch (LineUnavailableException e) {
	        e.printStackTrace();
	    } 
	}
}
