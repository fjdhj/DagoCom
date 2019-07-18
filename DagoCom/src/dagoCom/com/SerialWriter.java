package dagoCom.com;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerialWriter implements Runnable{
	
	private static OutputStream out;
	private static String str = "";
	private static boolean running = true;
	
	public SerialWriter() {
		System.out.println("Initialisation du SerialWriter");
		try {
			out = SerialCom.serialPort.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("SerialWriter: Thread lancé");
		while(running) {
			if(!str.isEmpty()) {
					try { 
					 System.out.println(str);
					 InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)); //Convertion String -> InputStream
					 int c = 0;
		            while ( ( c = stream.read()) > -1 ){
		                out.write(c);
		            }                
					} catch ( IOException e ){
						e.printStackTrace();
					}finally {
						str = str.substring(str.indexOf("\n")+2);
					}
			}
		}
     }
	
	public static void sendCommand(String command) {
		str += command + "\n";
	}
	
	public static void stop() {
		running = false;
	}
	
    /*public SerialWriter(OutputStream out, String str) {
    	Thread t = new Thread(new Runnable() {           
            public void run() { 
            	try { 
            		InputStream stream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)); //Convertion String -> InputStream
            		int c = 0;
            		while ( ( c = stream.read()) > -1 ){
            			out.write(c);
            		}                
            	}catch ( IOException e ){
            		e.printStackTrace();
            	} 
            }
        });
    	t.start();
    }
    
    public SerialWriter(OutputStream out, String[] str, String addToTheLine) { //addToTheLine est enfaite la plus part du tps "\n" mettre "" si ne rien rajouter
    	Thread t = new Thread(new Runnable() {           
            public void run() { 
            	try {
            		for(int i = 0; i<=str.length; i++) {
                		InputStream stream = new ByteArrayInputStream((str[i] + addToTheLine).getBytes(StandardCharsets.UTF_8)); //Convertion String -> InputStream
                		int c = 0;
                		while ( ( c = stream.read()) > -1 ){
                			out.write(c);
                		}	
            		}
                
            	}catch ( IOException e ){
                 e.printStackTrace();
            	} 
           } 
        });
    	t.start();
    }*/

}

